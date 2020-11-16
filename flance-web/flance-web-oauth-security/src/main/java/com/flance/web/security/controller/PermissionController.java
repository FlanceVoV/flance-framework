package com.flance.web.security.controller;

import com.flance.web.security.service.AuthService;
import com.flance.web.utils.feign.request.FeignRequest;
import com.flance.web.utils.feign.response.FeignResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 权限认证
 * @author jhf
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Resource
    AuthService authService;

    @PostMapping("/hasPermission")
    public FeignResponse hasPermission(@RequestBody FeignRequest feignRequest) {
        boolean flag;
        try {
            flag = authService.hasPermission(feignRequest.getToken(),
                                                feignRequest.getUrl(),
                                                feignRequest.getMethod(),
                                                feignRequest.getRequestId());

        } catch (Exception e) {
            e.printStackTrace();
            return FeignResponse.getFailed("鉴权失败！reason:[" + e.getMessage() + "]");
        }

        if (flag) {
            return FeignResponse.getSucceed("鉴权成功");
        }
        return FeignResponse.getFailed("没有访问权限！url(" + feignRequest.getMethod() + "):[" + feignRequest.getUrl() + "]");
    }

}
