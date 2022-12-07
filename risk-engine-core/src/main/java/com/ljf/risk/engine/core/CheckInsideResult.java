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
public enum CheckInsideResult {
    NO_EVENT(-100, "未配置事件"),
    EVENT_OFFLINE(-101, "事件未开启分析和累计"),
    NO_RULE(-200, "事件下未配置规则"),
    ERROR(-500, "内部错误");

    private Integer code;

    private String msg;
}
