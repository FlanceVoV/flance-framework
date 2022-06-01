package com.flance.tx.common.client;

/**
 * tc 客户端 基类
 * @author jhf
 */
public interface BaseClient {

    void setClientId(String clientId);

    void setClientName(String clientName);

    void setServerIp(String ip);

    void setServerPort(int port);

    void init();

    void destroy();

    void register();

}
