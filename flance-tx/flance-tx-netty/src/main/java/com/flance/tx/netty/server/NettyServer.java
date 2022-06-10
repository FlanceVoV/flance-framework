package com.flance.tx.netty.server;

import com.flance.tx.netty.server.handler.TxHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Slf4j
@Component
public class NettyServer {

    @Resource
    DataSource dataSource;

    private final NioEventLoopGroup boss = new NioEventLoopGroup();
    private final NioEventLoopGroup worker = new NioEventLoopGroup();

    private Channel channel;

    public ChannelFuture lister(int port) {

        ChannelFuture future;

        try {
            ServerBootstrap sb = new ServerBootstrap()
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(new StringEncoder());
                            socketChannel.pipeline().addLast(new TxHandler(dataSource));
                        }
                    });
            future = sb.bind(port).sync();
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
