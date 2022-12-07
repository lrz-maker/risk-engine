package com.ljf.risk.engine.executor;

import com.ljf.risk.engine.core.task.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * @author lijinfeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParallelWorker implements Callable<Object> {


    /**
     * 并行任务
     */
    private Task task;

    /**
     * 线程协作组件
     */
    private CountDownLatch latch;


    @Override
    public Object call() {
        try {
            return task.doTask();
        } finally {
            //分析进度通知
            this.latch.countDown();
        }
    }

}
