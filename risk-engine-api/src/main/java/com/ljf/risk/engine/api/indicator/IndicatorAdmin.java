package com.ljf.risk.engine.api.indicator;

import com.ljf.risk.engine.api.PlayLoadResponse;
import lombok.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(contextId = "engine.indicator", value = "risk-engine")
public interface IndicatorAdmin {

    @PostMapping("test-indicator")
    PlayLoadResponse<TestResponse> testIndicator(@RequestBody TestReq testReq);

    @ToString
    @Data
    class TestReq {
        private Long indicatorId;
        private String period;
        private Map<String, Object> attributes;
    }

    @ToString
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    class TestResponse {
        private String result;
    }

}
