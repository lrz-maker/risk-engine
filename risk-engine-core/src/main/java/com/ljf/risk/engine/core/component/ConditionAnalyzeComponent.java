package com.ljf.risk.engine.core.component;

import com.ljf.risk.engine.core.constants.CurrentContext;
import com.ljf.risk.engine.entity.Condition;
import com.ljf.risk.engine.entity.constants.Comparator;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;


/**
 * @author lijinfeng
 */
@Component
public class ConditionAnalyzeComponent {

    private final ConversionService conversionService;

    public ConditionAnalyzeComponent(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public Object getConditionValue(Condition.PropertyType propertyType, String property, String value, Comparator comparator, CurrentContext.Context context) {
        if (Condition.PropertyType.CONSTANT.equals(propertyType)) {
            return Comparator.convert(comparator, value, conversionService);
        }
        if (Condition.PropertyType.CONTEXT_PROPERTY.equals(propertyType)) {
            return Comparator.convert(comparator, MapUtils.getObject(context.getEngineCheckReq().getEventDetails(), property), conversionService);
        }
        if (Condition.PropertyType.FUNCTION.equals(propertyType)) {
            return Comparator.convert(comparator, MapUtils.getObject(context.getFunctionResult(), property), conversionService);
        }
        if (Condition.PropertyType.INDICATOR.equals(propertyType)) {
            String indicatorKey = StringUtils.isNotBlank(value) ? property + "." +  value : property;
            return Comparator.convert(comparator, MapUtils.getObject(context.getIndicatorResult(), indicatorKey), conversionService);
        }
        return null;
    }

}
