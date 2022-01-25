package com.flance.web.utils;

import com.flance.web.utils.web.response.WebResponse;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class AssertException extends RuntimeException {

    private String msg;

    private String code;

    private String methodName;

    private String className;

    public enum ErrCode {

        SYS_ERROR("系统错误：未知原因", "-1"),

        SYS_GATEWAY_DECODE_EMPTY_DATA("解密失败，空密文", "000001"),
        SYS_GATEWAY_DECODE_EMPTY_SIGN("解密失败，空签名", "000002"),
        SYS_GATEWAY_DECODE_EMPTY_TIMESTAMP("解密失败，空时间戳", "000003"),

        SYS_GATEWAY_ENCODE_EMPTY_DATA("加密失败，空响应", "000004"),

        SYS_GATEWAY_DECODE_ERROR("解密失败-解密异常", "000005"),
        SYS_GATEWAY_SIGN_CHECK_ERROR("解密失败-验签失败", "000006"),
        SYS_GATEWAY_ENCODE_ERROR("加密失败", "000007"),
        SYS_GATEWAY_ENCODE_BASE64_ERROR("加密失败-编码异常", "000007"),
        ;


        private final String msg;

        private final String code;

        ErrCode(String msg, String code) {
            this.msg = msg;
            this.code = code;
        }

    }

    public static AssertException getNormal(String msg, String code) {
        return AssertException.builder()
                .msg(msg)
                .code(code)
                .build();
    }

    public static AssertException getByEnum(ErrCode errCode) {
        return AssertException.builder()
                .msg(errCode.msg)
                .code(errCode.code)
                .build();
    }

    public WebResponse getResponse() {
        return WebResponse.getFailed(code, msg);
    }

}
