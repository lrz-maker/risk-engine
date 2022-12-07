package com.ljf.risk.engine.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.Lists;
import com.ljf.risk.engine.entity.constants.Comparator;
import com.ljf.risk.engine.entity.constants.ConditionRelationType;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author lijinfeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_engine_condition")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Condition extends Model<Condition> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String code;

    private String description;

    private PropertyType leftType;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String leftProperty;

    private String leftValue;

    private PropertyType rightType;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String rightProperty;

    private String rightValue;

    private String createBy;

    private Comparator comparator;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    @Version
    private Integer version;

    private Long relationId;

    private ConditionRelationType relationType;

    @NoArgsConstructor
    @AllArgsConstructor
    public enum PropertyType {
        //
        CONSTANT(0, "常量"),
        CONTEXT_PROPERTY(1, "上下文属性"),
        FUNCTION(2, "函数"),
        INDICATOR(3, "指标");

        @EnumValue
        private int code;

        @JsonValue
        private String description;
    }

    public String getConditionDetail(Object leftValue, Object rightValue) {
        String left = StringUtils.joinWith(":", leftType.description, String.valueOf(leftProperty), String.valueOf(leftValue));
        String right = StringUtils.joinWith(":", rightType.description, String.valueOf(rightProperty), String.valueOf(rightValue));
        return StringUtils.join(Lists.newArrayList(left, comparator.getLogicOperator(), right), " ");
    }


}
