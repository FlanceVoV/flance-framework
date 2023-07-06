package com.flance.tx.server.netty.handlers;

import com.flance.tx.netty.handler.ITcpReceiveHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TCPHandler extends ChannelInboundHandlerAdapter {


    private final ITcpReceiveHandler tcpReceiveHandler;

    public TCPHandler(ITcpReceiveHandler tcpReceiveHandler) {
        this.tcpReceiveHandler = tcpReceiveHandler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        log.info("接收到tcp消息：[{}]", msg);
        String resp = tcpReceiveHandler.handler(body, ctx.channel());
        log.info("处理完成：[{}]", resp);
        if (null != resp) {
            ctx.writeAndFlush(resp);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("断开连接: " + cause.getMessage());
        ctx.close();
    }

}
