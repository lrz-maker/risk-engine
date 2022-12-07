package com.ljf.risk.engine.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author lijinfeng
 */
@Component
public class SpringContextComponent implements ApplicationContextAware {
	private static ApplicationContext applicationContext = null;

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}

	public <T> T getBean(String beanName, Class<T> c) {
		return applicationContext.getBean(beanName, c);
	}

	public <T> T getBean(Class<T> c) {
		return applicationContext.getBean(c);
	}

	public Map getBeans(Class c) {
		return applicationContext.getBeansOfType(c);
	}

	public <T> ObjectProvider<T> getBeanProvider(Class<T> c) {
		return applicationContext.getBeanProvider(c);
	}

	public boolean containsBean(String beanName) {
		return applicationContext.containsBean(beanName);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextComponent.applicationContext = applicationContext;
	}
}