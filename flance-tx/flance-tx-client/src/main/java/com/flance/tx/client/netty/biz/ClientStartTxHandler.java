package com.flance.tx.client.netty.biz;

import com.flance.tx.netty.biz.IBizHandler;
import com.flance.tx.netty.current.CurrentNettyData;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("clientStartTxHandler")
public class ClientStartTxHandler implements IBizHandler<NettyRequest, NettyResponse> {


    @Override
    public NettyRequest doBizHandler(NettyResponse response, Channel channel) {
        String data = response.getData();
        log.info("收到服务端sql执行结果：{}", data);
        return null;
    }
}
