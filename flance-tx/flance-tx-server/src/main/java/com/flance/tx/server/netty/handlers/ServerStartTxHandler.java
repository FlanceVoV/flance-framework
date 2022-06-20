package com.flance.tx.server.netty.handlers;

import com.flance.tx.common.utils.GsonUtils;
import com.flance.tx.core.tx.FlanceTransaction;
import com.flance.tx.netty.biz.IBizHandler;
import com.flance.tx.netty.container.rooms.ConnectionRoom;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

/**
 * 开启事务处理器
 * @author jhf
 */
@Component("startTxHandler")
public class ServerStartTxHandler implements IBizHandler<NettyResponse, NettyRequest> {

    @Override
    public NettyResponse doBizHandler(NettyRequest request, Channel channel) {
        NettyResponse result = new NettyResponse();
        try {
            FlanceTransaction tx = GsonUtils.fromString(request.getData(), FlanceTransaction.class);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);

        }
        return result;
    }
}
