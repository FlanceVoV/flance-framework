package com.flance.jdbc.binlog.utils;

/**
 * function interface
 * @author jhf
 */
@FunctionalInterface
public interface ThreadExecuteFunction {

    /**
     * 执行逻辑
     */
    void execute();

}
