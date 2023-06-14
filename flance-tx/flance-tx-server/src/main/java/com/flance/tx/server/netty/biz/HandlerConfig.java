package com.flance.tx.server.netty.biz;

import com.flance.tx.netty.biz.IBizHandler;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerConfig {

    @Bean("clientStatusHandler")
    @ConditionalOnMissingBean(name = "clientStatusHandler")
    public IBizHandler<NettyResponse, NettyRequest> getClientStatusHandler() {
        return new ClientStatusHandler();
    }

    @Bean("connectHandler")
    @ConditionalOnMissingBean(name = "connectHandler")
    public IBizHandler<NettyResponse, NettyRequest> getConnectHandler() {
        return new ConnectHandler();
    }

    @Bean("serverPingReceiverHandler")
    @ConditionalOnMissingBean(name = "serverPingReceiverHandler")
    public IBizHandler<NettyResponse, NettyRequest> getServerPingReceiverHandler() {
        return new ServerPingReceiverHandler();
    }

}
