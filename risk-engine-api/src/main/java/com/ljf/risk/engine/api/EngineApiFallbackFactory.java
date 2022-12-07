package com.ljf.risk.engine.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * 兜底通过
 *
 * @author lijinfeng
 */
@Slf4j
//@Component
public class EngineApiFallbackFactory implements FallbackFactory<EngineApi> {

    @Override
    public EngineApi create(Throwable cause) {
        return new EngineApi() {
            @Override
            public EngineCheckRes check(EngineCheckReq checkReq) {
                log.error("engine check fallback, param: {}, ", checkReq, cause);
                return EngineCheckRes.success();
            }

            @Override
            public EngineCheckRes checkCallback() {
                log.error("engine checkCallback fallback, ", cause);
                return EngineCheckRes.success();
            }

            @Override
            public EngineCheckRes load() {
                log.error("engine load, ", cause);
                return EngineCheckRes.success();
            }
        };

    }
}
