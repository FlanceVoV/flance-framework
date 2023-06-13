package com.flance.tx.common;

public interface TxConstants {

    /**
     * 配置文件前缀
     */
    String FLANCE_TX_PREFIX = "flance.tx";

    String FLANCE_TX_RTMP_PREFIX = "flance.tx.rtmp";

    String FLANCE_TX_WS_PREFIX = "flance.tx.ws";

    String FLANCE_TX_SERVER_PREFIX = "flance.tx.server";

    String FLANCE_TX_CLIENT_PREFIX = "flance.tx.client";

    /**
     * 全局事务代理自动装配失败处理器
     */
    String BEAN_NAME_FAILURE_HANDLER = "failureHandler";

    /**
     * 自定义的spring容器
     */
    String BEAN_NAME_SPRING_APPLICATION_CONTEXT_PROVIDER = "springApplicationContextProvider";

    /**
     * bean name 数据源代理工厂
     */
    String BEAN_NAME_DATA_SOURCE_PROXY_CREATOR = "flanceDataSourceProxyCreator";

    /**
     * netty handler proxy
     */
    String BEAN_NAME_NETTY_BIZ_HANDLER_PROXY_CREATOR = "bizHandlerProxyCreator";

    /**
     *
     */
    String BEAN_NAME_MYBATIS_PLUGIN_CREATOR = "flanceMybatisPluginCreator";

    /**
     * 默认字符集
     */
    String DEFAULT_CHARSET_NAME = "UTF-8";


    /**
     * 注解属性
     */
    String AUTO_DATA_SOURCE_PROXY_ATTRIBUTE_KEY_EXCLUDES = "excludes";

    String ENABLE_AUTO_DATA_SOURCE_PROXY = "enableDataSourceProxy";


    /**
     * 服务模式
     */
    String TX_CENTER_MODULE_NETTY = "netty";

    String SQL_COMMAND_SELECT = "SELECT";

    String SQL_COMMAND_INSERT = "INSERT";

    String SQL_COMMAND_UPDATE = "UPDATE";

    String SQL_COMMAND_DELETE = "DELETE";

    String HTTP_HEADER_TX_ID = "txId";

    String FEIGN_HEADER_TX_ROOM_ID = "roomId";

}
