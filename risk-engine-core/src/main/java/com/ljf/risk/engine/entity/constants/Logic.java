package com.ljf.risk.engine.entity.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author lijinfeng
 */

@AllArgsConstructor
@NoArgsConstructor
public enum Logic {
    OR(0),
    AND(1);

    @EnumValue
    private int code;
}