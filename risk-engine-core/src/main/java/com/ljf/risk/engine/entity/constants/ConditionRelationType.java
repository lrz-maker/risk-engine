package com.ljf.risk.engine.entity.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author lijinfeng
 */
@AllArgsConstructor
@NoArgsConstructor
public enum ConditionRelationType {

    RULE(0),
    FUNCTION(1),
    INDICATOR(2),
    CONDITION_GROUP(3);

    @EnumValue
    private int code;

}
