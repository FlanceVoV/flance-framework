package com.flance.tx.datasource.proxy.plugins;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.aop.IntroductionInfo;

@Slf4j
public class CTMybatisPluginProxyAdvice implements MethodInterceptor, IntroductionInfo {

    private Interceptor interceptor;

    public CTMybatisPluginProxyAdvice() {
    }
    public CTMybatisPluginProxyAdvice(Interceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        log.info("mybatis 增强");
        return methodInvocation.proceed();
    }

    @Override
    public Class<?>[] getInterfaces() {
        return new Class[]{FlanceMybatisPlugins.class};
    }
}
