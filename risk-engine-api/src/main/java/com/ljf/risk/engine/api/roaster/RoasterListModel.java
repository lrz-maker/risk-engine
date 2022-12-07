package com.ljf.risk.engine.api.roaster;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 名单
 */
@Getter
@Setter
public class RoasterListModel implements Serializable {
    private Long id;
    private String value;
    private String roasterName;
    private Long roasterId;
    private String roasterCode;
    private String remark;
    private Date createTime;
    private Date updateTime;
    private Date expiredAt;
    private String updateUser;
}
