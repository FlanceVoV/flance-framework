package com.flance.tx.client.netty;


import com.flance.tx.netty.current.CallBackService;
import com.flance.tx.netty.data.NettyResponse;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ClientCallbackService implements CallBackService<NettyResponse> {

    private final CountDownLatch latch = new CountDownLatch(1);

    private NettyResponse nettyData;

    public void awaitThread(int timeout, TimeUnit unit) throws InterruptedException {
        latch.await(timeout, unit);
    }

    public void receiveMessage(NettyResponse data) throws Exception {
        this.nettyData = data;
        //释放调用线程
        latch.countDown();
    }

    public NettyResponse getData() {
        return nettyData;
    }

}
