package com.flance.tx.server.netty;

import com.flance.tx.server.netty.server.NettyServer;
import io.netty.channel.ChannelFuture;
import org.springframework.boot.CommandLineRunner;

import javax.annotation.Resource;

public class NettyServerStartApp implements CommandLineRunner {

    @Resource
    protected NettyServer nettyServer;

    @Override
    public void run(String... args) {
        if (null == nettyServer) {
            throw new RuntimeException("无法注入nettyServer");
        }
        ChannelFuture channelFuture = nettyServer.lister();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            nettyServer.destroy();
        }));
        channelFuture.channel().closeFuture().channel();
    }


}
