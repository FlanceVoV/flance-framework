package com.flance.web.utils.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Slf4j
@Component("startLoaderAware")
public class StartLoaderAware implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (!applicationContext.containsBean("startLoaderBefore")) {
            log.warn("未找到[startLoaderBefore]即将使用默认的");
            DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
            beanFactory.registerSingleton("startLoaderBefore", new StartLoaderBefore());
        }
    }
}
