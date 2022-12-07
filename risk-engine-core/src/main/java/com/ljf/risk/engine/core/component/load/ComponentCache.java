package com.ljf.risk.engine.core.component.load;


import java.util.Map;

/**
 * 规则引擎组件缓存
 *
 * @author lijinfeng
 */
public interface ComponentCache<K, V> extends ComponentLoad {

    public Map<K, V> getCache();

}
