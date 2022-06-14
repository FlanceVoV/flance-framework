package com.flance.tx.common.client.netty.handler;

import com.flance.tx.common.client.netty.biz.IBizHandler;
import com.flance.tx.common.client.netty.data.NettyRequest;
import com.flance.tx.common.client.netty.data.NettyResponse;
import com.flance.tx.common.utils.Base64Utils;
import com.flance.tx.common.utils.GsonUtils;
import com.flance.tx.common.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class ClientReceiveHandler implements IReceiveHandler<NettyRequest, NettyResponse>{

    @Override
    public NettyRequest handler(String msg) {
        log.info("收到服务端响应[{}]", msg);
        NettyRequest newRequest;
        try {
            String string = new String(Base64Utils.decode(msg), StandardCharsets.UTF_8);
            log.info("解析服务端响应[{}]", string);
            NettyResponse response = GsonUtils.fromString(string, NettyResponse.class);
            if (null != response.getHandlerId()) {
                IBizHandler<NettyRequest, NettyResponse> handler = SpringUtil.getBean(response.getHandlerId(), IBizHandler.class);
                newRequest = handler.doBizHandler(response);
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
    public NettyRequest handler(NettyResponse msg) {
        return null;
    }
}
