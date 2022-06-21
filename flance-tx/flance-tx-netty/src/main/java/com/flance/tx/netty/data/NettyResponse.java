package com.flance.tx.netty.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class NettyResponse extends NettyData{

    private NettyRequest request;

    private boolean success;

}
