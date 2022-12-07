package com.ljf.risk.engine.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.ljf.risk.engine.entity.constants.ConditionRelationType;
import com.ljf.risk.engine.entity.constants.Logic;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * @author lijinfeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_engine_condition_group")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConditionGroup extends Model<ConditionGroup> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String code;

    private String description;

    private Logic logic;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    @Version
    private Integer version;

    private Long relationId;

    private ConditionRelationType relationType;

    @TableField(exist = false)
    private List<Condition> conditionList;

}
