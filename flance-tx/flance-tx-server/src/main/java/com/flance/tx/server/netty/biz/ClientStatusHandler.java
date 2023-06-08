package com.flance.tx.server.netty.biz;

import com.flance.tx.common.utils.GsonUtils;
import com.flance.tx.netty.biz.IBizHandler;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("clientStatusHandler")
public class ClientStatusHandler implements IBizHandler<NettyResponse, NettyRequest> {

    @Override
    public NettyResponse doBizHandler(NettyRequest nettyRequest, Channel channel) {
        log.info("保存客户端实时心跳状态 [{}]", GsonUtils.toJSONString(nettyRequest));
        return null;
    }
}
