package com.ljf.risk.engine.core.component;

import com.ljf.risk.engine.core.constants.CurrentContext;
import com.ljf.risk.engine.entity.Condition;
import com.ljf.risk.engine.entity.Rule;
import com.ljf.risk.engine.entity.constants.ConditionRelationType;
import com.ljf.risk.engine.entity.constants.Logic;
import com.ljf.risk.engine.core.comparator.Comparator;
import com.ljf.risk.engine.core.component.load.AbstractComponent;
import com.ljf.risk.engine.service.ConditionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author lijinfeng
 */
@Slf4j
@Component
public class ConditionComponent extends AbstractComponent<Long, Condition> {

    private final ConditionService conditionService;

    @Autowired
    private ConditionAnalyzeComponent conditionAnalyzeComponent;

    @Autowired
    private ComparatorComponent comparatorComponent;

    public ConditionComponent(ConditionService conditionService) {
        this.conditionService = conditionService;
    }

    @Override
    public String getComponentName() {
        return "条件";
    }

    @Override
    public void load() {
        List<Condition> list = conditionService.list();
        this.cache = list.stream().collect(Collectors.toConcurrentMap(Condition::getId, condition -> condition));
    }

    public Map<Long, List<Condition>> getCacheByRules(List<Rule> rules) {
        if (CollectionUtils.isEmpty(rules)) {
            return new ConcurrentHashMap<>();
        }
        List<Long> ruleIds = rules.stream().map(Rule::getId).collect(Collectors.toList());
        Collection<Condition> conditions = cache.values();
        return conditions.stream()
                .filter(condition -> ruleIds.contains(condition.getRelationId()) && Objects.equals(condition.getRelationType(), ConditionRelationType.RULE))
                .collect(Collectors.groupingByConcurrent(Condition::getRelationId));
    }

    public List<Condition> getCacheByRelationId(Long relationId, ConditionRelationType relationType) {
        if (relationId == null || relationType == null) {
            return new ArrayList<>();
        }
        Collection<Condition> conditions = cache.values();
        return conditions.stream()
                .filter(condition -> Objects.equals(relationId, condition.getRelationId()) && Objects.equals(condition.getRelationType(), relationType))
                .collect(Collectors.toList());
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public Boolean conditionHandle(Long relationId, ConditionRelationType relationType, Logic logic, CurrentContext.Context context) {
        List<Condition> cacheByRelationId = getCacheByRelationId(relationId, relationType);
        Boolean hit = null;
        for (Condition condition : cacheByRelationId) {
            hit = analyse(condition, context);
            if (Logic.OR.equals(logic) && hit) {
                return true;
            }
            if (Logic.AND.equals(logic) && !hit) {
                return false;
            }
        }
        return hit;
    }

    public Boolean analyse(Condition condition, CurrentContext.Context context) {
        try {
            Object leftValue = conditionAnalyzeComponent.getConditionValue(condition.getLeftType(), condition.getLeftProperty(), condition.getLeftValue(), condition.getComparator(), context);
            Object rightValue = conditionAnalyzeComponent.getConditionValue(condition.getRightType(), condition.getRightProperty(), condition.getRightValue(), condition.getComparator(), context);
            if (Objects.isNull(leftValue) && Objects.isNull(rightValue)) {
                return false;
            }
            Comparator comparator = comparatorComponent.getCacheByComponentName(StringUtils.lowerCase(condition.getComparator().name()));
            boolean compare = comparator.compare(leftValue, rightValue);
            String conditionDetail = condition.getConditionDetail(leftValue, rightValue);
            log.info("{}, {}, {} condition calculate, {}, result: {}", this.getClass().getSimpleName(), condition.getCode(), condition.getRelationType().name(), conditionDetail, compare);
            CurrentContext.currentCtx().getConditionResult().put(condition.getCode(), conditionDetail);
            return compare;
        } catch (Exception e) {
            log.error("{}, {} analyse failed: ", this.getClass().getSimpleName(), condition.getCode(), e);
            return false;
        }
    }

    public Boolean getConditionResult(Boolean conditionHit, Boolean conditionGroupHit, Logic logic, ConditionRelationType conditionRelationType) {
        if (Objects.isNull(conditionHit) && Objects.isNull(conditionGroupHit)) {
            return !ConditionRelationType.RULE.equals(conditionRelationType);
        } else {
            if (Objects.isNull(conditionHit)) {
                return conditionGroupHit;
            }
            if (Objects.isNull(conditionGroupHit)) {
                return conditionHit;
            }
            if (Logic.OR.equals(logic)) {
                return conditionHit || conditionGroupHit;
            }
            if (Logic.AND.equals(logic)) {
                return conditionHit && conditionGroupHit;
            }
        }
        return false;
    }
}
