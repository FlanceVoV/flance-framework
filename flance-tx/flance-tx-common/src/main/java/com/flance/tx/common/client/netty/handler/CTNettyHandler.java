package com.flance.tx.common.client.netty.handler;

import com.flance.tx.common.client.netty.data.NettyResponse;
import com.flance.tx.common.utils.GsonUtils;
import com.flance.tx.common.utils.StringUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class CTNettyHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("接收到服务响应[{}]", msg);

        int len = 10;
        NettyResponse response;
        String recieve = "";
        try {
            recieve = (String) msg;
            log.info("服务端接收到的内容为:{}", msg);
            //小于64位不处理
            if (recieve.length() < len) {
                log.info("报文小于10位");
                ctx.close();
                return;
            }
            String length = recieve.substring(0, 10);
            //非数字不处理
            log.info("报文前十位[{}]", length);
            if (!StringUtils.isNumeric(length)) {
                log.error("报文前十位非数字[{}]", length);
                return;
            }
            //报文长度小于前四位指定的长度 不处理
            log.info("业务报文长度[{}]", recieve.getBytes(StandardCharsets.UTF_8).length);
            if (recieve.getBytes(StandardCharsets.UTF_8).length - len < Integer.parseInt(length)) {
                log.info("报文长度不够");
                ctx.close();
                return;
            }

            byte[] msgBytes = recieve.getBytes(StandardCharsets.UTF_8);
            //截取报文前十位长度的报文
            byte[] tempMsg = new byte[Integer.parseInt(length)];
            System.arraycopy(msgBytes, 10, tempMsg, 0, tempMsg.length);
            MsgHandler msgHandler = new MsgHandler();
            response = msgHandler.handler(new String(tempMsg, StandardCharsets.UTF_8));
            log.info("服务端响应的的内容为:{}", response);
            if (response.getIsHeartBeat()) {
                log.info("心跳检测[{}]", msg);
            }
        } catch (Exception e) {
            log.error("报文解析异常，报文内容为:" + msg, e);
        }

    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("激活管道");
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
