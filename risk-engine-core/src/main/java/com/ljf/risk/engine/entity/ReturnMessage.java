package com.ljf.risk.engine.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.util.Date;


/**
 * @author lijinfeng
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("t_engine_return_message")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReturnMessage extends Model<ReturnMessage> {
	private static final long serialVersionUID = 1L;

	@TableId(value = "id",type = IdType.AUTO)
	private Long id;

	private Integer code;

	private String description;

	private String returnMessage;

	private String createBy;

	private Date createTime;

	private String updateBy;

	private Date updateTime;

	@Version
	private Integer version;

}
