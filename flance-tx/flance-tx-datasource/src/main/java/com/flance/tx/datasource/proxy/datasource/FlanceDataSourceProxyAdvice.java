package com.flance.tx.datasource.proxy.datasource;

import com.flance.tx.datasource.proxy.FlanceTxProxy;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.IntroductionInfo;
import org.springframework.beans.BeanUtils;

import javax.sql.DataSource;
import java.lang.reflect.Method;

/**
 * 数据源方法切面
 * @author jhf
 */
@Slf4j
public class FlanceDataSourceProxyAdvice implements MethodInterceptor, IntroductionInfo {

    private final Class<? extends FlanceDataSourceProxy> dataSourceProxyClazz;

    public FlanceDataSourceProxyAdvice() {
        this.dataSourceProxyClazz = DataSourceProxy.class;

    }

    public FlanceDataSourceProxyAdvice(Class<? extends FlanceDataSourceProxy> dataSourceProxyClazz) {
        this.dataSourceProxyClazz = dataSourceProxyClazz;
    }

    /**
     * 切面方法增强
     */
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        Method method = methodInvocation.getMethod();
        Object[] args = methodInvocation.getArguments();

        DataSource dataSource;
        Method m = BeanUtils.findDeclaredMethod(dataSourceProxyClazz, method.getName(), method.getParameterTypes());
        if (m != null && DataSource.class.isAssignableFrom(method.getDeclaringClass())) {
            if (methodInvocation.getThis() instanceof FlanceDataSourceProxy) {
                dataSource = (FlanceDataSourceProxy) methodInvocation.getThis();
            } else {
                dataSource = new DataSourceProxy((DataSource) methodInvocation.getThis());
            }
            return m.invoke(dataSource, args);
        } else {
            return methodInvocation.proceed();
        }

    }

    /**
     * 定义获取接口，用于DefaultIntroductionAdvisor构造代理
     */
    @Override
    public Class<?>[] getInterfaces() {
        return new Class[]{FlanceTxProxy.class};
    }
}
