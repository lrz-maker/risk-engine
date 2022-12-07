package com.ljf.risk.engine.core.indicator.redis;

import com.ljf.risk.engine.core.constants.CurrentContext;
import com.ljf.risk.engine.core.indicator.IndicatorHandle;
import com.ljf.risk.engine.core.component.ConditionComponent;
import com.ljf.risk.engine.core.component.ConditionGroupComponent;
import com.ljf.risk.engine.entity.Indicator;
import com.ljf.risk.engine.entity.constants.ConditionRelationType;
import com.ljf.risk.engine.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisIndicatorHandle implements IndicatorHandle {

    private final ConditionComponent conditionComponent;
    private final ConditionGroupComponent conditionGroupComponent;
    private final StringRedisTemplate redisTemplate;

    public final String INDICATOR_KEY_PREFIX = "risk:indicator:";

    public RedisIndicatorHandle(ConditionComponent conditionComponent, ConditionGroupComponent conditionGroupComponent, StringRedisTemplate redisTemplate) {
        this.conditionComponent = conditionComponent;
        this.conditionGroupComponent = conditionGroupComponent;
        this.redisTemplate = redisTemplate;
    }

    private void buildIndicatorCache(Map<String, Object> eventDetails, List<Indicator> indicators, List<KeyValueIndicator> keyValueIndicators, List<ZSetIndicator> zSetIndicators, boolean isAnalysis) {
        next:
        for (Indicator indicator : isAnalysis ? new HashSet<>(indicators) : indicators) {
            if (handleCondition(indicator)) {
                List<String> keyList = new ArrayList<>();
                for (String dimension : indicator.getDimension()) {
                    String value = MapUtils.getString(eventDetails, dimension);
                    if (StringUtils.isBlank(value)) {
                        log.info("indicator, continue build cache: dimension {} is blank", dimension);
                        continue next;
                    }
                    keyList.add(value);
                }
                String keySuffix = StringUtils.join(keyList, ".");
                if (Indicator.IndicatorType.simpleValue(indicator.getType())) {
                    String value = MapUtils.getString(eventDetails, indicator.getFactor());
                    if (StringUtils.isBlank(value)) {
                        log.info("indicator, continue build cache: factor {} is blank", indicator.getFactor());
                        continue;
                    }
                    keyValueIndicators.add(buildKeyValueIndicator(indicator, keySuffix, value, isAnalysis));
                } else if (Indicator.IndicatorType.factorCount(indicator.getType())) {
                    String value;
                    if (StringUtils.isBlank(indicator.getFactor())) {
                        value = UUID.randomUUID().toString();
                    } else {
                        value = MapUtils.getString(eventDetails, indicator.getFactor());
                        if (StringUtils.isBlank(value)) {
                            log.info("indicator, continue build cache: factor {} is blank", indicator.getFactor());
                            continue;
                        }
                    }
                    zSetIndicators.add(buildZSetIndicator(indicator, keySuffix, value, isAnalysis));
                }
            }
        }
    }

    private KeyValueIndicator buildKeyValueIndicator(Indicator indicator, String keySuffix, String value, boolean isAnalysis) {
        String key = INDICATOR_KEY_PREFIX + (StringUtils.isNotBlank(keySuffix) ? StringUtils.join(Arrays.asList("simple", indicator.getCode(), keySuffix), ":") : StringUtils.join("simple", indicator.getCode(), ":"));
        KeyValueIndicator.KeyValueIndicatorBuilder<?, ?> keyValueIndicatorBuilder = KeyValueIndicator.builder().key(key).value(value);
        Long expire;
        if (isAnalysis) {
            boolean notBlank = StringUtils.isNotBlank(indicator.getAnalysisPeriod());
            expire = TimeUtils.timeAmountChangeMillis(notBlank ? indicator.getAnalysisPeriod() : indicator.getPeriod());
            keyValueIndicatorBuilder.indicatorResultCacheKey(notBlank ? indicator.getId() + "." + indicator.getAnalysisPeriod() : indicator.getId().toString());
        } else {
            expire = TimeUtils.timeAmountChangeMillis(indicator.getPeriod());
        }
        return keyValueIndicatorBuilder.expire(expire).build();
    }

    private ZSetIndicator buildZSetIndicator(Indicator indicator, String keySuffix, String value, boolean isAnalysis) {
        ZSetIndicator.ZSetIndicatorBuilder<?, ?> zSetIndicatorBuilder = ZSetIndicator.builder().member(value).score(CurrentContext.getEventTime().getTime());
        if (StringUtils.isBlank(indicator.getFactor())) {
            String key = INDICATOR_KEY_PREFIX + (StringUtils.isNotBlank(keySuffix) ? StringUtils.join(Arrays.asList("distinct", indicator.getCode(), keySuffix), ":") : StringUtils.join("distinct", indicator.getCode(), ":"));
            zSetIndicatorBuilder.key(key);
        } else {
            String key = INDICATOR_KEY_PREFIX + (StringUtils.isNotBlank(keySuffix) ? StringUtils.join(Arrays.asList(indicator.getCode(), keySuffix), ":") : indicator.getId());
            zSetIndicatorBuilder.key(key);
        }

        Long expire;
        if (isAnalysis) {
            boolean notBlank = StringUtils.isNotBlank(indicator.getAnalysisPeriod());
            expire = TimeUtils.timeAmountChangeMillis(notBlank ? indicator.getAnalysisPeriod() : indicator.getPeriod());
            zSetIndicatorBuilder.indicatorResultCacheKey(notBlank ? indicator.getId() + "." + indicator.getAnalysisPeriod() : indicator.getId().toString());
        } else {
            expire = TimeUtils.timeAmountChangeMillis(indicator.getPeriod());
        }
        return zSetIndicatorBuilder.expire(expire).build();
    }


    private Boolean handleCondition(Indicator indicator) {
        Boolean conditionHit = conditionComponent.conditionHandle(indicator.getId(), ConditionRelationType.INDICATOR, indicator.getLogic(), CurrentContext.currentCtx());
        Boolean conditionGroupHit = conditionGroupComponent.conditionGroupHandle(indicator.getId(), ConditionRelationType.INDICATOR, indicator.getLogic(), CurrentContext.currentCtx());
        return conditionComponent.getConditionResult(conditionHit, conditionGroupHit, indicator.getLogic(), ConditionRelationType.INDICATOR);
    }

    @Override
    public void accumulate(List<Indicator> indicators) {
        if (CollectionUtils.isEmpty(indicators)) {
            return;
        }
        long startTime = System.currentTimeMillis();
        Map<String, Object> eventDetails = CurrentContext.currentCtx().getEngineCheckReq().getEventDetails();
        List<KeyValueIndicator> keyValueIndicators = new ArrayList<>();
        List<ZSetIndicator> zSetIndicators = new ArrayList<>();
        buildIndicatorCache(eventDetails, indicators, keyValueIndicators, zSetIndicators, false);
        if (CollectionUtils.isNotEmpty(keyValueIndicators) || CollectionUtils.isNotEmpty(zSetIndicators)) {
            redisTemplate.executePipelined(new SessionCallback<String>() {
                @Override
                public String execute(RedisOperations operations) throws DataAccessException {
                    long now = System.currentTimeMillis();
                    for (KeyValueIndicator keyValueIndicator : keyValueIndicators) {
                        operations.opsForValue().setIfAbsent(keyValueIndicator.getKey(), keyValueIndicator.getValue(), keyValueIndicator.getExpire(), TimeUnit.MILLISECONDS);
                        log.info("indicator accumulate model: {}", keyValueIndicator);
                    }

                    for (ZSetIndicator zSetIndicator : zSetIndicators) {
                        operations.opsForZSet().add(zSetIndicator.getKey(), zSetIndicator.getMember(), zSetIndicator.getScore());
                        operations.opsForZSet().removeRangeByScore(zSetIndicator.getKey(), 0, now - zSetIndicator.getExpire());
                        operations.expire(zSetIndicator.getKey(), zSetIndicator.getExpire(), TimeUnit.MILLISECONDS);
                        log.info("indicator accumulate model: {}", zSetIndicator);
                    }
                    return null;
                }
            });
        }
        log.info("indicator accumulate end, cost: {}", System.currentTimeMillis() - startTime);
    }


    @Override
    public void analysis(List<Indicator> indicators) {
        if (CollectionUtils.isEmpty(indicators)) {
            return;
        }
        long startTime = System.currentTimeMillis();
        List<KeyValueIndicator> keyValueIndicators = new ArrayList<>();
        List<ZSetIndicator> zSetIndicators = new ArrayList<>();
        Map<String, Object> temp = new HashMap<>();
        CurrentContext.Context context = CurrentContext.currentCtx();
        buildIndicatorCache(context.getEngineCheckReq().getEventDetails(), indicators, keyValueIndicators, zSetIndicators, true);
        if (CollectionUtils.isNotEmpty(keyValueIndicators) || CollectionUtils.isNotEmpty(zSetIndicators)) {
            List<Object> results = redisTemplate.executePipelined(new SessionCallback<String>() {
                @Override
                public String execute(RedisOperations operations) throws DataAccessException {
                    long now = System.currentTimeMillis();
                    for (KeyValueIndicator keyValueIndicator : keyValueIndicators) {
                        operations.opsForValue().get(keyValueIndicator.getKey());
                        log.info("indicator analysis model: {}", keyValueIndicator);
                    }
                    for (ZSetIndicator zSetIndicator : zSetIndicators) {
                        operations.opsForZSet().count(zSetIndicator.getKey(), now - zSetIndicator.getExpire(), now);
                        log.info("indicator analysis model: {}", zSetIndicator);
                    }
                    return null;
                }
            });
            for (int i = 0; i < keyValueIndicators.size(); i++) {
                temp.put(keyValueIndicators.get(i).getIndicatorResultCacheKey(), results.get(i));
            }
            for (int i = 0; i < zSetIndicators.size(); i++) {
                temp.put(zSetIndicators.get(i).getIndicatorResultCacheKey(), results.get(keyValueIndicators.size() + i));
            }
            context.setIndicatorResult(temp);
        }
        log.info("indicator analysis end, cost: {}", System.currentTimeMillis() - startTime);
    }


}
