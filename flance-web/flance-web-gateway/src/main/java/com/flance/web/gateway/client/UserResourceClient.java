package com.flance.web.gateway.client;

import com.flance.web.utils.web.request.WebRequest;
import com.flance.web.utils.web.response.WebResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户资源服务接口
 * @author jhf
 */
public interface UserResourceClient {


    /**
     * 获取用户信息
     * @param webRequest  请求封装
     * @return  返回用户信息
     */
    @ResponseBody
    @PostMapping("/getUserInfo")
    WebResponse getUserInfo(@RequestBody WebRequest webRequest);
}
