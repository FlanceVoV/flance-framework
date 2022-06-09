package com.flance.tx.datasource.proxy.plugins;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Invocation;

/**
 * AT 模式 executor 处理器
 * @author jhf
 */
@Slf4j
public class ATExecutorHandler implements ExecutorHandler {

    public static Object intercept(Invocation invocation) throws Throwable {
        log.info("AT 模式 ATExecutorHandler");
        return invocation.proceed();
    }

}
