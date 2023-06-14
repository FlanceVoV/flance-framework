package com.flance.tx.client.netty.biz;

import com.flance.tx.netty.biz.IBizHandler;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerConfig {

    @Bean("clientPongReceiverHandler")
    @ConditionalOnMissingBean(name = "clientPongReceiverHandler")
    public IBizHandler<NettyRequest, NettyResponse> getClientPongReceiverHandler() {
        return new ClientPongReceiverHandler();
    }

}
