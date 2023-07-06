package com.flance.tx.server.netty.server;

import com.flance.tx.netty.handler.ITcpReceiveHandler;
import com.flance.tx.netty.handler.MsgByteToMessageCodec;
import com.flance.tx.server.netty.configs.NettyTCPServerConfig;
import com.flance.tx.server.netty.handlers.CTSimpleNettyServerHandler;
import com.flance.tx.server.netty.handlers.TCPHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class NettyNormalTCPServer {

    @Resource
    NettyTCPServerConfig nettyTCPServerConfig;

    @Resource
    ITcpReceiveHandler iTcpReceiveHandler;


    private final NioEventLoopGroup boss = new NioEventLoopGroup();
    private final NioEventLoopGroup worker = new NioEventLoopGroup();

    private Channel channel;

    public ChannelFuture lister() {

        log.info("开启 TC-NETTY-TCP-SERVER 服务监听[{}] - [{}] - [{}]", nettyTCPServerConfig.getTcpServerId(), nettyTCPServerConfig.getTcpServerIp(), nettyTCPServerConfig.getTcpServerPort());

        ChannelFuture future;

        try {
            ServerBootstrap sb = new ServerBootstrap()
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            socketChannel.pipeline().addLast(new ByteArrayEncoder());
                            socketChannel.pipeline().addLast(new ByteArrayDecoder());
                            socketChannel.pipeline().addLast(new TCPHandler(iTcpReceiveHandler));
                        }
                    });
            future = sb.bind(nettyTCPServerConfig.getTcpServerPort()).sync();
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
