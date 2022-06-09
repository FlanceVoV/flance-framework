package com.flance.tx.datasource.proxy.plugins;

import com.flance.tx.common.utils.SpringUtil;
import com.flance.tx.datasource.proxy.FlanceTxProxy;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.aop.IntroductionInfo;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Method;

@Slf4j
public class DefaultMybatisPluginProxyAdvice implements MethodInterceptor, IntroductionInfo {

    private Interceptor interceptor;

    public DefaultMybatisPluginProxyAdvice() {
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();

        Method m = BeanUtils.findDeclaredMethod(FlanceMybatisPlugins.class, method.getName(), method.getParameterTypes());

        if (method.getName().equals("intercept")) {
            log.info("mybatis 增强");
            Interceptor interceptor = SpringUtil.getBean("aTExecutorHandlerInterceptor", Interceptor.class);
            Interceptor interceptor2 = SpringUtil.getBean("cTExecutorHandlerInterceptor", Interceptor.class);

            return methodInvocation.proceed();
        } else {
            return methodInvocation.proceed();
        }
    }

    @Override
    public Class<?>[] getInterfaces() {
        return new Class[]{FlanceTxProxy.class};
    }
}
