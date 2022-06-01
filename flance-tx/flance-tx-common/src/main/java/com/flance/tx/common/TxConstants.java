package com.flance.tx.common;

public interface TxConstants {

    /**
     * 配置文件前缀
     */
    String FLANCE_TX_PREFIX = "flance.tx";

    /**
     * 全局事务代理自动装配失败处理器
     */
    String BEAN_NAME_FAILURE_HANDLER = "failureHandler";

    /**
     * 自定义的spring容器
     */
    String BEAN_NAME_SPRING_APPLICATION_CONTEXT_PROVIDER = "springApplicationContextProvider";

}
