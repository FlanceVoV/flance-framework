package com.flance.tx.client.netty.biz;

import com.flance.tx.netty.biz.IBizHandler;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("clientPongReceiverHandler")
public class ClientPongReceiverHandler implements IBizHandler<NettyRequest, NettyResponse> {

    @Override
    public NettyRequest doBizHandler(NettyResponse response) {
        log.info("get pong success");
        return null;
    }
}
