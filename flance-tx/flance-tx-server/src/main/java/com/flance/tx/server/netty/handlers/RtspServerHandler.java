package com.flance.tx.server.netty.handlers;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.rtsp.RtspHeaderNames;
import io.netty.handler.codec.rtsp.RtspHeaderValues;
import io.netty.handler.codec.rtsp.RtspResponseStatuses;
import io.netty.handler.codec.rtsp.RtspVersions;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class RtspServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        log.info("[RTSPHandler]channelRead:  {}", msg.getClass());
        FullHttpResponse o = new DefaultFullHttpResponse(RtspVersions.RTSP_1_0, RtspResponseStatuses.OK);
        if(msg instanceof DefaultHttpRequest){
            DefaultHttpRequest req = (DefaultHttpRequest)msg;
            HttpMethod method = req.method();
            String methodName = method.name();
            log.info("method: {}", methodName);
            /** 以下就是具体消息的处理, 需要开发者自己实现 */
            if(methodName.equalsIgnoreCase("OPTIONS") ||
                    methodName.equalsIgnoreCase("DESCRIBE")){
                log.info("1[{}]", methodName);
                o.headers().add(RtspHeaderValues.PUBLIC, "DESCRIBE, SETUP, PLAY, TEARDOWN, ANNOUNCE, RECORD, GET_PARAMETER");
            }else{
                log.info("2[{}]", methodName);
            }
        }else {
            HttpContent content = msg;
            if(content.content().isReadable()) {
                log.info("3[{}]", msg);
            }
        }

        final String cseq = msg.headers().get(RtspHeaderNames.CSEQ);
        if (cseq != null) {
            o.headers().add(RtspHeaderNames.CSEQ, cseq);
        }
        final String session = msg.headers().get(RtspHeaderNames.SESSION);
        if (session != null) {
            o.headers().add(RtspHeaderNames.SESSION, session);
        }
        if (!HttpUtil.isKeepAlive(msg)) {
            ctx.writeAndFlush(o).addListener(ChannelFutureListener.CLOSE);
        } else {
            o.headers().set(RtspHeaderNames.CONNECTION, RtspHeaderValues.KEEP_ALIVE);
            ctx.writeAndFlush(o);
        }
    }
}
