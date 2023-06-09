package com.flance.tx.demo3;

import com.flance.tx.client.netty.CTNettyClient;
import com.flance.tx.client.netty.ClientCallbackService;
import com.flance.tx.netty.current.CurrentNettyData;
import com.flance.tx.netty.data.DataUtils;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
import com.flance.tx.netty.data.ServerData;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@ComponentScan({"com.flance.*"})
@SpringBootApplication
public class Demo3Application {

    public static void main(String[] args) {

        SpringApplication.run(Demo3Application.class, args);
    }


    @Bean
    public Object test(ApplicationContext applicationContext) throws Exception {
        String roomId = UUID.randomUUID().toString();
        CTNettyClient client = new CTNettyClient("127.0.0.1", 8899, applicationContext);
        client.start();
        Channel heartChannel = client.getChannel();
        NettyRequest request = new NettyRequest();
        request.setMessageId(UUID.randomUUID().toString());
        request.setHandlerId("serverStartConnectHandler");
        request.setData("心跳检查");
        request.setRoomId(roomId);
        heartChannel.writeAndFlush(DataUtils.getStr(request).getBytes(StandardCharsets.UTF_8)).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        final ClientCallbackService clientCallbackService = new ClientCallbackService();
        CurrentNettyData.putCallback2DataMap(request.getMessageId(), heartChannel, clientCallbackService);
        clientCallbackService.getData();
        clientCallbackService.awaitThread(15, TimeUnit.SECONDS);
        NettyResponse data = clientCallbackService.getData();
        System.out.println(data);
        return null;
    }

}