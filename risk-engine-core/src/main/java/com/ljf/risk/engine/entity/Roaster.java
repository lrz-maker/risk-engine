package com.ljf.risk.engine.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@TableName("t_engine_roaster")
@Getter
@Setter
public class Roaster implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String code;
    private String name;
    private Date createTime;
    private Date updateTime;
    private String updateUser;
    private String remark;
}
