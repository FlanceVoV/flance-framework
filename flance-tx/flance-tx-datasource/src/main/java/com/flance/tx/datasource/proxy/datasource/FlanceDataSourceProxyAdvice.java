package com.flance.tx.datasource.proxy.datasource;

import com.flance.tx.core.annotation.FlanceGlobalTransactional;
import com.flance.tx.core.tx.TxThreadLocal;
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
                FlanceGlobalTransactional.Module module = TxThreadLocal.getTxModule();
                if (null == module) {
                    return methodInvocation.proceed();
                }
                log.info("检测到全局事务 - 模式 - [{}]", module.getModule());
                switch (module) {
                    case CT:
                        dataSource = new CTDataSourceProxy((DataSource) methodInvocation.getThis());
                        break;
                    case AT:
                        dataSource = new DataSourceProxy((DataSource) methodInvocation.getThis());
                        break;
                    default:
                        return methodInvocation.proceed();
                }
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
