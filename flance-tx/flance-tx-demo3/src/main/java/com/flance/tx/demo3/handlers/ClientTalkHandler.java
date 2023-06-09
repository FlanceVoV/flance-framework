package com.flance.tx.demo3.handlers;

import com.flance.tx.common.netty.TalkVo;
import com.flance.tx.common.utils.GsonUtils;
import com.flance.tx.netty.biz.IBizHandler;
import com.flance.tx.netty.data.NettyRequest;
import com.flance.tx.netty.data.NettyResponse;
import com.google.common.collect.Lists;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component("clientTalkHandler")
public class ClientTalkHandler implements IBizHandler<NettyRequest, NettyResponse> {

    public static List<String> HIS_MSG = Lists.newArrayList();

    @Override
    public NettyRequest doBizHandler(NettyResponse response, Channel channel) {
        log.info("get talkMsg [{}]", GsonUtils.toJSONString(response));
        TalkVo talkVo = GsonUtils.fromString(response.getData(), TalkVo.class);
        HIS_MSG.add(talkVo.getFrom() + ":" + talkVo.getMsg());
        return null;
    }
}
