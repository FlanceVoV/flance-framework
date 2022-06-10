package com.flance.tx.netty.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Slf4j
public class TxHandler extends SimpleChannelInboundHandler {

    private DataSource dataSource;

    public TxHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("激活管道");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("断开链接");
        super.channelInactive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        log.info("读取信息");
        if(msg instanceof String) {
            log.info(msg.toString());
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(msg.toString());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("读取信息");
        if(msg instanceof String) {
            log.info(msg.toString());
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(msg.toString());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("链接异常");
        super.exceptionCaught(ctx, cause);
    }
}
