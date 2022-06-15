package com.flance.tx.client.netty.handlers;

import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
import com.flance.tx.netty.handler.NettyChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class CTSimpleNettyHandler extends NettyChannelInboundHandler<NettyRequest, NettyResponse> {

    public CTSimpleNettyHandler() {
        super(new ClientReceiveHandler());
    }
}
