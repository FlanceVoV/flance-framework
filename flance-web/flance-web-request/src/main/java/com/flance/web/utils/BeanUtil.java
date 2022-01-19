package com.flance.web.utils;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author jhf
 * beanUtils
 */
public class BeanUtil {


    /**
     *  动态注入单例bean实例
     * @param beanName bean名称
     * @param singletonObject 单例bean实例
     * @return 注入实例
     */
    public static <T> T registerSingletonBean(ConfigurableApplicationContext applicationContext,
                                              String beanName,
                                              Object singletonObject){

        //将applicationContext转换为ConfigurableApplicationContext
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;

        //获取BeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getAutowireCapableBeanFactory();

        //动态注册bean.
        defaultListableBeanFactory.registerSingleton(beanName, singletonObject);

        //获取动态注册的bean.
        return (T) configurableApplicationContext.getBean(beanName);
    }

    /**
     * 手动在Spring容器中注册bean
     */
    public static <T> T registerBean(ConfigurableApplicationContext applicationContext,
                                     String name,
                                     Class<T> clazz,
                                     String scope,
                                     Object... args) {

        if(applicationContext.containsBean(name)) {
            Object bean = applicationContext.getBean(name);
            if (bean.getClass().isAssignableFrom(clazz)) {
                return (T) bean;
            } else {
                throw new RuntimeException("BeanName 重复 " + name);
            }
        }

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        for (Object arg : args) {
            beanDefinitionBuilder.addConstructorArgValue(arg);
        }
        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        beanDefinition.setScope(scope);
        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
        beanFactory.registerBeanDefinition(name, beanDefinition);
        return applicationContext.getBean(name, clazz);
    }

}
