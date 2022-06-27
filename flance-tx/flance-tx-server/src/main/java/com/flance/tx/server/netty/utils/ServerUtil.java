package com.flance.tx.server.netty.utils;

import com.flance.tx.netty.data.ServerData;
import com.flance.tx.server.netty.configs.NettyServerConfig;

public class ServerUtil {

    private static ServerData serverData = new ServerData();;

    public static ServerData getServerData() {
        return serverData;
    }

    public static void setServerData(NettyServerConfig nettyServerConfig) {
        serverData.setApplicationId(nettyServerConfig.getNettyServerId());
        serverData.setId(nettyServerConfig.getNettyServerId());
        serverData.setPort(nettyServerConfig.getNettyServerPort());
        serverData.setIp(nettyServerConfig.getNettyServerIp());
    }

}
