package com.ljf.risk.engine.core.task;

import com.ljf.risk.engine.core.constants.CurrentContext;
import com.ljf.risk.engine.core.fuction.CustomFunction;
import com.ljf.risk.engine.entity.FunctionExtend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lijinfeng
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Slf4j
public class FunctionTask implements Task {

    private CurrentContext.Context context;

    private CustomFunction function;

    private FunctionExtend functionExtend;

    @Override
    public Object doTask() {
        return function.execute(context, functionExtend);
    }
}
