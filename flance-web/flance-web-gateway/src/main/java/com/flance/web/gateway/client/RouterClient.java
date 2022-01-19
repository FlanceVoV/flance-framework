package com.flance.web.gateway.client;

import com.flance.web.utils.web.request.WebRequest;
import com.flance.web.utils.web.response.WebResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

public interface RouterClient {

    /**
     * 获取用户信息
     * @return  返回用户信息
     */
    @ResponseBody
    @GetMapping("/getRouters")
    WebResponse getRouters();

}
