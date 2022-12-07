package com.ljf.risk.engine.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.util.Date;


/**
 * @author lijinfeng
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("t_engine_rule_group")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleGroup extends Model<RuleGroup> {
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
	 * Status
	 */
	private Status status;

	private String createBy;

	private Date createTime;

	private String updateBy;

	private Date updateTime;

	@Version
	private Integer version;

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

}
