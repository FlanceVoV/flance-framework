package com.flance.tx.common.client.netty.biz.impl;

import com.flance.tx.common.client.netty.biz.IBizHandler;
import com.flance.tx.common.client.netty.data.NettyRequest;
import com.flance.tx.common.client.netty.data.NettyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("clientPongReceiverHandler")
public class ClientPongReceiverHandler implements IBizHandler<NettyRequest, NettyResponse> {

    @Override
    public NettyRequest doBizHandler(NettyResponse response) {
        log.info("get pong success");
        return null;
    }
}
