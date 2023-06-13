package com.flance.tx.server.netty.server;

import com.flance.tx.server.netty.configs.NettyRtmpServerConfig;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class NettyRtmpServer {


    @Resource
    NettyRtmpServerConfig nettyRtmpServerConfig;

    private final NioEventLoopGroup boss = new NioEventLoopGroup();
    private final NioEventLoopGroup worker = new NioEventLoopGroup();

    private Channel channel;

    public ChannelFuture lister() {

        log.info("开启 TC-NETTY-RTMP-SERVER 服务监听[{}] - [{}] - [{}]", nettyRtmpServerConfig.getRtmpServerId(), nettyRtmpServerConfig.getRtmpServerIp(), nettyRtmpServerConfig.getRtmpServerPort());

        ChannelFuture future;

        try {
            ServerBootstrap sb = new ServerBootstrap()
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new RtspDecoder());
                            socketChannel.pipeline().addLast(new RtspEncoder());
                            socketChannel.pipeline().addLast(new RtspServerHandler());
                        }
                    });
            future = sb.bind(nettyRtmpServerConfig.getRtmpServerPort()).sync();
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
