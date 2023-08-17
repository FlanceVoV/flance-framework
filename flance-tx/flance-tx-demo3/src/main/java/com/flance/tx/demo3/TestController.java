package com.flance.tx.demo3;

import com.flance.tx.common.netty.TalkVo;
import com.flance.tx.common.utils.GsonUtils;
import com.flance.tx.demo3.handlers.ClientTalkHandler;
import com.flance.tx.netty.data.DataUtils;
import com.flance.tx.netty.data.NettyRequest;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    Channel channel;

    private static final String clientId = "demo3";

    @GetMapping("/sendMsg")
    public void sendMsg(String msg, String roomId, String to) {
        NettyRequest talkReq = new NettyRequest();
        talkReq.setMessageId(UUID.randomUUID().toString());
        talkReq.setRoomId(roomId);
        talkReq.setClientId(clientId);
        talkReq.setHandlerId("talkHandler");

        TalkVo talkVo = new TalkVo();
        talkVo.setFrom(clientId);
        talkVo.setTo(to);
        talkVo.setRoomId(roomId);
        talkVo.setMsg(msg);
        talkReq.setData(GsonUtils.toJSONString(talkVo));
        channel.writeAndFlush(DataUtils.getStr(talkReq).getBytes(StandardCharsets.UTF_8)).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }

    @GetMapping("/readMsg")
    public List<String> readMsg() {
        return ClientTalkHandler.HIS_MSG;
    }



}
