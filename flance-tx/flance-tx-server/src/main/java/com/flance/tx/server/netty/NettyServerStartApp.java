package com.flance.tx.server.netty;

import com.flance.tx.server.netty.configs.NettyRtspServerConfig;
import com.flance.tx.server.netty.configs.NettyTCPServerConfig;
import com.flance.tx.server.netty.configs.NettyWebsocketServerConfig;
import com.flance.tx.server.netty.server.NettyNormalTCPServer;
import com.flance.tx.server.netty.server.NettyRtspServer;
import com.flance.tx.server.netty.server.NettyServer;
import com.flance.tx.server.netty.server.NettyWebsocketServer;
import io.netty.channel.ChannelFuture;
import org.springframework.boot.CommandLineRunner;

import javax.annotation.Resource;

public class NettyServerStartApp implements CommandLineRunner {

    @Resource
    protected NettyServer nettyServer;

    @Resource
    protected NettyRtspServer nettyRtmpServer;

    @Resource
    private NettyNormalTCPServer nettyNormalTCPServer;

    @Resource
    protected NettyWebsocketServer nettyWebsocketServer;

    @Resource
    protected NettyRtspServerConfig rtmpServerConfig;

    @Resource
    private NettyWebsocketServerConfig websocketServerConfig;

    @Resource
    private NettyTCPServerConfig nettyTCPServerConfig;

    @Override
    public void run(String... args) {
        startNettyServer();
        startNettyRtspServer();
        setNettyWebsocketServer();
        setNettyTcpServer();
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

    private void startNettyRtspServer() {
        if (null == rtmpServerConfig || !rtmpServerConfig.isEnable()) {
            return;
        }
        if (null == nettyRtmpServer) {
            throw new RuntimeException("无法注入RtspServer");
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

    private void setNettyTcpServer() {
        if (null == nettyTCPServerConfig || !nettyTCPServerConfig.isEnable()) {
            return;
        }
        if (null == nettyNormalTCPServer) {
            throw new RuntimeException("无法注入TCPServer");
        }
        ChannelFuture channelFuture = nettyNormalTCPServer.lister();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            nettyNormalTCPServer.destroy();
        }));
        channelFuture.channel().closeFuture().channel();
    }

}
