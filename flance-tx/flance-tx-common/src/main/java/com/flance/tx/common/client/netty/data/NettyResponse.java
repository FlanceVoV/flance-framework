package com.flance.tx.common.client.netty.data;

import lombok.Data;

@Data
public class NettyResponse {

    private String messageId;

    private Boolean isHeartBeat;

    private Object data;

    private boolean success;

}
