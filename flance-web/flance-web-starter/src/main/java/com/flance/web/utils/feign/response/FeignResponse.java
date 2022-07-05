package com.flance.web.utils.feign.response;

/**
 * feign响应
 * 推荐所有接口统一使用WebResponse
 * @author jhf
 */
@Deprecated
public class FeignResponse {

    private Boolean success;

    private String msg;

    private String code;

    private Object data;

    public FeignResponse(boolean success, String code, String msg, Object data) {
        this.code = code;
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public FeignResponse(Object data) {
        this.data = data;
    }

    public FeignResponse() {}

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static FeignResponse getSucceed(Object data) {
        FeignResponse feignResponse = new FeignResponse(data);
        feignResponse.setCode("000000");
        feignResponse.setSuccess(true);
        feignResponse.setMsg("请求成功");
        return feignResponse;
    }

    public static FeignResponse getFailed(String msg) {
        FeignResponse feignResponse = new FeignResponse(null);
        feignResponse.setCode("-1");
        feignResponse.setSuccess(false);
        feignResponse.setMsg(msg);
        return feignResponse;
    }

    @Override
    public String toString() {
        String resultStr = "空响应";
        if (null != data) {
            resultStr = data.toString();
        }
        return "{code:" + this.code + ", success:" + this.success + ", msg:" + this.msg + ", data:" + resultStr + "}";
    }
}
