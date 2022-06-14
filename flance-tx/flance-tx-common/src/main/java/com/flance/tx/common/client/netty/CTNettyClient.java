package com.flance.tx.common.client.netty;

import com.flance.tx.common.client.netty.handler.CTSimpleNettyHandler;
import com.flance.tx.common.client.netty.handler.MsgByteToMessageCodec;
import com.flance.tx.common.utils.SpringUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

@Data
@Slf4j
public class CTNettyClient implements NettyClient {

    private final String serverIp;

    private final Integer serverPort;

    private final ApplicationContext applicationContext;

    private Channel channel;

    public CTNettyClient(String serverIp, Integer serverPort, ApplicationContext applicationContext) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.applicationContext = applicationContext;
        SpringUtil.setApplicationContextByOut(applicationContext);
    }

    @Override
    public void start() throws Exception {
        final EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)  // 使用NioSocketChannel来作为连接用的channel类
                .handler(new ChannelInitializer<SocketChannel>() { // 绑定连接初始化器
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        System.out.println("正在连接中...");
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new MsgByteToMessageCodec());
                        pipeline.addLast(new StringEncoder()); //编码request
                        pipeline.addLast(new StringDecoder()); //解码response
                        pipeline.addLast(new CTSimpleNettyHandler()); //客户端处理类

                    }
                });

        //发起异步连接请求，绑定连接端口和host信息
        final ChannelFuture future = b.connect(serverIp, serverPort).sync();

        future.addListener((ChannelFutureListener) listener -> {
            if (future.isSuccess()) {
                log.info("服务器连接成功");

            } else {
                log.error("服务器连接失败");
                future.cause().printStackTrace();
                group.shutdownGracefully();
            }
        });

        this.channel = future.channel();
    }

}
