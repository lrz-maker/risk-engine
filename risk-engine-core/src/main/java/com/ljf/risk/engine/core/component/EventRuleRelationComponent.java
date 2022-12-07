package com.ljf.risk.engine.core.component;

import com.ljf.risk.engine.core.component.load.AbstractComponent;
import com.ljf.risk.engine.entity.EventRuleRelation;
import com.ljf.risk.engine.entity.Rule;
import com.ljf.risk.engine.service.EventRuleRelationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author lijinfeng
 */
@Slf4j
@Component
public class EventRuleRelationComponent extends AbstractComponent<Long, List<Rule>> {

    private final EventRuleRelationService eventRuleRelationService;

    private final RuleComponent ruleComponent;

    public EventRuleRelationComponent(EventRuleRelationService eventRuleRelationService, RuleComponent ruleComponent) {
        this.eventRuleRelationService = eventRuleRelationService;
        this.ruleComponent = ruleComponent;
    }

    @Override
    public String getComponentName() {
        return "事件和规则关系";
    }

    @Override
    public void load() {
        List<EventRuleRelation> list = eventRuleRelationService.list();
        if (!CollectionUtils.isEmpty(list)) {
            Map<Long, List<Rule>> temp = new ConcurrentHashMap<>();
            Map<Long, Rule> ruleComponentCache = ruleComponent.getCache();
            if (MapUtils.isEmpty(ruleComponentCache)) {
                this.cache = new ConcurrentHashMap<>();
                return;
            }
            Map<Long, List<EventRuleRelation>> group = list.stream().collect(Collectors.groupingBy(EventRuleRelation::getEventId));
            for (Map.Entry<Long, List<EventRuleRelation>> longListEntry : group.entrySet()) {
                List<Rule> rules = new ArrayList<>();
                for (EventRuleRelation eventRuleRelation : longListEntry.getValue()) {
                    Rule rule = ruleComponentCache.get(eventRuleRelation.getRuleId());
                    if (rule != null) {
                        rules.add(rule);
                    }
                }
                temp.put(longListEntry.getKey(), rules);
            }
            this.cache = temp;
        }
    }

    public List<Rule> getCacheByEventId(Long eventId) {
        return cache.get(eventId);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
