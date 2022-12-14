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
    PASS(0, "ιθΏ"),
    REJECT(1001, "ζη»");

    private Integer code;

    private String msg;
}
