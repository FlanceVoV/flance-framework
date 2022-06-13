package com.flance.tx.common.client.netty.data;

import lombok.Data;

@Data
public class NettyResponse {

    private NettyRequest request;

    private String messageId;

    private Boolean isHeartBeat;

    private String data;

    private boolean success;

}
