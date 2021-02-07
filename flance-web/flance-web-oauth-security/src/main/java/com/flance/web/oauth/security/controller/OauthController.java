package com.flance.web.oauth.security.controller;

import com.alibaba.fastjson.JSONObject;
import com.flance.web.auth.utils.TokenUtils;
import com.flance.web.oauth.security.utils.ErrCodeConstant;
import com.flance.web.utils.web.response.WebResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.Map;

/**
 * 自定义获取token
 * @author jhf
 */
@RestController
@RequestMapping("/oauth")
public class OauthController {

    private static final String ERROR_TOKEN = "Token was not recognised";

    private static final String EXPIRED_TOKEN = "Token has expired";

    private static Logger log = LoggerFactory.getLogger(OauthController.class);

    @Resource
    TokenEndpoint tokenEndpoint;

    @Resource
    CheckTokenEndpoint checkTokenEndpoint;

    @RequestMapping("/token")
    public WebResponse postAccessTokenWithUserInfo(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        try {
            OAuth2AccessToken accessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
            return WebResponse.getSucceed(accessToken, "access_token获取成功！");
        } catch (InvalidGrantException e) {
            e.printStackTrace();
            return WebResponse.getFailed("010001", "access_token获取失败！");
        }
    }

    @GetMapping("/check_token")
    public WebResponse checkToken(String token) {
        try {
            Map<String, ?> response = checkTokenEndpoint.checkToken(token);
            log.info("响应token：[{}]", JSONObject.toJSONString(response));
        } catch (InvalidTokenException e) {
            e.printStackTrace();
            if (StringUtils.isEmpty(e.getMessage()) && e.getMessage().equals(ERROR_TOKEN)) {
                return WebResponse.getFailed(ErrCodeConstant.ERROR_TOKEN_RECOGNISED, "access_token校验失败");
            } else if (StringUtils.isEmpty(e.getMessage()) && e.getMessage().equals(EXPIRED_TOKEN)) {
                return WebResponse.getFailed(ErrCodeConstant.ERROR_TOKEN_EXPIRED, "access_token已经过期");
            } else {
                return WebResponse.getFailed(ErrCodeConstant.ERROR_TOKEN_UNKNOWN, "access_token校验失败");
            }
        }
        log.info("解析token：[{}]", JSONObject.toJSONString(TokenUtils.decode(token)));
        return WebResponse.getSucceed(null, "access_token获取成功！");
    }

}
