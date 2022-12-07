package com.ljf.risk.engine.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@TableName("t_engine_roaster_list")
@Getter
@Setter
public class RoasterList implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    private String value;
    //名单id
    private Long roasterId;
    private Date expiredAt;
    private Date createTime;
    private Date updateTime;
    private String remark;
    private String updateUser;
}
