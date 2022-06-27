package com.flance.tx.server.netty.init;

import com.flance.tx.server.netty.configs.NettyServerConfig;
import com.flance.tx.server.netty.utils.ServerUtil;
import com.flance.web.utils.init.IHandler;

public class ServerConfigInit implements IHandler {

    private final NettyServerConfig nettyServerConfig;

    public ServerConfigInit(NettyServerConfig nettyServerConfig) {
        this.nettyServerConfig = nettyServerConfig;
    }

    @Override
    public void handler() {
        ServerUtil.setServerData(nettyServerConfig);
    }

}
