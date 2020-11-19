package com.flance.web.security.controller;


import com.flance.web.auth.model.BaseUser;
import com.flance.web.auth.utils.TokenUtils;
import com.flance.web.security.service.SecurityUserDetailsService;
import com.flance.web.utils.feign.request.FeignRequest;
import com.flance.web.utils.feign.response.FeignResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 用户信息接口
 * @author jhf
 */
@RestController
@RequestMapping("/api/user")
public class UserInfoController {

    @Resource
    SecurityUserDetailsService<? extends BaseUser> securityUserDetailsService;


    @PostMapping("/getUserInfo")
    public FeignResponse getUserInfo(@RequestBody FeignRequest feignRequest, HttpServletRequest request) {
        String token = TokenUtils.getTokenByRequest(request);
        if (StringUtils.isEmpty(token)) {
            token = feignRequest.getToken();
            token = token.replaceFirst("Bearer ", "");
            token = token.replaceFirst("bearer ", "");
        }
        if (StringUtils.isEmpty(token)) {
            return FeignResponse.getFailed("token为空！");
        }
        try {
            if (TokenUtils.isExp(token)) {
                return FeignResponse.getFailed("token已过期！");
            }
            Map<String, Object> map = TokenUtils.decode(token);
            String userId = map.get("id").toString();
            BaseUser user = securityUserDetailsService.getUserByUserId(userId);
            return FeignResponse.getSucceed(user);
        } catch (Exception e) {
            e.printStackTrace();
            return FeignResponse.getFailed("token解析失败！[ " +e.getMessage() + "]");
        }
    }


}
