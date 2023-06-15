package com.flance.tx.server.netty.handlers;

import com.flance.tx.common.utils.SpringUtil;
import com.flance.tx.netty.biz.IBizHandler;
import com.flance.tx.netty.data.DataUtils;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
import com.flance.tx.netty.handler.IReceiveHandler;
import com.flance.tx.netty.handler.NettyChannelInboundHandler;
import com.flance.tx.server.netty.biz.wesocket.WebSocketReleaseHandler;
import com.flance.tx.server.netty.biz.wesocket.WebSocketUrlHandler;
import com.flance.web.utils.AssertException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class WsHandler extends NettyChannelInboundHandler<NettyResponse, NettyRequest> {

    private WebSocketServerHandshaker handshaker;

    public WsHandler(IReceiveHandler<NettyResponse, NettyRequest> iReceiveHandler) {
        super(iReceiveHandler);
    }



    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("收到消息：[{}]", msg);
        if (msg instanceof FullHttpRequest req) {
            //以http请求形式接入，但是走的是websocket
            QueryStringDecoder decoder = new QueryStringDecoder(req.uri());
            String path = decoder.path();
            String uri = decoder.uri();
            handleHttpRequest(ctx, (FullHttpRequest) msg, uri, path);
        }
        if (msg instanceof WebSocketFrame) {
            handleWebSocketRequest(ctx, (WebSocketFrame) msg);
        }
    }


    /**
     * ws握手地址
     */
    private String getWebSocketLocation(FullHttpRequest request) {
        String location = request.headers().get(HttpHeaderNames.HOST) + request.uri();
        return "ws://" + location;
    }

    /**
     * 唯一的一次http请求，用于创建websocket
     * */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req, String uri, String path) {
        // 要求Upgrade为websocket，过滤掉get/Post
        if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
            // 若不是websocket方式，则创建BAD_REQUEST的req，返回给客户端
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketLocation(req), null, true, 5 * 1024 * 1024);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
            try {
                WebSocketUrlHandler socketUrlHandler = SpringUtil.getBean(path, WebSocketUrlHandler.class);
                socketUrlHandler.doHandler(ctx.channel(), uri);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("获取handler失败[{}]", path);
                ctx.close();
            }

        }
    }

    /**
     * 拒绝不合法的请求，并返回错误信息
     * */
    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(),
                    CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        // 如果是非Keep-Alive，关闭连接
        if (res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * websocket处理
     *
     * @param ctx
     * @param frame
     */
    private void handleWebSocketRequest(ChannelHandlerContext ctx, WebSocketFrame frame) {

        log.info("处理收到消息 [{}]", frame);

        // 关闭
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            try {
                WebSocketReleaseHandler releaseHandler = SpringUtil.getBean("webSocketReleaseHandler", WebSocketReleaseHandler.class);
                releaseHandler.release();
            } catch (Exception ignore) {
            }
            return;
        }

        // 握手PING/PONG
        if (frame instanceof PingWebSocketFrame) {
            ctx.write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        // 文本
        if (frame instanceof TextWebSocketFrame) {
            String msg = ((TextWebSocketFrame) frame).text();
            if (msg.equals("PING")) {
                ctx.writeAndFlush(new TextWebSocketFrame("PONG")).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                return;
            }
            try {
                super.channelRead0(ctx, msg);
            } catch (AssertException e) {
                e.printStackTrace();
                TextWebSocketFrame webSocketFrame = new TextWebSocketFrame(DataUtils.getStr(e.getMsg()));
                ctx.writeAndFlush(webSocketFrame).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                TextWebSocketFrame webSocketFrame = new TextWebSocketFrame(DataUtils.getStr(e.getCause()));
                ctx.writeAndFlush(webSocketFrame).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                return;
            }

            if (null != this.message) {
                TextWebSocketFrame webSocketFrame = new TextWebSocketFrame(DataUtils.getStr(this.message));
                // 写回数据也是按照协议格式写TextWebSocketFrame
                ctx.writeAndFlush(webSocketFrame).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }

            return;
        }

        if (frame instanceof BinaryWebSocketFrame) {
            return;
        }
    }

}
