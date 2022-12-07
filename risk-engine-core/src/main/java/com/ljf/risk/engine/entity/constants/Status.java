package com.ljf.risk.engine.entity.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum Status {
    ONLINE(1, "上线"),
    OFFLINE(0, "下线");

    @EnumValue
    private int code;

    @JsonValue
    private String description;

}