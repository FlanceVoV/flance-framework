package com.flance.tx.common.client.netty.handler;

import com.flance.tx.common.client.netty.data.DataUtils;
import com.flance.tx.common.client.netty.data.NettyRequest;
import com.flance.tx.common.client.netty.data.NettyResponse;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

@Slf4j
public class CTSimpleNettyServerHandler extends NettyChannelInboundHandler<NettyResponse, NettyRequest> {

    private final DataSource dataSource;

    public CTSimpleNettyServerHandler(DataSource dataSource) {
        super(new ServerReceiveHandler());
        this.dataSource = dataSource;
    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        super.channelRead0(ctx, msg);
        if (null != message) {
            String writeMsg = DataUtils.getStr(super.message);
            writeTo(ctx, writeMsg);
        }
    }
}
