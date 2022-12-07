package com.ljf.risk.engine.core.indicator.redis;

import lombok.*;
import lombok.experimental.SuperBuilder;


/**
 * @author lijinfeng
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ZSetIndicator extends RedisIndicator {

    private double score;

    private String member;

}
