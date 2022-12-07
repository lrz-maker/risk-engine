package com.ljf.risk.engine.core.component;

import com.ljf.risk.engine.core.constants.CurrentContext;
import com.ljf.risk.engine.core.component.load.AbstractComponent;
import com.ljf.risk.engine.core.task.FunctionTask;
import com.ljf.risk.engine.core.task.Task;
import com.ljf.risk.engine.entity.Condition;
import com.ljf.risk.engine.entity.ConditionGroup;
import com.ljf.risk.engine.entity.FunctionExtend;
import com.ljf.risk.engine.executor.ParallelExecutor;
import com.ljf.risk.engine.service.FunctionExtentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author lijinfeng
 */
@Slf4j
@Component
public class FunctionExtentComponent extends AbstractComponent<Long, FunctionExtend> {

    private final FunctionExtentService functionExtentService;

    private final FunctionComponent functionComponent;

    private final ThreadPoolTaskExecutor asyncThreadPoolTaskExecutor;

    public FunctionExtentComponent(FunctionExtentService functionExtentService, FunctionComponent functionComponent, ThreadPoolTaskExecutor asyncThreadPoolTaskExecutor) {
        this.functionExtentService = functionExtentService;
        this.functionComponent = functionComponent;
        this.asyncThreadPoolTaskExecutor = asyncThreadPoolTaskExecutor;
    }

    @Override
    public String getComponentName() {
        return "函数";
    }

    @Override
    public void load() {
        List<FunctionExtend> list = functionExtentService.list();
        this.cache = list.stream().collect(Collectors.toConcurrentMap(FunctionExtend::getId, functionExtend -> functionExtend));
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public void calculateFunction(Map<Long, List<Condition>> conditionCacheByRules, Map<Long, List<ConditionGroup>> conditionGroupCacheByRules) {
        log.info("calculate function start");
        long startTime = System.currentTimeMillis();
        Set<FunctionExtend> cacheByConditions = getCacheByConditions(conditionCacheByRules, conditionGroupCacheByRules);
        CurrentContext.Context context = CurrentContext.currentCtx();
        Map<String, Task> taskMap = new HashMap<>();
        for (FunctionExtend functionExtend : cacheByConditions) {
            FunctionTask task = FunctionTask.builder()
                    .context(context)
                    .function(functionComponent.getCacheByFunctionCode(functionExtend.getFunctionCode()))
                    .functionExtend(functionExtend)
                    .build();
            taskMap.put(functionExtend.getId().toString(), task);
        }

        // 组装结果
        Map<String, Future<Object>> futureMap = ParallelExecutor.doExe(taskMap, asyncThreadPoolTaskExecutor, 100);
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Future<Object>> futureEntry : futureMap.entrySet()) {
            try {
                Future<Object> future = futureEntry.getValue();
                if (future.isDone()) {
                    Object value = future.get(0, TimeUnit.MILLISECONDS);
                    result.put(futureEntry.getKey(), value);
                } else {
                    result.put(futureEntry.getKey(), null);
                    future.cancel(true);
                    log.error("function {} run timeout: ", cache.get(Long.parseLong(futureEntry.getKey())).getCode());
                }
            } catch (Exception e) {
                log.error("function {} run failed: ", cache.get(Long.parseLong(futureEntry.getKey())).getCode(), e);
            }
        }
        context.setFunctionResult(result);
        log.info("calculate function end, cost: {}ms", System.currentTimeMillis() - startTime);
    }

    public Set<FunctionExtend> getCacheByConditions(Map<Long, List<Condition>> conditionCacheByRules, Map<Long, List<ConditionGroup>> conditionGroupCacheByRules) {
        Set<FunctionExtend> functions = new HashSet<>();
        for (List<Condition> value : conditionCacheByRules.values()) {
            for (Condition condition : value) {
                if (Objects.equals(condition.getLeftType(), Condition.PropertyType.FUNCTION)) {
                    functions.add(cache.get(Long.parseLong(condition.getLeftProperty())));
                }
                if (Objects.equals(condition.getRightType(), Condition.PropertyType.FUNCTION)) {
                    functions.add(cache.get(Long.parseLong(condition.getRightProperty())));
                }
            }
        }
        Collection<List<ConditionGroup>> values = conditionGroupCacheByRules.values();
        for (List<ConditionGroup> value : values) {
            for (ConditionGroup conditionGroup : value) {
                for (Condition condition : conditionGroup.getConditionList()) {
                    if (Objects.equals(condition.getLeftType(), Condition.PropertyType.FUNCTION)) {
                        functions.add(cache.get(Long.parseLong(condition.getLeftProperty())));
                    }
                    if (Objects.equals(condition.getRightType(), Condition.PropertyType.FUNCTION)) {
                        functions.add(cache.get(Long.parseLong(condition.getRightProperty())));
                    }
                }
            }
        }
        return functions;
    }


}
