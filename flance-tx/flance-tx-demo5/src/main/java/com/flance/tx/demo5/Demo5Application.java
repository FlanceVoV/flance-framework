package com.flance.tx.demo5;

import com.flance.tx.client.netty.CTNettyClient;
import com.flance.tx.client.netty.ClientCallbackService;
import com.flance.tx.common.netty.TalkVo;
import com.flance.tx.common.utils.GsonUtils;
import com.flance.tx.common.utils.ThreadUtils;
import com.flance.tx.netty.biz.BizHandlerProxyCreator;
import com.flance.tx.netty.config.NettyConfiguration;
import com.flance.tx.netty.current.CurrentNettyData;
import com.flance.tx.netty.data.DataUtils;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@ComponentScan(basePackages = {"com.flance.*"}, excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {NettyConfiguration.class})})
@SpringBootApplication
public class Demo5Application {

    public static void main(String[] args) {
        log.info(" roomId ============= [room1] client id ============== [demo5]");
        SpringApplication.run(Demo5Application.class, args);
    }


    @Bean
    public Channel getChannel(ApplicationContext applicationContext) throws Exception {
        String clientId = "demo5";
        String roomId = "room1";
        CTNettyClient client = new CTNettyClient("47.99.42.18", 8899, applicationContext);
        client.start();
        Channel heartChannel = client.getChannel();
        NettyRequest request = new NettyRequest();
        request.setClientId(clientId);
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
        return heartChannel;
    }


    @Bean
    public Object startImage(Channel channel) {
        String testImage = "D:\\tmp\\uploads\\1686535073552.png";
        String base64 = getImgBase(testImage);

        new Thread(() -> {
            for (int i = 0; i < 900; i++) {
                System.out.println("-----------------------即将发送---------------------");
//                ThreadUtils.sleep(2000);
                ImagePushModel imagePushModel = new ImagePushModel();
                imagePushModel.setImgNo(i);
                imagePushModel.setBase64(base64);
                NettyRequest request = new NettyRequest();
                request.setRoomId("room1");
                request.setClientId("demo5");
                request.setMessageId("sendImage");
                request.setData(GsonUtils.toJSONString(imagePushModel));
                request.setHandlerId("pushVideoStreamHandler");
                System.out.println(i);
                if (channel.isActive() && channel.isOpen()) {
                    channel.writeAndFlush(DataUtils.getStr(request).getBytes(StandardCharsets.UTF_8)).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                } else {
                    System.exit(0);
                }
            }

        }).start();

        return null;
    }


    public static String getImgBase(String imgFile) {

        // 将图片文件转化为二进制流
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 图片头
        //String imghead = "data:image/jpeg;base64,";
        return Base64.encodeBase64String(data);
    }


}
