package com.ljf.risk.engine.api.rule;

import com.ljf.risk.engine.api.PlayLoadResponse;
import lombok.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

/**
 * 规则状态机器：
 * 初始状态 test
 * test->down,test[至少经过一次成功的测试用例]->up
 * up->test,up->down
 * down->test
 *
 * @author weiweihan
 */
@FeignClient(contextId = "engine.rule", value = "risk-engine")
public interface RuleAdmin {

    @PostMapping("test-rule")
    PlayLoadResponse<TestResponse> testRule(TestRuleReq testRuleReq);

    @ToString
    @Data
    class TestRuleReq {
        private Long ruleId;
        private Map<String, Object> attributes;
    }

    @ToString
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    class TestResponse {
        private String ruleResult;
        private Map<String, Object> functionResult;
        private Map<String, Object> conditionResult;
        private Map<String, Object> indicatorResult;
    }

}
