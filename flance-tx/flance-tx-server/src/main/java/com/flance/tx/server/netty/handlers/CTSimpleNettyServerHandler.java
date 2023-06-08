package com.flance.tx.server.netty.handlers;

import com.flance.tx.netty.data.DataUtils;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
import com.flance.tx.netty.handler.NettyChannelInboundHandler;
import com.flance.web.utils.GsonUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;

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
