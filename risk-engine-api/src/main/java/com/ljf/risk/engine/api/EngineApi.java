package com.ljf.risk.engine.api;

import lombok.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

/**
 * @author lijinfeng
 */
@FeignClient(contextId = "engine.api", value = "risk-engine")
public interface EngineApi {
    /**
     * 实时检测
     */
    @PostMapping("check")
    EngineCheckRes check(EngineCheckReq checkReq);

    /**
     * 实时检测回调
     */
    @PostMapping("check-callback")
    EngineCheckRes checkCallback();


    @GetMapping("load")
    EngineCheckRes load();

    @Data
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class EngineCheckReq {
        private String eventCode;
        private String bizId;
        private Map<String, Object> eventDetails;
    }

    @Data
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class EngineCheckRes {
        private Integer code;
        private String msg;
        private CheckResult checkResult;
        private String original;

        public static EngineCheckRes failed(Result result) {
            return EngineCheckRes.builder().code(result.getCode()).msg(result.getMsg()).build();
        }

        public static EngineCheckRes success() {
            return EngineCheckRes.builder().code(Result.SUCCESS.getCode()).msg(Result.SUCCESS.getMsg()).build();
        }

        public static EngineCheckRes success(CheckResult checkResult) {
            return EngineCheckRes.builder().code(Result.SUCCESS.getCode()).msg(Result.SUCCESS.getMsg()).build().checkResult(checkResult);
        }

        public EngineCheckRes checkResult(CheckResult checkResult) {
            setCheckResult(checkResult);
            return this;
        }


        public boolean isSuccess() {
            return code.equals(Result.SUCCESS.getCode());
        }

    }

    @ToString
    @Getter
    @AllArgsConstructor
    enum Result {
        SUCCESS(200, "成功"),
        ERROR(500, "内部错误");

        private Integer code;

        private String msg;

    }


    @Data
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class CheckResult {
        private String eventCode;
        private String eventCodeDesc;
        private Integer code;
        private String msg;
        private CheckDetails details;

        public boolean isReject() {
            return code.equals(1001);
        }
    }

    @Data
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class CheckDetails {
        private Integer failedCode;
        private String failedMsg;
        private List<String> hitRules;
        private String hitRule;
        private Map<String, Object> conditionResult;
    }

    class CheckCallbackReq {
        private String eventCode;
        private Map<String, Object> eventDetails;
    }
}