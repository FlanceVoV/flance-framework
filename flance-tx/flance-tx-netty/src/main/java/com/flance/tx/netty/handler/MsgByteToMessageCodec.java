package com.flance.tx.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class MsgByteToMessageCodec extends ByteToMessageCodec<Object> {

    /**
     * 处理粘包问题
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        int msgLength = 10;
        if (buf.readableBytes() < msgLength) {// 不足长度10(开始10位代表整个报文长度位)，无法获取长度
            return;
        }
        //记下readerIndex
        buf.markReaderIndex();

        //获取前10位表示报文长度的字符串
        String lenstr = getMsgLength(buf, msgLength);
        //转为整型
        int length = toInt(lenstr);
        if (length <= 0) {
            ctx.close();
            return;
        }

        //判断报文长度是否到达报文前十位指定的长度
        if (buf.readableBytes() < length) {
            //重置到上一次调用markReaderIndex()的readerIndex位置
            buf.resetReaderIndex();
            return;
        }

        //报文内容(不包含前十位，前十位在前面已经被读取)
        byte[] data = getBytes(buf);
        String content = new String(data, StandardCharsets.UTF_8);

        //msg=报文前十位长度+报文内容
        String msg = lenstr + content;
        out.add(msg);
        ctx.writeAndFlush(out);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        byte[] body = (byte[]) msg;
        out.writeBytes(body);
    }


    /**
     * 读取ByteBuf字节内容
     *
     * @param buf
     * @return
     */
    private byte[] getBytes(ByteBuf buf) {
        int readablebytes = buf.readableBytes();
        ByteBuf tempBuf = buf.readBytes(readablebytes);
        byte[] data = new byte[readablebytes];//数据大小
        tempBuf.getBytes(0, data);
        return data;
    }

    /**
     * 读取ByteBuf字节内容
     *
     * @param buf
     * @return
     */
    private byte[] getBytes2(ByteBuf buf) {
        byte[] data = new byte[buf.readableBytes()];//数据大小
        buf.getBytes(0, data);
        return data;
    }

    /**
     * 读取ByteBuf中指定长度内容
     *
     * @param buf ByteBuf
     * @param len 读取字节长度
     * @return
     */
    private String getMsgLength(ByteBuf buf, int len) {
        byte[] bytes = new byte[len];
        ByteBuf lengBuf = buf.readBytes(len);
        lengBuf.getBytes(0, bytes);
        return new String(bytes);
    }


    private int toInt(String lenstr) {
        try {
            return Integer.parseInt(lenstr);
        } catch (NumberFormatException e) {
            log.info("报文前十位：{}不是有效数字", lenstr);
            return 0;
        }

    }


}
