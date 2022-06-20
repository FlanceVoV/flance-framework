package com.flance.tx.netty.data;

import lombok.Data;

@Data
public class NettyResponse {

    private NettyRequest request;

    private String messageId;

    private String roomId;

    private String handlerId;

    private Boolean isHeartBeat;

    private String data;

    private boolean success;

    /**
     * 服务端自己的信息，用于给客户端找的负载后的实际服务，方便事务会话控制
     */
    private ServerData serverData;

}
