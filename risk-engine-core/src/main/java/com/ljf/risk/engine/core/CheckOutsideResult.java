package com.ljf.risk.engine.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author lijinfeng
 */
@ToString
@Getter
@AllArgsConstructor
public enum CheckOutsideResult {
    PASS(0, "通过"),
    REJECT(1001, "拒绝");

    private Integer code;

    private String msg;
}
