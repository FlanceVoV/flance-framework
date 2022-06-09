package com.flance.tx.datasource.proxy.plugins;

import org.apache.ibatis.plugin.Invocation;

public interface ExecutorHandler {

    static Object intercept(Invocation invocation) throws Throwable {
        return invocation.proceed();
    }

}
