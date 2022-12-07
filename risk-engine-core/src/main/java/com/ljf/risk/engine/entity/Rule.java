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
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("t_engine_rule")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rule extends Model<Rule> {
    private static final long serialVersionUID = 1L;
    /**
     * Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Name
     */
    private String code;

    /**
     * Description
     */
    private String description;

    /**
     * Status
     */
    private Status status;

    private Logic logic;

    private Integer priority;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    @Version
    private Integer version;

    private Boolean test;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long returnMessageId;

    private Result result;

    private Long ruleGroupId;

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

    @AllArgsConstructor
    @NoArgsConstructor
    public enum Result {
        REJECT(1001, "拒绝"),
        PASS(0, "通过");

        @EnumValue
        private int code;

        @JsonValue
        private String description;

        public static boolean pass(Result result) {
            return PASS.equals(result);
        }

        public static boolean reject(Result result) {
            return REJECT.equals(result);
        }
    }

}
