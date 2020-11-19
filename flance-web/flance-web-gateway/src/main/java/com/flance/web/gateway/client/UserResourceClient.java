package com.flance.web.gateway.client;

import com.flance.web.utils.feign.request.FeignRequest;
import com.flance.web.utils.feign.response.FeignResponse;
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
     * @param feignRequest  请求封装
     * @return  返回用户信息
     */
    @ResponseBody
    @PostMapping("/getUserInfo")
    FeignResponse getUserInfo(@RequestBody FeignRequest feignRequest);
}
