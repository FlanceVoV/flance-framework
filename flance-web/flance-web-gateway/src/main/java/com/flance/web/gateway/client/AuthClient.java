package com.flance.web.gateway.client;


import com.flance.web.utils.web.request.WebRequest;
import com.flance.web.utils.web.response.WebResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 鉴权client接口，需继承，并增加feignClient注解
 * @author jhf
 */
public interface AuthClient {

    /**
     * 验证权限
     * @param webRequest 鉴权请求封装
     * @return  返回鉴权结果
     */
    @ResponseBody
    @PostMapping("/hasPermission")
    WebResponse hasPermission(@RequestBody WebRequest webRequest);


}
