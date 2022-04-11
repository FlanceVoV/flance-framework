package com.flance.thrid.wx.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class WxOpenIdResponse {

    private String sessionKey;
    private String openId;
    private String unionId;
    private Date loginTime;
    private String wxResultMsg;
    private String wxResultCode;

}
