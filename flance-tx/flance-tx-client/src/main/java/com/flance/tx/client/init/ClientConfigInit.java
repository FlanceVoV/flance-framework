package com.flance.tx.client.init;

import com.flance.tx.client.netty.configs.NettyClientConfig;
import com.flance.tx.client.utils.ClientUtil;
import com.flance.web.utils.init.IHandler;



public class ClientConfigInit implements IHandler {

    private final NettyClientConfig nettyClientConfig;

    public ClientConfigInit(NettyClientConfig nettyClientConfig) {
        this.nettyClientConfig = nettyClientConfig;
    }

    @Override
    public void handler() {
        ClientUtil.setServerData(nettyClientConfig);
    }

}
