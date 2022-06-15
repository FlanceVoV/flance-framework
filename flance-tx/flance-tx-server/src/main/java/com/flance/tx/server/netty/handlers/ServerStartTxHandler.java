package com.flance.tx.server.netty.handlers;

import com.flance.tx.netty.biz.IBizHandler;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
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
