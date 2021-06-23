package com.flance.web.auth.jwt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.flance.web.auth.utils.Constants;
import com.flance.web.security.common.user.SecurityAccount;
import com.flance.web.security.common.user.SecurityAuth;
import com.flance.web.utils.RedisUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.security.oauth2.common.*;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * 重写jwt token转换器自定义一些jwt token 属性
 * @author jhf
 */
public class AccessTokenConverter extends JwtAccessTokenConverter {

    @Resource
    private RedisUtils redisUtils;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof SecurityAccount) {
            SecurityAccount baseUser = (SecurityAccount) principal;
            HashMap<String, Object> info = Maps.newHashMap();
            info.put("id", baseUser.getAccountId());
            info.put("username", baseUser.getUsername());
            info.put("expires_time", System.currentTimeMillis() / 1000 + 3600L);
            List<SecurityAuth> auths = Lists.newArrayList();
            if (!baseUser.getRoles().isEmpty()) {
                List<String> roleCodes = Lists.newArrayList();
                baseUser.getRoles().forEach(item -> {
                    roleCodes.add(item.getRoleCode());
                    auths.addAll(item.getAuths());
                });
                info.put("roles", roleCodes);
            }
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
            // 清除数据
            redisUtils.clear("user_" + baseUser.getAccountId());
            // 重置
            redisUtils.add("user_" + baseUser.getAccountId() + ":" + Constants.USER_INFO, JSONObject.toJSONString(baseUser), 3600L);
            redisUtils.add("user_" + baseUser.getAccountId() + ":" + Constants.AUTH_INFO, JSONArray.toJSONString(auths), 3600L);
        }
        return super.enhance(accessToken, authentication);
    }

}
