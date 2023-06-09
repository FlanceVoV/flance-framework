package com.flance.tx.demo1;

import com.flance.jdbc.binlog.BinLogStarter;
import com.flance.tx.client.netty.CTNettyClient;
import com.flance.tx.client.netty.ClientCallbackService;
import com.flance.tx.common.netty.TalkVo;
import com.flance.tx.common.utils.GsonUtils;
import com.flance.tx.config.tx.EnableFlanceTx;
import com.flance.tx.netty.current.CurrentNettyData;
import com.flance.tx.netty.data.DataUtils;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@ComponentScan({"com.flance.*"})
@SpringBootApplication
public class Demo1Application extends BinLogStarter {

    public static void main(String[] args) {

        SpringApplication.run(Demo1Application.class, args);
    }

    @Bean
    public Object test(ApplicationContext applicationContext) throws Exception {
        String roomId = "设备二";
        CTNettyClient client = new CTNettyClient("127.0.0.1", 8899, applicationContext);
        client.start();
        Channel heartChannel = client.getChannel();
        NettyRequest request = new NettyRequest();
        request.setMessageId(UUID.randomUUID().toString());
        request.setHandlerId("connectHandler");
        request.setData("开始连接");
        request.setRoomId(roomId);
        heartChannel.writeAndFlush(DataUtils.getStr(request).getBytes(StandardCharsets.UTF_8)).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        final ClientCallbackService clientCallbackService = new ClientCallbackService();
        CurrentNettyData.putCallback2DataMap(request.getMessageId(), heartChannel, clientCallbackService);
        clientCallbackService.getData();
        clientCallbackService.awaitThread(15, TimeUnit.SECONDS);
        NettyResponse data = clientCallbackService.getData();
        System.out.println(data);

        Thread.sleep(20000);

        NettyRequest talkReq = new NettyRequest();
        TalkVo talkVo = new TalkVo();
        talkVo.setFrom("设备二");
        talkVo.setTo("设备一");
        talkVo.setMsg("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        talkReq.setRoomId("设备二");
        talkReq.setHandlerId("talkHandler");
        talkReq.setData(GsonUtils.toJSONString(talkVo));
        heartChannel.writeAndFlush(DataUtils.getStr(talkReq).getBytes(StandardCharsets.UTF_8)).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        return null;
    }

}
