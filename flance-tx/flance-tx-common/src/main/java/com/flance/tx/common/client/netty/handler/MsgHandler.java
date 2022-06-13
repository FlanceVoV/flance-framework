package com.flance.tx.common.client.netty.handler;

import com.flance.tx.common.client.netty.data.NettyRequest;
import com.flance.tx.common.client.netty.data.NettyResponse;
import com.flance.tx.common.utils.Base64Utils;
import com.flance.tx.common.utils.GsonUtils;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class MsgHandler {

    /**
     * 业务出路
     * @param msg 请求报文
     * @return
     */
    public NettyResponse handler(String msg) {
        NettyResponse response = new NettyResponse();
        log.info("收到的报文内容为"+msg);
        try {
            String string = new String(Base64Utils.decode(msg), StandardCharsets.UTF_8);
            NettyRequest request = GsonUtils.fromString(string, NettyRequest.class);
            response.setIsHeartBeat(request.getIsHeartBeat());
            response.setMessageId(request.getMessageId());
            response.setRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;

    }

}
