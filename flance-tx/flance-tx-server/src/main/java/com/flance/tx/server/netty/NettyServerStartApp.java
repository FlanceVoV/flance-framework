package com.flance.tx.server.netty;

import com.flance.tx.server.netty.configs.NettyRtmpServerConfig;
import com.flance.tx.server.netty.server.NettyRtmpServer;
import com.flance.tx.server.netty.server.NettyServer;
import io.netty.channel.ChannelFuture;
import org.springframework.boot.CommandLineRunner;

import javax.annotation.Resource;

public class NettyServerStartApp implements CommandLineRunner {

    @Resource
    protected NettyServer nettyServer;

    @Resource
    protected NettyRtmpServer nettyRtmpServer;

    @Resource
    protected NettyRtmpServerConfig rtmpServerConfig;

    @Override
    public void run(String... args) {
        startNettyServer();
        startNettyRtmpServer();
    }


    private void startNettyServer() {
        if (null == nettyServer) {
            throw new RuntimeException("无法注入nettyServer");
        }
        ChannelFuture channelFuture = nettyServer.lister();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            nettyServer.destroy();
        }));
        channelFuture.channel().closeFuture().channel();
    }

    private void startNettyRtmpServer() {
        if (null == rtmpServerConfig || !rtmpServerConfig.isEnable()) {
            return;
        }
        if (null == nettyRtmpServer) {
            throw new RuntimeException("无法注入RtmpServer");
        }
        ChannelFuture channelFuture = nettyRtmpServer.lister();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            nettyRtmpServer.destroy();
        }));
        channelFuture.channel().closeFuture().channel();
    }

}
