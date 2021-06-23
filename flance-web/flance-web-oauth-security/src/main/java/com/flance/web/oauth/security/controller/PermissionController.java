package com.flance.web.oauth.security.controller;

import com.flance.web.oauth.security.auth.AuthService;
import com.flance.web.oauth.security.utils.ErrCodeConstant;
import com.flance.web.utils.web.request.WebRequest;
import com.flance.web.utils.web.response.WebResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 权限认证
 * @author jhf
 */
@RestController
@RequestMapping("/api/permission")
public class PermissionController {

    @Resource
    AuthService authService;

    @PostMapping("/hasPermission")
    public WebResponse hasPermission(@RequestBody WebRequest webRequest) {
        boolean flag;
        try {
            flag = authService.hasPermission(webRequest.getToken(),
                                            webRequest.getUrl(),
                                            webRequest.getMethod(),
                                            webRequest.getRequestId());

        } catch (Exception e) {
            e.printStackTrace();
            return WebResponse.getFailed(ErrCodeConstant.ERROR_AUTH_UNKNOWN, "鉴权失败！reason:[" + e.getMessage() + "]");
        }

        if (flag) {
            return WebResponse.getSucceed(null, "鉴权成功");
        }
        return WebResponse.getFailed(ErrCodeConstant.ERROR_AUTH_ACCESS, "没有访问权限！url(" + webRequest.getMethod() + "):[" + webRequest.getUrl() + "]");
    }

}
