package com.flance.tx.config.tx;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 全局事务 方法拦截器
 * @author jhf
 */
@Slf4j
public class FlanceGlobalTxInterceptor implements MethodInterceptor {

    private FailureHandler failureHandler;

    public FlanceGlobalTxInterceptor() {
    }

    public FlanceGlobalTxInterceptor(FailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }


    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        before(methodInvocation);
        Object ret = methodInvocation.proceed();
        after(methodInvocation);
        return ret;
    }


    private void before(MethodInvocation methodInvocation) {
        log.info("FlanceGlobalTxInterceptor - 之前");
    }


    private void after(MethodInvocation methodInvocation) {
        log.info("FlanceGlobalTxInterceptor - 之后");
    }

}
