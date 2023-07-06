package com.flance.tx.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public interface CustomProtocol {

    void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out);

    void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out);

}
