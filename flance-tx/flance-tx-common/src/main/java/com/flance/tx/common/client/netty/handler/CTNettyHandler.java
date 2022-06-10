package com.flance.tx.common.client.netty.handler;

import com.flance.tx.common.client.netty.data.NettyResponse;
import com.flance.tx.common.utils.GsonUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CTNettyHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        log.info("接收到服务响应[{}]", s);
        NettyResponse nettyResponse = GsonUtils.fromString(s, NettyResponse.class);
        if (nettyResponse.getIsHeartBeat()) {
            log.info("心跳检测[{}]", s);
        }
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("激活管道");
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
