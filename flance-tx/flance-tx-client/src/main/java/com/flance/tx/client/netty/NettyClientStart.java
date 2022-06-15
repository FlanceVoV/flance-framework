package com.flance.tx.client.netty;

import com.flance.tx.netty.data.DataUtils;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.common.utils.ThreadUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class NettyClientStart {

    private static final AtomicInteger count = new AtomicInteger(-1);

    public static void startNettyClient(ApplicationContext applicationContext) {
        try {
            CTNettyClient ctNettyClient = new CTNettyClient("127.0.0.1", 8899, applicationContext);
            ctNettyClient.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启服务端心跳检测
     */
    public static void sendHeartBeat(String ip, int port, int reTryTimes, ApplicationContext applicationContext) {

        if (count.get() == -1) {
            count.set(reTryTimes);
        }

        ThreadUtils.execSupplierNow(() -> {

            Channel heartChannel;

            try {
                CTNettyClient ctNettyClient = new CTNettyClient(ip, port, applicationContext);
                ctNettyClient.start();
                heartChannel = ctNettyClient.getChannel();
            } catch (Exception e) {
                e.printStackTrace();
                log.info("服务中断等待15s将再次重试连接，重试次数[{}]", count.get());
                ThreadUtils.sleep(15000);
                if (count.decrementAndGet() > 0) {
                    sendHeartBeat(ip, port, reTryTimes, applicationContext);
                } else {
                    log.info("服务中断重试连接失败");
                    System.exit(0);
                }
                return;
            }

            NettyRequest request = new NettyRequest();
            request.setIsHeartBeat(true);
            request.setHandlerId("serverPingReceiverHandler");
            request.setData("心跳检查");

            while (true) {
                request.setMessageId(UUID.randomUUID().toString());
                try {
                    log.info("连接是否激活 - active[{}], open[{}], writable[{}]", heartChannel.isActive(), heartChannel.isOpen(), heartChannel.isActive());
                    if (!(heartChannel.isWritable() && heartChannel.isOpen() && heartChannel.isActive())) {
                        throw new RuntimeException("channel不可写");
                    }
                    heartChannel.writeAndFlush(DataUtils.getStr(request).getBytes(StandardCharsets.UTF_8)).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                    ThreadUtils.sleep(3000);
                    if (count.get() != reTryTimes) {
                        count.set(reTryTimes);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    heartChannel.close();
                    log.info("心跳检测线程中断");
                    break;
                }
            }
            log.info("服务中断等待15s将再次尝试心跳检测，重试次数[{}]", count.get());
            ThreadUtils.sleep(15000);
            if (count.decrementAndGet() > 0) {
                sendHeartBeat(ip, port, reTryTimes, applicationContext);
            }

        });

    }



}
