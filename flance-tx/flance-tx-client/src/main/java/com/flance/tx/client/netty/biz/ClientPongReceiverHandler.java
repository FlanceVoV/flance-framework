package com.flance.tx.client.netty.biz;

import com.flance.tx.netty.biz.IBizHandler;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Slf4j
public class ClientPongReceiverHandler implements IBizHandler<NettyRequest, NettyResponse> {

    @Override
    public NettyRequest doBizHandler(NettyResponse response, Channel channel) {
        log.info("get pong success");
        NettyRequest request = new NettyRequest();
        request.setHandlerId("clientStatusHandler");
        request.setRoomId(response.getRoomId());
        request.setData("客户端信息json");
        request.setMessageId(response.getMessageId());
        request.setServerData(response.getServerData());
        request.setClientId(response.getClientId());
        return null;
    }
}
