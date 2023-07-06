package com.flance.tx.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class CustomByteToMsgCode extends ByteToMessageCodec<Object> {

    private CustomProtocol protocol;

    public CustomByteToMsgCode(CustomProtocol protocol) {
        this.protocol = protocol;
    }

    /**
     * 处理粘包问题
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        log.info("接收到消息！");
        protocol.decode(ctx,buf,out);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        log.info("响应消息！");
        byte[] body = (byte[]) msg;
        out.writeBytes(body);
    }



}
