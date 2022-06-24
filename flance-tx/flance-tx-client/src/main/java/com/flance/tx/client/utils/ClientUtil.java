package com.flance.tx.client.utils;

import com.flance.tx.client.netty.configs.NettyClientConfig;
import com.flance.tx.netty.data.ServerData;

public class ClientUtil {

    private static ServerData serverData = null;

    public static ServerData getServerData(NettyClientConfig nettyClientConfig) {
        if (null == serverData) {
            serverData = new ServerData();
            serverData.setApplicationId(nettyClientConfig.getNettyClientId());
            serverData.setId(nettyClientConfig.getNettyClientIp());
            serverData.setPort(nettyClientConfig.getNettyClientPort());
            serverData.setIp(nettyClientConfig.getNettyClientIp());
        }
        return serverData;
    }

}
