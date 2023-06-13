package com.flance.tx.server.netty.server;

import com.flance.tx.server.netty.configs.NettyWebsocketServerConfig;
import com.flance.tx.server.netty.handlers.ServerReceiveHandler;
import com.flance.tx.server.netty.handlers.WsHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class NettyWebsocketServer {

    @Resource
    NettyWebsocketServerConfig nettyWebsocketServerConfig;

    private final NioEventLoopGroup boss = new NioEventLoopGroup();
    private final NioEventLoopGroup worker = new NioEventLoopGroup();

    private Channel channel;

    public ChannelFuture lister() {

        log.info("开启 TC-NETTY-WS-SERVER 服务监听[{}] - [{}] - [{}]", nettyWebsocketServerConfig.getWsServerId(), nettyWebsocketServerConfig.getWsServerIp(), nettyWebsocketServerConfig.getWsServerPort());

        ChannelFuture future;

        try {
            ServerBootstrap sb = new ServerBootstrap()
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            CorsConfig corsConfig = CorsConfigBuilder.forAnyOrigin().allowNullOrigin().allowCredentials().build();

                            socketChannel.pipeline()
                                    .addLast(new HttpResponseEncoder())
                                    .addLast(new HttpRequestDecoder())
                                    .addLast(new ChunkedWriteHandler())
                                    .addLast(new HttpObjectAggregator(64 * 1024))
                                    .addLast(new CorsHandler(corsConfig))
                                    .addLast(new WsHandler(new ServerReceiveHandler()));
                        }
                    });
            future = sb.bind(nettyWebsocketServerConfig.getWsServerPort()).sync();
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
