package com.flance.tx.server.netty.utils;

import com.flance.tx.netty.data.ServerData;
import com.flance.tx.server.netty.configs.NettyServerConfig;

public class ServerUtil {

    private static ServerData serverData = null;

    public static ServerData getServerData(NettyServerConfig nettyServerConfig) {
        if (null == serverData) {
            serverData = new ServerData();
            serverData.setApplicationId(nettyServerConfig.getNettyServerId());
            serverData.setId(nettyServerConfig.getNettyServerId());
            serverData.setPort(nettyServerConfig.getNettyServerPort());
            serverData.setIp(nettyServerConfig.getNettyServerIp());
        }
        return serverData;
    }

}
