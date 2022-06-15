package com.flance.tx.common.utils;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

/**
 * 动态定时任务工具类
 * @author jhf
 */
public class ThreadUtils {

    private final static ThreadPoolTaskScheduler THREAD_POOL_TASK_SCHEDULER = new ThreadPoolTaskScheduler();

    static {
        // 初始化
        THREAD_POOL_TASK_SCHEDULER.initialize();
        // 线程池数量
        THREAD_POOL_TASK_SCHEDULER.setPoolSize(16);
    }

    /**
     * 固定周期定时任务
     * @param executeFunction   无参无返回函数接口
     * @param cronTrigger       周期
     */
    public static void execSupplierByCycle(ThreadExecuteFunction executeFunction, CronTrigger cronTrigger) {
        execSupplierNow(executeFunction::execute);
        THREAD_POOL_TASK_SCHEDULER.schedule(executeFunction::execute, cronTrigger);
    }

    /**
     * 立即执行任务
     * @param executeFunction    无参无返回函数接口
     */
    public static void execSupplierNow(ThreadExecuteFunction executeFunction) {
        THREAD_POOL_TASK_SCHEDULER.execute(executeFunction::execute);
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}
