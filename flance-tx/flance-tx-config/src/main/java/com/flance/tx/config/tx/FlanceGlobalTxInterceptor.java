package com.flance.tx.config.tx;

import com.flance.tx.core.annotation.FlanceGlobalTransactional;
import com.flance.tx.core.tx.TxThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Method;

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
        Object ret;
        try {
            before(methodInvocation);
            ret = methodInvocation.proceed();
            after(methodInvocation);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            TxThreadLocal.removeTxModule();
        }
        return ret;
    }


    private void before(MethodInvocation methodInvocation) {
        log.info("FlanceGlobalTxInterceptor - 之前");
        Method method = methodInvocation.getMethod();
        FlanceGlobalTransactional flanceGlobalTransactional = method.getAnnotation(FlanceGlobalTransactional.class);
        if (null == flanceGlobalTransactional) {
            return;
        }
        FlanceGlobalTransactional.Module module = flanceGlobalTransactional.module();
        TxThreadLocal.setTxModule(module);
    }


    private void after(MethodInvocation methodInvocation) {
        log.info("FlanceGlobalTxInterceptor - 之后");

    }

}
