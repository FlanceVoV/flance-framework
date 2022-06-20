package com.flance.tx.netty.data;

import lombok.Data;

@Data
public class NettyRequest {

    private String messageId;

    private String roomId;

    private String handlerId;

    private Boolean isHeartBeat;

    private String data;

    /**
     * 客户端自己的信息
     */
    private ServerData serverData;

}
