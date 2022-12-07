package com.ljf.risk.engine.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.ljf.risk.engine.entity.constants.Logic;
import lombok.*;

import java.util.Date;

/**
 * @author lijinfeng
 */
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"id"})
@TableName("t_engine_function_extend")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FunctionExtend extends Model<FunctionExtend> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String code;

    private String description;

    private Logic logic;

    private String functionCode;

    private String params;

    @Version
    private Long version;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;


}
