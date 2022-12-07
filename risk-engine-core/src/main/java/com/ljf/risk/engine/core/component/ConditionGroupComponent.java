package com.ljf.risk.engine.core.component;

import com.ljf.risk.engine.core.constants.CurrentContext;
import com.ljf.risk.engine.entity.Condition;
import com.ljf.risk.engine.entity.constants.ConditionRelationType;
import com.ljf.risk.engine.entity.ConditionGroup;
import com.ljf.risk.engine.entity.Rule;
import com.ljf.risk.engine.entity.constants.Logic;
import com.ljf.risk.engine.core.component.load.AbstractComponent;
import com.ljf.risk.engine.service.ConditionGroupService;
import lombok.extern.slf4j.Slf4j;
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
public class ConditionGroupComponent extends AbstractComponent<Long, ConditionGroup> {

    private final ConditionGroupService conditionGroupService;

    private final ConditionComponent conditionComponent;

    public ConditionGroupComponent(ConditionGroupService conditionGroupService, ConditionComponent conditionComponent) {
        this.conditionGroupService = conditionGroupService;
        this.conditionComponent = conditionComponent;
    }

    @Override
    public String getComponentName() {
        return "条件组";
    }

    @Override
    public void load() {
        List<ConditionGroup> list = conditionGroupService.list();
        this.cache = list.stream().collect(Collectors.toConcurrentMap(ConditionGroup::getId, conditionGroup -> conditionGroup));
    }

    public Map<Long, List<ConditionGroup>> getCacheByRules(List<Rule> rules) {
        if (CollectionUtils.isEmpty(rules)) {
            return new ConcurrentHashMap<>();
        }
        List<Long> ruleIds = rules.stream().map(Rule::getId).collect(Collectors.toList());
        Collection<ConditionGroup> temp = cache.values();
        List<ConditionGroup> conditionGroups = temp.stream()
                .filter(conditionGroup -> ruleIds.contains(conditionGroup.getRelationId()) && Objects.equals(conditionGroup.getRelationType(), ConditionRelationType.RULE))
                .collect(Collectors.toList());
        return getConditionGroupsAndConditionByConditionGroups(conditionGroups);
    }

    public List<ConditionGroup> getCacheByRelationId(Long relationId, ConditionRelationType relationType) {
        if (relationId == null || relationType == null) {
            return new ArrayList<>();
        }
        List<ConditionGroup> conditionGroups = cache.values().stream()
                .filter(conditionGroup -> Objects.equals(relationId, conditionGroup.getRelationId()) && Objects.equals(conditionGroup.getRelationType(), relationType))
                .collect(Collectors.toList());
        Collection<Condition> temp = conditionComponent.getCache().values();
        for (ConditionGroup conditionGroup : conditionGroups) {
            List<Condition> conditions = temp.stream()
                    .filter(condition -> Objects.equals(conditionGroup.getId(), condition.getRelationId()) && Objects.equals(condition.getRelationType(), ConditionRelationType.CONDITION_GROUP))
                    .collect(Collectors.toList());
            conditionGroup.setConditionList(conditions);
        }
        return conditionGroups;
    }

    public Map<Long, List<ConditionGroup>> getConditionGroupsAndConditionByConditionGroups(List<ConditionGroup> conditionGroups) {
        if (CollectionUtils.isEmpty(conditionGroups)) {
            return new ConcurrentHashMap<>();
        }
        Collection<Condition> temp = conditionComponent.getCache().values();
        for (ConditionGroup conditionGroup : conditionGroups) {
            List<Condition> conditions = temp.stream()
                    .filter(condition -> Objects.equals(conditionGroup.getId(), condition.getRelationId()) && Objects.equals(condition.getRelationType(), ConditionRelationType.CONDITION_GROUP))
                    .collect(Collectors.toList());
            conditionGroup.setConditionList(conditions);
        }
        return conditionGroups.stream().collect(Collectors.groupingByConcurrent(ConditionGroup::getRelationId));
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public Boolean conditionGroupHandle(Long relationId, ConditionRelationType relationType, Logic logic, CurrentContext.Context context) {
        List<ConditionGroup> cacheByRelationId = getCacheByRelationId(relationId, relationType);
        Boolean hit = null;
        for (ConditionGroup conditionGroup : cacheByRelationId) {
            List<Condition> conditionList = conditionGroup.getConditionList();
            if (CollectionUtils.isEmpty(conditionList)) {
                continue;
            }
            for (Condition condition : conditionList) {
                hit = conditionComponent.analyse(condition, context);
                if (Logic.OR.equals(conditionGroup.getLogic()) && hit) {
                    return true;
                }
                if (Logic.AND.equals(conditionGroup.getLogic()) && !hit) {
                    return false;
                }
            }
            if (Logic.OR.equals(logic) && hit) {
                return true;
            }
            if (Logic.AND.equals(logic) && !hit) {
                return false;
            }

        }
        return hit;
    }
}
