package com.flance.web.utils.web.request;

import lombok.Data;

@Data
public class GatewayRequest {

    /**
     * 签名 = sign(data + timestamp)
     */
    private String sign;

    /**
     * base64字符串 （请求数据 -> jsonStr 加密 -> base64字符串）
     */
    private String data;

    private Long timestamp;

}
