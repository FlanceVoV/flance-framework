package com.flance.tx.datasource.annotation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 注册bean的 后置处理器
 * Bean实例化完毕后且依赖注入完成后触发
 * @author jhf
 */
@Slf4j
public class FlanceDataSourceBeanPostProcessor implements BeanPostProcessor {

    /**
     * 实例化、依赖注入完毕，
     * 在调用显示的初始化之前完成一些定制的初始化任务
     * @param bean              bean实例
     * @param beanName          beanName
     * @return                  返回bean
     * @throws BeansException   异常
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        log.info("在调用显示的初始化之前完成一些定制的初始化任务-{}", beanName);
        return bean;
    }

    /**
     * 实例化、依赖注入、初始化完毕时执行
     * @param bean              bean实例
     * @param beanName          beanName
     * @return                  返回bean
     * @throws BeansException   异常
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.info("实例化、依赖注入、初始化完毕时执行-{}", beanName);
        return bean;
    }
}
