package com.flance.tx.netty.handlers;

import com.flance.tx.common.client.netty.biz.IBizHandler;
import com.flance.tx.common.client.netty.data.NettyRequest;
import com.flance.tx.common.client.netty.data.NettyResponse;
import org.springframework.stereotype.Component;

/**
 * 开启事务处理器
 * @author jhf
 */
@Component("startTxHandler")
public class ServerStartTxHandler implements IBizHandler<NettyResponse, NettyRequest> {

    @Override
    public NettyResponse doBizHandler(NettyRequest request) {


        return null;
    }
}
