package com.ljf.risk.engine.core.component.load;


import org.springframework.core.Ordered;

/**
 * 规则引擎组件加载器
 * @author lijinfeng
 */
public interface ComponentLoad extends Ordered {

    public void load();

}
