package com.flance.tx.common.netty;

import lombok.Data;

@Data
public class TalkVo {

    private String roomId;

    private Long sendTime;

    private Long receiveTime;

    private String fromName;

    private String from;

    private String toName;

    private String to;

    private String msg;

}
