package com.ljf.risk.engine.entity.constants;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.core.convert.ConversionService;

import java.math.BigDecimal;

/**
 * @author lijinfeng
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum Comparator {
    // 比较符
    EQ("等于", "=", String.class),
    GT("大于", ">", BigDecimal.class),
    GR("大于等于", ">=", BigDecimal.class),
    LT("小于", "<", BigDecimal.class),
    LE("小于等于", "=<", BigDecimal.class),
    NE("不等于", "!=", String.class),
    CONTAINS("包含", "in", Object.class);

    @JsonValue
    private String description;

    private String logicOperator;

    private Class<?> c;

    /**
     * 根据类型转换数值
     *
     * @param comparator
     * @param value
     * @param defaultConversionService
     * @return
     */
    public static Object convert(Comparator comparator, Object value, ConversionService defaultConversionService) {
        if (comparator.getC() == Object.class) {
            return value;
        }
        return defaultConversionService.convert(value, comparator.getC());
    }
}
