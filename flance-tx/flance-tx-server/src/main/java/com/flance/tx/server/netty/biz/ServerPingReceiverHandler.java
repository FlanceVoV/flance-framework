package com.flance.tx.server.netty.biz;

import com.flance.tx.netty.biz.IBizHandler;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
import com.flance.tx.netty.data.ServerData;
import com.flance.tx.server.netty.configs.NettyServerConfig;
import com.flance.tx.server.netty.utils.ServerUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Slf4j
@Component("serverPingReceiverHandler")
public class ServerPingReceiverHandler implements IBizHandler<NettyResponse, NettyRequest> {

    @Resource
    NettyServerConfig nettyServerConfig;

    @Override
    public NettyResponse doBizHandler(NettyRequest request, Channel channel) {
        NettyResponse response = new NettyResponse();
        log.info("get ping success");
        response.setIsHeartBeat(request.getIsHeartBeat());
        response.setMessageId(request.getMessageId());
        response.setRequest(request);
        response.setHandlerId("clientPongReceiverHandler");
        response.setServerData(ServerUtil.getServerData(nettyServerConfig));
        response.setRoomId(request.getRoomId());
        return response;
    }

}
