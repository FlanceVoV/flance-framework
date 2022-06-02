package com.flance.tx.datasource.proxy;

import com.google.common.collect.Lists;
import org.springframework.aop.Advisor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.support.DefaultIntroductionAdvisor;
import org.springframework.beans.BeansException;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据源代理构造器
 * @author jhf
 */
public class FlanceDataSourceProxyCreator extends AbstractAutoProxyCreator {

    private final Advisor advisor;

    private final List<String> excludes;

    /**
     * 构造函数，创建数据源代理切面
     */
    public FlanceDataSourceProxyCreator() {
        this.excludes = Lists.newArrayList();
        this.advisor = new DefaultIntroductionAdvisor(new FlanceDataSourceProxyAdvice());
    }

    public FlanceDataSourceProxyCreator(List<String> excludes) {
        this.excludes = excludes;
        this.advisor = new DefaultIntroductionAdvisor(new FlanceDataSourceProxyAdvice());
    }

    /**
     * 创建切面方法增强
     */
    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> aClass, String s, TargetSource targetSource) throws BeansException {
        return new Object[]{advisor};
    }

    /**
     * 跳过代理创建
     * 只接受数据源的、实现FlanceTxProxy和excludes配置的
     */
    @Override
    protected boolean shouldSkip(Class<?> beanClass, String beanName) {
        return !DataSource.class.isAssignableFrom(beanClass) ||
                FlanceTxProxy.class.isAssignableFrom(beanClass) ||
                excludes.contains(beanClass.getName());
    }
}
