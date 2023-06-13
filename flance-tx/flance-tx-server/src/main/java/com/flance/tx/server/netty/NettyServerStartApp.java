package com.flance.tx.server.netty;

import com.flance.tx.server.netty.configs.NettyRtmpServerConfig;
import com.flance.tx.server.netty.configs.NettyWebsocketServerConfig;
import com.flance.tx.server.netty.server.NettyRtmpServer;
import com.flance.tx.server.netty.server.NettyServer;
import com.flance.tx.server.netty.server.NettyWebsocketServer;
import io.netty.channel.ChannelFuture;
import org.springframework.boot.CommandLineRunner;

import javax.annotation.Resource;

public class NettyServerStartApp implements CommandLineRunner {

    @Resource
    protected NettyServer nettyServer;

    @Resource
    protected NettyRtmpServer nettyRtmpServer;

    @Resource
    protected NettyWebsocketServer nettyWebsocketServer;

    @Resource
    protected NettyRtmpServerConfig rtmpServerConfig;

    @Resource
    private NettyWebsocketServerConfig websocketServerConfig;

    @Override
    public void run(String... args) {
        startNettyServer();
        startNettyRtmpServer();
        setNettyWebsocketServer();
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

    private void setNettyWebsocketServer() {
        if (null == websocketServerConfig || !websocketServerConfig.isEnable()) {
            return;
        }
        if (null == nettyWebsocketServer) {
            throw new RuntimeException("无法注入WebsocketServer");
        }
        ChannelFuture channelFuture = nettyWebsocketServer.lister();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            nettyWebsocketServer.destroy();
        }));
        channelFuture.channel().closeFuture().channel();
    }

}
