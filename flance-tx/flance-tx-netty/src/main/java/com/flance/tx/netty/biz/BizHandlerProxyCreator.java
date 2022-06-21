package com.flance.tx.netty.biz;

import com.flance.tx.datasource.proxy.FlanceTxProxy;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.TargetSource;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.BeansException;

import javax.sql.DataSource;

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
