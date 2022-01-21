package com.flance.web.gateway.client;


import com.flance.web.utils.web.response.WebResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 应用服务
 * @author jhf
 */
public interface AppClient {

    /**
     * 获取服务
     * @return  服务集合
     */
    @ResponseBody
    @GetMapping("/getApps")
    WebResponse getApps();

}
