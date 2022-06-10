package com.flance.tx.common.client.netty.data;

import lombok.Data;

@Data
public class NettyRequest {

    private String messageId;

    private Boolean isHeartBeat;

    private Object data;

}
