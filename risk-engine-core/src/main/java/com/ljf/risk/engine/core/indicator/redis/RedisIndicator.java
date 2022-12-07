package com.ljf.risk.engine.core.indicator.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


/**
 * @author lijinfeng
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RedisIndicator {

    private String indicatorResultCacheKey;

    private String key;

    private Long expire;

}
