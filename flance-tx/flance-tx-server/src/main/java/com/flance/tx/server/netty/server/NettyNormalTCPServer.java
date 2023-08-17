package com.flance.tx.server.netty.server;

import com.flance.tx.netty.handler.CustomByteToMsgCode;
import com.flance.tx.netty.handler.CustomProtocol;
import com.flance.tx.netty.handler.ITcpReceiveHandler;
import com.flance.tx.netty.handler.MsgByteToMessageCodec;
import com.flance.tx.server.netty.configs.NettyTCPServerConfig;
import com.flance.tx.server.netty.handlers.CTSimpleNettyServerHandler;
import com.flance.tx.server.netty.handlers.TCPHandler;
import com.google.common.collect.Lists;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class NettyNormalTCPServer {

    @Resource
    NettyTCPServerConfig nettyTCPServerConfig;

    @Resource
    ITcpReceiveHandler iTcpReceiveHandler;

    @Resource
    CustomProtocol customProtocol;


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
                            if (null != nettyTCPServerConfig.getCustomEnds() && nettyTCPServerConfig.getCustomEnds().size() > 0) {
                                List<ByteBuf> delemiters = Lists.newArrayList();
                                for(int i = 0; i < nettyTCPServerConfig.getCustomEnds().size(); i ++) {
                                    ByteBuf delemiter= Unpooled.buffer();
                                    delemiter.writeBytes(nettyTCPServerConfig.getCustomEnds().get(i).getBytes());
                                    delemiters.add(delemiter);
                                }
                                ByteBuf[] arr = new ByteBuf[delemiters.size()];
                                socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, true, true, delemiters.toArray(arr)));
                            } else {
                                socketChannel.pipeline().addLast(new CustomByteToMsgCode(customProtocol));
                            }
                            socketChannel.pipeline().addLast(new StringEncoder());
                            socketChannel.pipeline().addLast(new StringDecoder());
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
