package com.flance.tx.server.netty.biz;

import com.flance.tx.common.netty.Room;
import com.flance.tx.common.netty.RoomContainer;
import com.flance.tx.common.netty.TalkVo;
import com.flance.tx.common.utils.GsonUtils;
import com.flance.tx.netty.biz.IBizHandler;
import com.flance.tx.netty.data.DataUtils;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component("talkHandler")
public class TalkHandler implements IBizHandler<NettyResponse, NettyRequest> {

    @Override
    public NettyResponse doBizHandler(NettyRequest nettyRequest, Channel channel) {
        NettyResponse response = new NettyResponse();
        response.setTimestamp(System.currentTimeMillis());
        response.setMessageId(nettyRequest.getMessageId());
        response.setClientId(nettyRequest.getClientId());
        TalkVo talkVo = GsonUtils.fromString(nettyRequest.getData(), TalkVo.class);

        Room room = RoomContainer.getRoom(nettyRequest.getRoomId());
        Channel targetChannel = room.getChannelById(talkVo.getTo());
        if (null == targetChannel) {
            response.setSuccess(false);
            response.setData("目标客户端不存在或已经关闭连接");
            return response;
        }
        if (!targetChannel.isOpen() || !targetChannel.isActive()) {
            room.getChannels().remove(talkVo.getTo());
            targetChannel.close();
            return null;
        }
        response.setSuccess(true);
        response.setHandlerId("clientTalkHandler");
        response.setClientId(nettyRequest.getClientId());
        response.setData(GsonUtils.toJSONString(talkVo));
        targetChannel.writeAndFlush(DataUtils.getStr(response).getBytes(StandardCharsets.UTF_8)).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);

        response = new NettyResponse();
        response.setSuccess(true);
        response.setTimestamp(System.currentTimeMillis());
        response.setMessageId(nettyRequest.getMessageId());
        response.setClientId(nettyRequest.getClientId());
        response.setRoomId(nettyRequest.getRoomId());
        return response;
    }
}
