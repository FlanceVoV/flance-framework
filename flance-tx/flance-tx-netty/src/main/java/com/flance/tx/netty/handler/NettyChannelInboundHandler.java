package com.flance.tx.netty.handler;

import com.flance.tx.common.utils.GsonUtils;
import com.flance.tx.common.utils.StringUtils;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public abstract class NettyChannelInboundHandler<T, R> extends SimpleChannelInboundHandler<String> {

    private final IReceiveHandler<T, R> iReceiveHandler;

    protected T message;

    public NettyChannelInboundHandler(IReceiveHandler<T, R> iReceiveHandler) {
        this.iReceiveHandler = iReceiveHandler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("激活管道");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("断开链接");
        super.channelInactive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        int len = 10;
        T message = null;
        String receive = "";
        try {
            receive = (String) msg;
            log.info("服务端接收到的内容为:{}", msg);
            //小于64位不处理
            if (receive.length() < len) {
                log.info("报文小于10位");
                ctx.close();
                return;
            }
            String length = receive.substring(0, 10);
            //非数字不处理
            log.info("报文前十位[{}]", length);
            if (!StringUtils.isNumeric(length)) {
                log.error("报文前十位非数字[{}]", length);
                return;
            }
            //报文长度小于前四位指定的长度 不处理
            log.info("数据报文长度[{}]", receive.getBytes(StandardCharsets.UTF_8).length);
            log.info("业务报文长度[{}]", receive.getBytes(StandardCharsets.UTF_8).length - len);
            if (receive.getBytes(StandardCharsets.UTF_8).length - len < Integer.parseInt(length)) {
                log.info("报文长度不够");
                ctx.close();
                return;
            }

            byte[] msgBytes = receive.getBytes(StandardCharsets.UTF_8);
            //截取报文前十位长度的报文
            byte[] tempMsg = new byte[Integer.parseInt(length)];
            System.arraycopy(msgBytes, 10, tempMsg, 0, tempMsg.length);

            String receiveMsg = new String(tempMsg, StandardCharsets.UTF_8);
            log.info("服务端响应的的内容为:{}", receiveMsg);
            message = iReceiveHandler.handler(receiveMsg, ctx.channel());
            this.message = message;
            if (null != message) {
                log.info("服务端响应解析[{}]", GsonUtils.toJSONString(message));
            }
        } catch (Exception e) {
            log.error("报文解析异常，报文内容为:" + msg, e);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("链接异常");
        super.exceptionCaught(ctx, cause);
    }


    protected void writeTo(ChannelHandlerContext ctx, String writeMsg) {
        ctx.writeAndFlush(writeMsg.getBytes(StandardCharsets.UTF_8)).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }

}
