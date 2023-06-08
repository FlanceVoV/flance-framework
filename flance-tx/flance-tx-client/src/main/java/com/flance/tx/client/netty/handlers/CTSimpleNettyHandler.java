package com.flance.tx.client.netty.handlers;

import com.flance.tx.client.netty.ClientCallbackService;
import com.flance.tx.netty.current.CurrentNettyData;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
import com.flance.tx.netty.handler.NettyChannelInboundHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class CTSimpleNettyHandler extends NettyChannelInboundHandler<NettyRequest, NettyResponse> {

    public CTSimpleNettyHandler() {
        super(new ClientReceiveHandler());
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        super.channelRead0(ctx, msg);
        ClientCallbackService callbackService = CurrentNettyData.removeCallback(respMessage.getMessageId(), ctx.channel());
        if (null != callbackService) {
            callbackService.receiveMessage(respMessage);
        }
    }
}
