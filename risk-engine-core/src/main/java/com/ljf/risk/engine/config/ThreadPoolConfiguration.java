package com.ljf.risk.engine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolConfiguration {
    private int asyncCorePoolSize = 16;

    private int asyncMaxPoolSize = 64;

    private int asyncKeepAliveSeconds = 60;

    private int asyncQueueCapacity = 1000;

    private int accumulateCorePoolSize = 8;

    private int accumulateMaxPoolSize = 32;

    private int accumulateKeepAliveSeconds = 60;

    private int accumulateQueueCapacity = 1000;

    @Bean(name="asyncThreadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setThreadNamePrefix("async-task-");
        pool.setCorePoolSize(asyncCorePoolSize);
        pool.setMaxPoolSize(asyncMaxPoolSize);
        pool.setKeepAliveSeconds(asyncKeepAliveSeconds);
        pool.setQueueCapacity(asyncQueueCapacity);
        pool.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy());
        return pool;
    }

    @Bean(name="accumulateThreadPoolTaskExecutor")
    public ThreadPoolTaskExecutor accumulateThreadPoolTaskExecutor(){
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setThreadNamePrefix("accumulate-task-");
        pool.setCorePoolSize(accumulateCorePoolSize);
        pool.setMaxPoolSize(accumulateMaxPoolSize);
        pool.setKeepAliveSeconds(accumulateKeepAliveSeconds);
        pool.setQueueCapacity(accumulateQueueCapacity);
        pool.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy());
        return pool;
    }


}
