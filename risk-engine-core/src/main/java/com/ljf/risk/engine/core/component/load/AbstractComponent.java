package com.ljf.risk.engine.core.component.load;

import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lijinfeng
 */
public abstract class AbstractComponent<K, V> implements ComponentCache<K, V> {

    private long startTime;

    protected volatile Map<K, V> cache = new ConcurrentHashMap<>();

    /**
     * 获取子组件名称
     *
     * @return
     */
    public abstract String getComponentName();

    public void beforeLoad() {
        startTime = System.currentTimeMillis();
        LoggerFactory.getLogger(this.getClass()).info("ComponentLoad, 加载{}开始", getComponentName());
    }

    public void afterLoad() {
        LoggerFactory.getLogger(this.getClass()).info("ComponentLoad, 加载{}结束, size: {}, cost: {}ms", getComponentName(), getCache().size(), System.currentTimeMillis() - startTime);
    }

    @Override
    public Map<K, V> getCache() {
        return cache;
    }

}
