package com.ljf.risk.engine.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.ljf.risk.engine.entity.constants.CommonStatus;
import com.ljf.risk.engine.entity.constants.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * @author lijinfeng
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("t_engine_event")
public class Event extends Model<Event> {
	private static final long serialVersionUID = 1L;
	/**
	 * Id
	 */
	@TableId(value = "id",type = IdType.AUTO)
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
	 * Analysis
	 */
	private CommonStatus analysis;

	/**
	 * Accumulate
	 */
	private CommonStatus accumulate;


	/**
	 * Status
	 */
	private Status status;

	private String createBy;

	private Date createTime;

	private String updateBy;

	private Date updateTime;

	@Version
	private Integer version;

}
