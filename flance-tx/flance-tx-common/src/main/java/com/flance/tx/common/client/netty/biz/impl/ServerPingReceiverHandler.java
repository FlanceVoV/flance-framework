package com.flance.tx.common.client.netty.biz.impl;

import com.flance.tx.common.client.netty.biz.IBizHandler;
import com.flance.tx.common.client.netty.data.NettyRequest;
import com.flance.tx.common.client.netty.data.NettyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("serverPingReceiverHandler")
public class ServerPingReceiverHandler implements IBizHandler<NettyResponse, NettyRequest> {

    @Override
    public NettyResponse doBizHandler(NettyRequest request) {
        NettyResponse response = new NettyResponse();
        log.info("get ping success");
        response.setIsHeartBeat(request.getIsHeartBeat());
        response.setMessageId(request.getMessageId());
        response.setRequest(request);
        response.setHandlerId("clientPongReceiverHandler");
        return response;
    }
}
