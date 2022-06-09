package com.flance.tx.datasource.proxy.plugins;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.support.DefaultIntroductionAdvisor;
import org.springframework.beans.BeansException;

@Slf4j
public class FlanceMybatisPluginCreator extends AbstractAutoProxyCreator {


    private final Advisor advisor;

    public FlanceMybatisPluginCreator() {
        advisor = new DefaultIntroductionAdvisor(new CTMybatisPluginProxyAdvice());
    }

    /**
     * 跳过代理创建
     * 只接受mybatis plugins Interceptor
     */
    @Override
    protected boolean shouldSkip(Class<?> beanClass, String beanName) {
        return !Interceptor.class.isAssignableFrom(beanClass) ||
                FlanceMybatisPlugins.class.isAssignableFrom(beanClass);
    }

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> aClass, String s, TargetSource targetSource) throws BeansException {
        return new Object[]{advisor};
    }
}
