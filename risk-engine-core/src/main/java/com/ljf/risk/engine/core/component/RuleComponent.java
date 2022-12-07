package com.ljf.risk.engine.core.component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.ljf.risk.engine.api.EngineApi;
import com.ljf.risk.engine.api.PlayLoadResponse;
import com.ljf.risk.engine.api.rule.RuleAdmin;
import com.ljf.risk.engine.core.CheckOutsideResult;
import com.ljf.risk.engine.core.SpringContextComponent;
import com.ljf.risk.engine.core.component.load.AbstractComponent;
import com.ljf.risk.engine.core.constants.CurrentContext;
import com.ljf.risk.engine.entity.Condition;
import com.ljf.risk.engine.entity.ConditionGroup;
import com.ljf.risk.engine.entity.ReturnMessage;
import com.ljf.risk.engine.entity.Rule;
import com.ljf.risk.engine.entity.constants.ConditionRelationType;
import com.ljf.risk.engine.service.RuleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lijinfeng
 */
@Slf4j
@RestController
public class RuleComponent extends AbstractComponent<Long, Rule> implements RuleAdmin {

    private final RuleService ruleService;

    private final ConditionComponent conditionComponent;

    private final ConditionGroupComponent conditionGroupComponent;

    private final ReturnMessageComponent returnMessageComponent;

    private final FunctionExtentComponent functionExtentComponent;

    private final PunishComponent punishComponent;

    private final IndicatorComponent indicatorComponent;
    private final SpringContextComponent springContextComponent;

    public RuleComponent(RuleService ruleService, ConditionComponent conditionComponent, ConditionGroupComponent conditionGroupComponent, ReturnMessageComponent returnMessageComponent, FunctionExtentComponent functionExtentComponent, PunishComponent punishComponent, IndicatorComponent indicatorComponent, SpringContextComponent springContextComponent) {
        this.ruleService = ruleService;
        this.conditionComponent = conditionComponent;
        this.conditionGroupComponent = conditionGroupComponent;
        this.returnMessageComponent = returnMessageComponent;
        this.functionExtentComponent = functionExtentComponent;
        this.punishComponent = punishComponent;
        this.indicatorComponent = indicatorComponent;
        this.springContextComponent = springContextComponent;
    }

    @Override
    public String getComponentName() {
        return "规则";
    }

    @Override
    public void load() {
        List<Rule> list = ruleService.list(new LambdaQueryWrapper<Rule>().eq(Rule::getStatus, Rule.Status.ONLINE));
        this.cache = list.stream().collect(Collectors.toConcurrentMap(Rule::getId, rule -> rule));
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public EngineApi.EngineCheckRes analyse(List<Rule> rules) {
        beforeAnalyse(rules);
        List<Rule> hitRules = new ArrayList<>();
        List<String> hitRuleCodes = new ArrayList<>();
        for (Rule rule : rules) {
            Boolean hit = handleCondition(rule);
            if (hit) {
                hitRules.add(rule);
                hitRuleCodes.add(rule.getCode());
            }
        }
        EngineApi.EngineCheckRes checkRes = EngineApi.EngineCheckRes.success();
        EngineApi.CheckResult.CheckResultBuilder checkResultBuilder = EngineApi.CheckResult.builder().code(CheckOutsideResult.PASS.getCode()).msg(CheckOutsideResult.PASS.getMsg());
        EngineApi.CheckDetails.CheckDetailsBuilder checkDetailsBuilder = EngineApi.CheckDetails.builder();
        checkResultBuilder.eventCode(CurrentContext.currentCtx().getEventCode()).eventCodeDesc(CurrentContext.currentCtx().getEventCodeDesc());
        if (CollectionUtils.isNotEmpty(hitRules)) {
            CurrentContext.currentCtx().setHitRuleNames(hitRuleCodes);
            hitRules.stream().max(Comparator.comparingInt(Rule::getPriority)).ifPresent(rule -> {
                CurrentContext.currentCtx().setHitRule(rule);
            });
            Rule hitRule = CurrentContext.currentCtx().getHitRule();
            punishComponent.punish(hitRule.getId());
            checkDetailsBuilder.hitRules(hitRuleCodes).hitRule(hitRule.getCode());
            checkDetailsBuilder.conditionResult(CurrentContext.currentCtx().getConditionResult());
            if (Rule.Result.reject(hitRule.getResult())) {
                ReturnMessage returnMessage = returnMessageComponent.getCacheByReturnMessageId(hitRule.getReturnMessageId());
                checkResultBuilder.code(CheckOutsideResult.REJECT.getCode()).msg(CheckOutsideResult.REJECT.getMsg());
                if (returnMessage != null) {
                    checkDetailsBuilder.failedCode(returnMessage.getCode());
                    checkDetailsBuilder.failedMsg(returnMessage.getReturnMessage());
                }
                return checkRes.checkResult(checkResultBuilder.details(checkDetailsBuilder.build()).build());
            } else if (Rule.Result.pass(hitRule.getResult())) {
                checkResultBuilder.code(CheckOutsideResult.PASS.getCode()).msg(CheckOutsideResult.PASS.getMsg());
                return checkRes.checkResult(checkResultBuilder.details(checkDetailsBuilder.build()).build());
            }
        }
        return checkRes.checkResult(checkResultBuilder.details(checkDetailsBuilder.build()).build());
    }

    private void beforeAnalyse(List<Rule> rules) {
        Map<Long, List<Condition>> conditionCacheByRules = conditionComponent.getCacheByRules(rules);
        Map<Long, List<ConditionGroup>> conditionGroupCacheByRules = conditionGroupComponent.getCacheByRules(rules);
        functionExtentComponent.calculateFunction(conditionCacheByRules, conditionGroupCacheByRules);
        indicatorComponent.calculateIndicator(conditionCacheByRules, conditionGroupCacheByRules);
    }

    private Boolean handleCondition(Rule rule) {
        CurrentContext.currentCtx().setConditionResult(new HashMap<>());
        Boolean conditionHit = conditionComponent.conditionHandle(rule.getId(), ConditionRelationType.RULE, rule.getLogic(), CurrentContext.currentCtx());
        Boolean conditionGroupHit = conditionGroupComponent.conditionGroupHandle(rule.getId(), ConditionRelationType.RULE, rule.getLogic(), CurrentContext.currentCtx());
        return conditionComponent.getConditionResult(conditionHit, conditionGroupHit, rule.getLogic(), ConditionRelationType.RULE);
    }

    @Override
    @PostMapping("test-rule")
    public PlayLoadResponse<TestResponse> testRule(@RequestBody TestRuleReq testRuleReq) {
        try {
            if (Objects.isNull(testRuleReq.getRuleId())) {
                return PlayLoadResponse.failurePlayLoad("", "参数必填");
            }
            Rule rule = ruleService.getOne(new LambdaQueryWrapper<Rule>().eq(Rule::getId, testRuleReq.getRuleId()));
            if (rule == null) {
                return PlayLoadResponse.failurePlayLoad("", "无相关规则");
            }
            InitComponent bean = springContextComponent.getBean(InitComponent.class);
            boolean load = bean.load();
            if (!load) {
                return PlayLoadResponse.failurePlayLoad("", "加载规则失败");
            }
            EngineApi.EngineCheckReq build = EngineApi.EngineCheckReq.builder().eventCode(null).eventDetails(testRuleReq.getAttributes()).bizId(UUID.randomUUID().toString()).build();
            try (CurrentContext ignored = new CurrentContext(CurrentContext.Context.builder().eventTime(new Date()).engineCheckReq(build).build())) {
                EngineApi.EngineCheckRes analyse = analyse(Lists.newArrayList(rule));
                return PlayLoadResponse.success(TestResponse.builder()
                        .ruleResult(analyse.getCheckResult().getMsg())
                        .functionResult(CurrentContext.currentCtx().getFunctionResult())
                        .indicatorResult(CurrentContext.currentCtx().getIndicatorResult())
                        .conditionResult(CurrentContext.currentCtx().getConditionResult())
                        .build());
            }
        } catch (Exception e) {
            log.error("", e);
            return PlayLoadResponse.failurePlayLoad("", "测试失败");
        }
    }
}
