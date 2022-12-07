package com.ljf.risk.engine.api.roaster;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 名单分类
 */
@Getter
@Setter
public class RoasterModel implements Serializable {
    private Long id;
    private String code;
    private String name;
    /**
     * 创建时间
     */
    private Date createTime;
    private String remark;
    private Date updateTime;
    private String updateUser;
}
