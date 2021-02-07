package com.flance.web.oauth.security.controller;


import com.flance.web.auth.model.BaseUser;
import com.flance.web.auth.utils.TokenUtils;
import com.flance.web.oauth.security.service.SecurityUserDetailsService;
import com.flance.web.oauth.security.utils.ErrCodeConstant;
import com.flance.web.utils.web.request.WebRequest;
import com.flance.web.utils.web.response.WebResponse;
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
    public WebResponse getUserInfo(@RequestBody WebRequest webRequest, HttpServletRequest request) {
        String token = TokenUtils.getTokenByRequest(request);
        if (StringUtils.isEmpty(token)) {
            token = webRequest.getToken();
            token = token.replaceFirst("Bearer ", "");
            token = token.replaceFirst("bearer ", "");
        }
        if (StringUtils.isEmpty(token)) {
            return WebResponse.getFailed(ErrCodeConstant.ERROR_TOKEN_NULL, "token为空！");
        }
        try {
            if (TokenUtils.isExp(token)) {
                return WebResponse.getFailed(ErrCodeConstant.ERROR_TOKEN_EXPIRED, "token已过期！");
            }
            Map<String, Object> map = TokenUtils.decode(token);
            String userId = map.get("id").toString();
            BaseUser user = securityUserDetailsService.getUserByUserId(userId);
            return WebResponse.getSucceed(user, "用户信息获取成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponse.getFailed(ErrCodeConstant.ERROR_TOKEN_UNKNOWN, "token解析失败！[ " +e.getMessage() + "]");
        }
    }


}
