package com.flance.tx.server.netty.server;

import com.flance.tx.server.netty.handlers.CTSimpleNettyServerHandler;
import com.flance.tx.netty.handler.MsgByteToMessageCodec;
import com.flance.tx.server.netty.configs.NettyServerConfig;
import com.flance.tx.server.netty.handlers.RtspServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.rtsp.RtspDecoder;
import io.netty.handler.codec.rtsp.RtspEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import javax.sql.DataSource;

@Slf4j
@Component
public class NettyServer {

    @Resource
    DataSource dataSource;

    @Resource
    NettyServerConfig nettyServerConfig;

    private final NioEventLoopGroup boss = new NioEventLoopGroup();
    private final NioEventLoopGroup worker = new NioEventLoopGroup();

    private Channel channel;

    public ChannelFuture lister() {

        log.info("开启 TC-NETTY-SERVER 服务监听[{}] - [{}] - [{}]", nettyServerConfig.getNettyServerId(), nettyServerConfig.getNettyServerIp(), nettyServerConfig.getNettyServerPort());

        ChannelFuture future;

        try {
            ServerBootstrap sb = new ServerBootstrap()
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new MsgByteToMessageCodec());
                            socketChannel.pipeline().addLast(new StringEncoder());
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(new CTSimpleNettyServerHandler(dataSource));
                        }
                    });
            future = sb.bind(nettyServerConfig.getNettyServerPort()).sync();
            channel = future.channel();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return future;

    }

    public void destroy() {
        if (null == channel) {
            return;
        }
        worker.shutdownGracefully();
        boss.shutdownGracefully();
    }

}
