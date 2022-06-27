package com.flance.tx.client.utils;

import com.flance.tx.client.netty.configs.NettyClientConfig;
import com.flance.tx.netty.data.ServerData;

public class ClientUtil {

    private final static ServerData serverData = new ServerData();

    public static ServerData getServerData() {
        return serverData;
    }

    public static void setServerData(NettyClientConfig nettyClientConfig) {
        serverData.setApplicationId(nettyClientConfig.getNettyClientId());
        serverData.setId(nettyClientConfig.getNettyClientId());
        serverData.setPort(nettyClientConfig.getNettyClientPort());
        serverData.setIp(nettyClientConfig.getNettyClientIp());
    }

}
