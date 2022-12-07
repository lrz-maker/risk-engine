package com.ljf.risk.engine.executor;

import com.ljf.risk.engine.core.task.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author lijinfeng
 */
@Slf4j
public class ParallelExecutor {

    /**
     * 并行执行
     *
     * @param taskMap
     * @param executor
     * @param timeout
     * @return
     */
    public static Map<String, Future<Object>> doExe(Map<String, Task> taskMap, ThreadPoolTaskExecutor executor, int timeout) {
        CountDownLatch latch = new CountDownLatch(taskMap.size());
        Map<String, Future<Object>> futureMap = new HashMap<>();
        for (Map.Entry<String, Task> taskEntry : taskMap.entrySet()) {
            try {
                futureMap.put(taskEntry.getKey(), executor.submit(new ParallelWorker(taskEntry.getValue(), latch)));
            } catch (Exception e) {
                // 这里一般为队列满了，走默认系统异常流程
                // 计数器需要减少
                latch.countDown();
            }
        }

        try {
            boolean await = latch.await(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("ParallelExecutor failed: ", e);
        }
        return futureMap;
    }
}
