package com.flance.tx.netty.data;

import lombok.Data;

@Data
public class NettyData {

    protected String messageId;

    protected String roomId;

    protected String handlerId;

    protected Boolean isHeartBeat;

    protected String data;

    /**
     * 服务端自己的信息，用于给客户端找的负载后的实际服务，方便事务会话控制
     */
    protected ServerData serverData;

}
