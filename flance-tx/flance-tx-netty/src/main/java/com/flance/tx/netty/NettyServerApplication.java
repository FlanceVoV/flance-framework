package com.flance.tx.netty;

import com.flance.tx.netty.server.NettyServer;
import io.netty.channel.ChannelFuture;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.Resource;

@ComponentScan({"com.flance.*"})
@SpringBootApplication
public class NettyServerApplication implements CommandLineRunner {

    @Resource
    NettyServer nettyServer;

    public static void main(String[] args) {
        SpringApplication.run(NettyServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ChannelFuture channelFuture = nettyServer.lister(8899);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            nettyServer.destroy();
        }));
        channelFuture.channel().closeFuture().channel();
    }
}
