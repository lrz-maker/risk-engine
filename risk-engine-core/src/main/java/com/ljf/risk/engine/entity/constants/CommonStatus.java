package com.ljf.risk.engine.entity.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author lijinfeng
 */
@AllArgsConstructor
@NoArgsConstructor
public enum CommonStatus {

    YES(1, "是"),
    NO(0, "否");

    @EnumValue
    private int code;

    @JsonValue
    private String description;

    public static boolean success(CommonStatus commonStatus) {
        return commonStatus.equals(CommonStatus.YES);
    }

}
