package com.flance.tx.netty.biz;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.BeansException;

public class BizHandlerProxyCreator extends AbstractAutoProxyCreator {

    private final DefaultPointcutAdvisor advisor;

    public BizHandlerProxyCreator() {
        advisor = new DefaultPointcutAdvisor();
        advisor.setAdvice(new BizHandlerProxyAdvice());
    }

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> aClass, String s, TargetSource targetSource) throws BeansException {
        return new Object[]{advisor};
    }

    /**
     * 跳过代理创建
     */
    @Override
    protected boolean shouldSkip(Class<?> beanClass, String beanName) {
        return !IBizHandler.class.isAssignableFrom(beanClass);
    }

}
