package com.flance.tx.common.client.netty.handler;

import com.flance.tx.common.client.netty.data.NettyRequest;
import com.flance.tx.common.client.netty.data.NettyResponse;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class CTSimpleNettyHandler extends NettyChannelInboundHandler<NettyRequest, NettyResponse> {

    public CTSimpleNettyHandler() {
        super(new ClientReceiveHandler());
    }
}
