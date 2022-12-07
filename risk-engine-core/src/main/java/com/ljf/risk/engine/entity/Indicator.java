package com.ljf.risk.engine.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ljf.risk.engine.entity.constants.Logic;
import lombok.*;

import java.util.Date;

/**
 * @author lijinfeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_engine_indicator", autoResultMap = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Indicator extends Model<Indicator> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String code;

    private String description;

    private Logic logic;

    private String period;

    @TableField(exist = false)
    private String analysisPeriod;

    private String factor;

    @TableField(typeHandler = ArrayTypeHandler.class)
    private String[] dimension;

    private IndicatorType type;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    @Version
    private Integer version;

    @AllArgsConstructor
    @NoArgsConstructor
    public enum IndicatorType {
        //
        SIMPLE_VALUE(0, "单值累计"),
        FACTOR_COUNT(1, "因子次数累计");

        @EnumValue
        private int code;

        @JsonValue
        private String description;

        public static boolean simpleValue(IndicatorType indicatorType) {
            return SIMPLE_VALUE.equals(indicatorType);
        }

        public static boolean factorCount(IndicatorType indicatorType) {
            return FACTOR_COUNT.equals(indicatorType);
        }

    }

}
