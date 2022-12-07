package com.ljf.risk.engine.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

/**
 * @author lijinfeng
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName(value = "t_engine_punish", autoResultMap = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Punish extends Model<Punish> {
    private static final long serialVersionUID = 1L;
    /**
     * Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private PunishType punishCode;

    @TableField(typeHandler = ArrayTypeHandler.class)
    private String[] punishField;

    private String punishPeriod;

    private Long ruleId;

    private Long roasterId;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public enum PunishType {
        COMMON_LIST_PUNISH("通用加名单");

        private String description;
    }


}
