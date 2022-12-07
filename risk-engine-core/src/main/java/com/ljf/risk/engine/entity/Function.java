package com.ljf.risk.engine.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * @author lijinfeng
 */
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"id"})
@TableName("t_engine_function")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Function extends Model<Function> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String code;

    private String description;

    private ReturnType returnType;

    @Version
    private Long version;

    @AllArgsConstructor
    @NoArgsConstructor
    public enum ReturnType {
        STRING(0, "字符串", String.class),
        NUMBER(1, "数值", Number.class),
        BOOLEAN(2, "布尔", Boolean.class),
        COLLECTION(3, "集合", Collection.class),
        MAP(4, "散列表", Map.class),
        OBJECT(5, "任意对象", Objects.class);

        @EnumValue
        private int code;

        @JsonValue
        private String description;

        private Class classType;
    }


}
