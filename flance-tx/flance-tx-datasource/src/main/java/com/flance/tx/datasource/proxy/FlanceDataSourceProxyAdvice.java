package com.flance.tx.datasource.proxy;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.IntroductionInfo;

/**
 * 数据源方法切面
 * @author jhf
 */
@Slf4j
public class FlanceDataSourceProxyAdvice implements MethodInterceptor, IntroductionInfo {

    private final Class<? extends FlanceDataSourceProxy> dataSourceProxyClazz;

    public FlanceDataSourceProxyAdvice() {
        this.dataSourceProxyClazz = AbstractDataSourceProxy.class;

    }

    public FlanceDataSourceProxyAdvice(Class<? extends FlanceDataSourceProxy> dataSourceProxyClazz) {
        this.dataSourceProxyClazz = dataSourceProxyClazz;
    }

    /**
     * 切面方法增强
     */
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        return methodInvocation.proceed();
    }

    /**
     * 定义获取接口，用于DefaultIntroductionAdvisor构造代理
     */
    @Override
    public Class<?>[] getInterfaces() {
        return new Class[]{FlanceTxProxy.class};
    }
}
