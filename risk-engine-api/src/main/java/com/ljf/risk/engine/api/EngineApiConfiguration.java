package com.ljf.risk.engine.api;

import com.alibaba.fastjson.JSONObject;
import feign.Logger;
import feign.Request;
import feign.codec.Decoder;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class EngineApiConfiguration {
    @Bean
    public Logger.Level level() {
        return Logger.Level.FULL;
    }

    @Bean
    public Request.Options options() {
        return new Request.Options(100, TimeUnit.MILLISECONDS, 500, TimeUnit.MILLISECONDS, true);
    }

    @Bean
    public Decoder decoder() {
        return (response, type) -> {
            byte[] bytes = IOUtils.toByteArray(response.body().asReader(StandardCharsets.UTF_8), StandardCharsets.UTF_8.name());
            EngineApi.EngineCheckRes riskEngineResponse = JSONObject.parseObject(bytes, EngineApi.EngineCheckRes.class);
            riskEngineResponse.setOriginal(Base64.getEncoder().encodeToString(bytes));
            return riskEngineResponse;
        };
    }
}
