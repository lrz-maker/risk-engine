package com.ljf.risk.engine.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

/**
 * @author lijinfeng
 */
@EqualsAndHashCode(callSuper = true, exclude = {"id"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("t_engine_event_rule_relation")
public class EventRuleRelation extends Model<EventRuleRelation> {
	private static final long serialVersionUID = 1L;
	/**
	 * Id
	 */
	@TableId(value = "id",type = IdType.AUTO)
	private Long id;

	private Long eventId;

	private Long ruleId;

}
