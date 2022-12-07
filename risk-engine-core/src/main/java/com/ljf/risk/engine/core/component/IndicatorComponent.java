package com.ljf.risk.engine.core.component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.ljf.risk.engine.api.EngineApi;
import com.ljf.risk.engine.api.PlayLoadResponse;
import com.ljf.risk.engine.api.indicator.IndicatorAdmin;
import com.ljf.risk.engine.core.component.load.AbstractComponent;
import com.ljf.risk.engine.core.constants.CurrentContext;
import com.ljf.risk.engine.core.indicator.IndicatorHandle;
import com.ljf.risk.engine.entity.Condition;
import com.ljf.risk.engine.entity.ConditionGroup;
import com.ljf.risk.engine.entity.Indicator;
import com.ljf.risk.engine.service.IndicatorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lijinfeng
 */
@Slf4j
@RestController
public class IndicatorComponent extends AbstractComponent<Long, Indicator> implements IndicatorAdmin {

    private final IndicatorService indicatorService;
    private final IndicatorHandle indicatorHandle;

    public IndicatorComponent(IndicatorService indicatorService, IndicatorHandle indicatorHandle) {
        this.indicatorService = indicatorService;
        this.indicatorHandle = indicatorHandle;
    }

    @Override
    public String getComponentName() {
        return "指标";
    }


    @Override
    public void load() {
        List<Indicator> list = indicatorService.list();
        this.cache = list.stream().collect(Collectors.toConcurrentMap(Indicator::getId, indicator -> indicator));
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public List<Indicator> getCacheByConditions(Map<Long, List<Condition>> conditionCacheByRules, Map<Long, List<ConditionGroup>> conditionGroupCacheByRules) {
        List<Indicator> indicators = new ArrayList<>();
        for (List<Condition> value : conditionCacheByRules.values()) {
            for (Condition condition : value) {
                getIndicator(indicators, condition);
            }
        }
        Collection<List<ConditionGroup>> values = conditionGroupCacheByRules.values();
        for (List<ConditionGroup> value : values) {
            for (ConditionGroup conditionGroup : value) {
                for (Condition condition : conditionGroup.getConditionList()) {
                    getIndicator(indicators, condition);
                }
            }
        }
        return indicators;
    }

    private void getIndicator(List<Indicator> indicators, Condition condition) {
        if (Objects.equals(condition.getLeftType(), Condition.PropertyType.INDICATOR)) {
            Indicator indicator = cache.get(Long.parseLong(condition.getLeftProperty()));
            if (Objects.nonNull(indicator)) {
                indicator.setAnalysisPeriod(condition.getLeftValue());
                indicators.add(indicator);
            }
        }
        if (Objects.equals(condition.getRightType(), Condition.PropertyType.INDICATOR)) {
            Indicator indicator = cache.get(Long.parseLong(condition.getRightProperty()));
            if (Objects.nonNull(indicator)) {
                indicator.setAnalysisPeriod(condition.getRightValue());
                indicators.add(indicator);
            }
        }
    }

    public void calculateIndicator(Map<Long, List<Condition>> conditionCacheByRules, Map<Long, List<ConditionGroup>> conditionGroupCacheByRules) {
        List<Indicator> cacheByConditions = getCacheByConditions(conditionCacheByRules, conditionGroupCacheByRules);
        if (CollectionUtils.isNotEmpty(cacheByConditions)) {
            analysis(cacheByConditions);
        }
    }

    public void analysis(List<Indicator> indicators) {
        indicatorHandle.analysis(indicators);
    }

    public void accumulate(List<Indicator> indicators) {
        indicatorHandle.accumulate(indicators);
    }

    @Override
    @PostMapping("test-indicator")
    public PlayLoadResponse<TestResponse> testIndicator(@RequestBody IndicatorAdmin.TestReq testReq) {
        try {
            if (Objects.isNull(testReq.getIndicatorId())) {
                return PlayLoadResponse.failurePlayLoad("", "参数必填");
            }
            Indicator indicator = indicatorService.getOne(new LambdaQueryWrapper<Indicator>().eq(Indicator::getId, testReq.getIndicatorId()));
            if (indicator == null) {
                return PlayLoadResponse.failurePlayLoad("", "该指标已被删除");
            }

            indicator.setAnalysisPeriod(testReq.getPeriod());
            EngineApi.EngineCheckReq build = EngineApi.EngineCheckReq.builder().eventCode(null).eventDetails(testReq.getAttributes()).bizId(UUID.randomUUID().toString()).build();
            try (CurrentContext ignored = new CurrentContext(CurrentContext.Context.builder().eventTime(new Date()).engineCheckReq(build).build())) {
                analysis(Lists.newArrayList(indicator));
                Map<String, Object> indicatorResult = CurrentContext.currentCtx().getIndicatorResult();
                return PlayLoadResponse.success(IndicatorAdmin.TestResponse.builder().result(new ArrayList<>(indicatorResult.values()).get(0).toString()).build());
            }
        } catch (Exception e) {
            log.error("", e);
            return PlayLoadResponse.failurePlayLoad("", "获取指标结果失败");
        }
    }
}
