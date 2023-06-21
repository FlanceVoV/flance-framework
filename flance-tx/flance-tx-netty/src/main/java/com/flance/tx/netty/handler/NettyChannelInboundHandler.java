package com.flance.tx.netty.handler;

import com.flance.tx.common.netty.RoomContainer;
import com.flance.tx.common.utils.GsonUtils;
import com.flance.tx.common.utils.StringUtils;
import com.flance.web.utils.AssertException;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public abstract class NettyChannelInboundHandler<T, R> extends SimpleChannelInboundHandler<Object> {

    private final IReceiveHandler<T, R> iReceiveHandler;

    protected T message;

    protected R respMessage;

    public NettyChannelInboundHandler(IReceiveHandler<T, R> iReceiveHandler) {
        this.iReceiveHandler = iReceiveHandler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("激活管道");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("断开链接");
        super.channelInactive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        int len = 10;
        String receive = "";
        try {
            receive = (String) msg;
            log.debug("读取到报文 - 原文:{}", msg);
            if (receive.length() < len) {
                log.info("报文小于10位");
                throw AssertException.getNormal("-1", "报文小于10位");
            }
            if (receive.startsWith("$_")) {
                receive = receive.replaceFirst("\\$_", "");
            }
            String length = receive.substring(0, 10);
            //非数字不处理
            log.debug("报文前十位[{}]", length);
            if (!StringUtils.isNumeric(length)) {
                log.error("报文前十位非数字[{}]", length);
                throw AssertException.getNormal("-1", "报文前十位非数字[" + length + "]");
            }
            //报文长度小于前四位指定的长度 不处理
            int dataLen = receive.getBytes(StandardCharsets.UTF_8).length;
            log.debug("数据报文长度[{}]", dataLen);
            log.debug("业务报文长度[{}]", length);
            if (dataLen - len != Integer.parseInt(length)) {
                log.warn("报文长度匹配");
                throw AssertException.getNormal("-1", "报文长度不匹配header[" + Integer.parseInt(length) + "] data[" + dataLen + "]");
            }

            byte[] msgBytes = receive.getBytes(StandardCharsets.UTF_8);
            //截取报文前十位长度的报文
            byte[] tempMsg = new byte[Integer.parseInt(length)];
            System.arraycopy(msgBytes, 10, tempMsg, 0, tempMsg.length);

            String receiveMsg = new String(tempMsg, StandardCharsets.UTF_8);
            log.info("业务报文 - 原文:{}", receiveMsg.length());
            log.debug("业务报文 - 原文:{}", receiveMsg);
            this.message = iReceiveHandler.handler(receiveMsg, ctx.channel());
            this.respMessage = iReceiveHandler.getOrigin(receiveMsg, ctx.channel());
            if (null != this.message) {
                log.debug("业务报名 - 解码[{}]", GsonUtils.toJSONString(this.message));
            }
        } catch (AssertException e) {
            ctx.close();
            RoomContainer.release();
            throw e;
        } catch (Exception e) {
            log.error("报文解析异常，报文内容为:" + msg, e);
            ctx.close();
            RoomContainer.release();
            e.printStackTrace();
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("链接异常");
        cause.printStackTrace();
        super.exceptionCaught(ctx, cause);
    }


    protected void writeTo(ChannelHandlerContext ctx, String writeMsg) {
        ctx.writeAndFlush(writeMsg.getBytes(StandardCharsets.UTF_8)).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }

}
