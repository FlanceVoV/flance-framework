package com.flance.tx.server.netty.handlers;

import com.flance.tx.netty.biz.IBizHandler;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
import com.flance.tx.common.utils.Base64Utils;
import com.flance.tx.common.utils.GsonUtils;
import com.flance.tx.common.utils.SpringUtil;
import com.flance.tx.netty.handler.IReceiveHandler;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class ServerReceiveHandler implements IReceiveHandler<NettyResponse, NettyRequest> {

    @Override
    public NettyResponse handler(String msg, Channel channel) {
        NettyResponse response;
        log.info("收到的报文内容为[{}]", msg);
        try {
            NettyRequest request = getOrigin(msg, channel);
            if (null != request.getHandlerId()) {
                IBizHandler<NettyResponse, NettyRequest> bizHandler = SpringUtil.getBean(request.getHandlerId(), IBizHandler.class);
                response = bizHandler.doBizHandler(request, channel);
            } else {
                response = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("接收客户端请求异常！即将断开");
        }
        return response;
    }

    @Override
    public NettyResponse handler(NettyRequest msg, Channel channel) {
        return null;
    }

    @Override
    public NettyRequest getOrigin(String msg, Channel channel) {
        try {
            String string = new String(Base64Utils.decode(msg), StandardCharsets.UTF_8);
            return GsonUtils.fromString(string, NettyRequest.class);
        } catch (Exception e) {
            throw new RuntimeException("接收客户端请求异常！即将断开");
        }
    }

    @Override
    public NettyRequest getOrigin(NettyRequest msg, Channel channel) {
        return msg;
    }
}
