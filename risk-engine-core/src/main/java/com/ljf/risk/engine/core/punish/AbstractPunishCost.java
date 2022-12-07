package com.ljf.risk.engine.core.punish;

import com.ljf.risk.engine.core.constants.CurrentContext;
import com.ljf.risk.engine.entity.Punish;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lijinfeng
 */
@Slf4j
public abstract class AbstractPunishCost<T> implements CustomPunish {

    private long startTime;

    private void before() {
        log.info("{} start", this.getClass().getSimpleName());
        startTime = System.currentTimeMillis();
    }

    private void after() {
        log.info("{} end, cost: {}", this.getClass().getSimpleName(), System.currentTimeMillis() - startTime);
    }

    @Override
    public void punish(CurrentContext.Context context, Punish punish) {
        before();
        doService(context, punish);
        after();
    }


    protected abstract void doService(CurrentContext.Context context, Punish punish);

}
