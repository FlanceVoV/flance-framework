package com.flance.tx.netty.handler;

import com.flance.tx.netty.data.DataUtils;
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

        //记下readerIndex
        buf.markReaderIndex();

        byte[] start = new byte[2];
        buf.readBytes(start);
        String mark = new String(start, StandardCharsets.UTF_8);
        if (!mark.equals(DataUtils.START)) {
            return;
        }

        int msgLength = 10;
        if (buf.readableBytes() < msgLength) {// 不足长度10(开始10位代表整个报文长度位)，无法获取长度
            return;
        }

        if (buf.readableBytes() >= 10485760) {
            ctx.close();
            log.info("错误：报文超出限制{}", buf.readableBytes());
            return;
        }


        //获取前10位表示报文长度的字符串
        byte[] dataLengthBytes = new byte[10];
        buf.readBytes(dataLengthBytes);
        String length = new String(dataLengthBytes, StandardCharsets.UTF_8);
        if (buf.readableBytes() < toInt(length)) {
            //总长度小于数据长度则不处理
            buf.resetReaderIndex();
            return;
        }

        //重置
        buf.resetReaderIndex();

        //报文内容(不包含前十位，前十位在前面已经被读取)
        byte[] data = getBytes(buf, toInt(length) + 10 + 2);

        //msg=报文前十位长度+报文内容
        String msg = new String(data, StandardCharsets.UTF_8);
        if (msg.startsWith(DataUtils.START)) {
            msg = msg.substring(2);
        }
//        if (msg.endsWith(DataUtils.END)) {
//            msg = msg.substring(0, msg.length() - 2);
//        }
        out.add(msg);
        buf.retain();
        buf.release();
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
    private byte[] getBytes(ByteBuf buf, int length) {
        ByteBuf tempBuf = buf.readBytes(length);
        byte[] data = new byte[length];//数据大小
        tempBuf.getBytes(0, data);
        return data;
    }


    private int byte2int24(byte[] bytes, int pos) {
        int size = (int) (bytes[pos] & 0xff);
        size |= ((((int) bytes[pos + 1]) & 0xff) << 8);
        size |= ((((int) bytes[pos + 2]) & 0xff) << 16);
        return size;
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
