package com.ljf.risk.engine.core.component;

import com.ljf.risk.engine.entity.Indicator;
import com.ljf.risk.engine.core.component.load.AbstractComponent;
import com.ljf.risk.engine.entity.EventIndicatorRelation;
import com.ljf.risk.engine.service.EventIndicatorRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author lijinfeng
 */
@Slf4j
@Component
public class EventIndicatorRelationComponent extends AbstractComponent<Long, List<Indicator>> {

    private final EventIndicatorRelationService eventIndicatorRelationService;

    private final IndicatorComponent indicatorComponent;

    public EventIndicatorRelationComponent(EventIndicatorRelationService eventIndicatorRelationService, IndicatorComponent indicatorComponent) {
        this.eventIndicatorRelationService = eventIndicatorRelationService;
        this.indicatorComponent = indicatorComponent;
    }

    @Override
    public String getComponentName() {
        return "事件和指标关系";
    }

    @Override
    public void load() {
        List<EventIndicatorRelation> list = eventIndicatorRelationService.list();
        if (!CollectionUtils.isEmpty(list)) {
            Map<Long, List<Indicator>> temp = new ConcurrentHashMap<>();
            Map<Long, Indicator> indicatorComponentCache = indicatorComponent.getCache();
            Map<Long, List<EventIndicatorRelation>> group = list.stream().collect(Collectors.groupingBy(EventIndicatorRelation::getEventId));
            for (Map.Entry<Long, List<EventIndicatorRelation>> longListEntry : group.entrySet()) {
                List<Indicator> indicators = new ArrayList<>();
                for (EventIndicatorRelation eventIndicatorRelation : longListEntry.getValue()) {
                    Indicator indicator = indicatorComponentCache.get(eventIndicatorRelation.getIndicatorId());
                    if (indicator != null) {
                        indicators.add(indicator);
                    }
                }
                temp.put(longListEntry.getKey(), indicators);
            }
            this.cache = temp;
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }

    public List<Indicator> getCacheByEventId(Long eventId) {
        return Optional.ofNullable(cache.get(eventId)).orElseGet(ArrayList::new);
    }
}
