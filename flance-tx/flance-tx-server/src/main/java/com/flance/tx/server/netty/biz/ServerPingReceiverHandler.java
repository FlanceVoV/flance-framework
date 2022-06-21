package com.flance.tx.server.netty.biz;

import com.flance.tx.netty.biz.IBizHandler;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
import com.flance.tx.netty.data.ServerData;
import com.flance.tx.server.netty.configs.NettyServerConfig;
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
        ServerData serverData = new ServerData();
        serverData.setApplicationId(nettyServerConfig.getNettyServerId());
        serverData.setId(nettyServerConfig.getNettyServerId());
        serverData.setPort(nettyServerConfig.getNettyServerPort());
        serverData.setIp(nettyServerConfig.getNettyServerIp());
        log.info("get ping success");
        response.setIsHeartBeat(request.getIsHeartBeat());
        response.setMessageId(request.getMessageId());
        response.setRequest(request);
        response.setHandlerId("clientPongReceiverHandler");
        response.setServerData(serverData);
        response.setRoomId(request.getRoomId());
        return response;
    }

}
