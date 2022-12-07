package com.ljf.risk.engine.core.fuction;

import com.ljf.risk.engine.core.component.ConditionComponent;
import com.ljf.risk.engine.core.component.ConditionGroupComponent;
import com.ljf.risk.engine.core.constants.CurrentContext;
import com.ljf.risk.engine.entity.constants.ConditionRelationType;
import com.ljf.risk.engine.entity.FunctionExtend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author lijinfeng
 */
@Slf4j
public abstract class AbstractFunction implements CustomFunction {

    @Autowired
    private ConditionComponent conditionComponent;

    @Autowired
    private ConditionGroupComponent conditionGroupComponent;

    @Override
    public Object execute(CurrentContext.Context context, FunctionExtend functionExtend) {
        log.info("{} start", this.getClass().getSimpleName());
        long startTime = System.currentTimeMillis();
        Object result = null;
        Boolean precondition = precondition(functionExtend, context);
        if (precondition) {
            result = doService(context.getEngineCheckReq().getEventDetails(), functionExtend.getParams());
        }
        log.info("{} end, precondition: {}, result: {}, cost: {}", this.getClass().getSimpleName(), precondition, result, System.currentTimeMillis() - startTime);
        return result;
    }

    public Boolean precondition(FunctionExtend functionExtend, CurrentContext.Context context) {
        Boolean conditionHit = conditionComponent.conditionHandle(functionExtend.getId(), ConditionRelationType.FUNCTION, functionExtend.getLogic(), context);
        Boolean conditionGroupHit = conditionGroupComponent.conditionGroupHandle(functionExtend.getId(), ConditionRelationType.FUNCTION, functionExtend.getLogic(), context);
        return conditionComponent.getConditionResult(conditionHit, conditionGroupHit, functionExtend.getLogic(), ConditionRelationType.FUNCTION);
    }

    public abstract Object doService(Map<String, Object> context, String params);

}
