package com.flance.tx.client.netty.handlers;

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
public class ClientReceiveHandler implements IReceiveHandler<NettyRequest, NettyResponse> {

    @Override
    public NettyRequest handler(String msg, Channel channel) {
        log.info("收到服务端响应[{}]", msg);
        NettyRequest newRequest;
        try {
            NettyResponse response = getOrigin(msg, channel);
            if (null != response.getHandlerId()) {
                IBizHandler<NettyRequest, NettyResponse> handler = SpringUtil.getBean(response.getHandlerId(), IBizHandler.class);
                newRequest = handler.doBizHandler(response, channel);
            } else {
                newRequest = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("接收服务响应失败！即将断开连接");
        }
        return newRequest;
    }

    @Override
    public NettyRequest handler(NettyResponse msg, Channel channel) {
        return null;
    }

    @Override
    public NettyResponse getOrigin(String msg, Channel channel) {
        try {
            String string = new String(Base64Utils.decode(msg), StandardCharsets.UTF_8);
//            log.info("解析服务端响应[{}]", string);
            return GsonUtils.fromString(string, NettyResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("接收服务响应失败！即将断开连接");
        }
    }

    @Override
    public NettyResponse getOrigin(NettyResponse msg, Channel channel) {
        return msg;
    }
}
