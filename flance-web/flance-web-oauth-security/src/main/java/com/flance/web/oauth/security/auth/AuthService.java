package com.flance.web.oauth.security.auth;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.flance.web.auth.utils.Constants;
import com.flance.web.auth.utils.TokenUtils;
import com.flance.web.security.common.SecurityConstant;
import com.flance.web.security.common.exception.AuthException;
import com.flance.web.security.common.user.SecurityAccount;
import com.flance.web.security.common.user.SecurityAccountService;
import com.flance.web.security.common.user.SecurityAuth;
import com.flance.web.utils.RedisUtils;
import com.flance.web.utils.UrlMatchUtil;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author jhf
 **/
@Service
public class AuthService {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private SecurityAccountService userDetailsService;

    /** 直接放行的url **/
    @Value("${flance.security.ignore.url:/login,/token,/token/refresh,/logout}")
    private List<String> passUrls;

    @Resource
    private RedisUtils redisUtils;

    public boolean hasPermission(String token, String uri, String methodType, String urlId) {
        long time1 = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString();
        logger.info("请求标识[{}]，接收到鉴权请求：[{}]", requestId, uri);
        logger.info("请求标识[{}]，请求token：[{}]", requestId, token);
        logger.info("请求标识[{}]，请求method：[{}]", requestId, methodType);
        logger.info("请求标识[{}]，请求requestId(url标识)：[{}]", requestId, urlId);
        // 公开权限
        if (openAuth(requestId, uri, methodType)) {
            logger.info("请求标识[{}]，鉴权成功，公开权限：[{}]", requestId, uri);
            return true;
        }
        // token为空，无权限
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        try {
            // 检查token
            if (!TokenUtils.checkToken(token)) {
                logger.error("请求标识[{}]，鉴权失败，token校验失败：[{}]", requestId, token);
                return false;
            }
            // token过期
            if (TokenUtils.isExp(token)) {
                logger.error("请求标识[{}]，鉴权失败，token过期：[{}]", requestId, token);
                return false;
            }
        }catch (Exception e) {
            logger.error("请求标识[{}]，鉴权失败，token解析失败[{}]", requestId, token);
            return false;
        }
        Map<String, Object> map = TokenUtils.decode(token);
        if (map == null || null == map.get("id")) {
            logger.error("请求标识[{}]，鉴权失败，无法解析token中的用户[{}]", requestId, token);
            throw new AuthException("token失效，法获取用户信息！", SecurityConstant.ERROR_NULL_USER);
        }
        Long id = Long.parseLong(map.get("id").toString());
        return checkAuth(requestId, id, methodType, uri, time1);
    }

    public List<SecurityAuth> findOpenAuths() {
        return null;
    }

    protected Boolean openAuth(String requestId, String uri, String methodType) {
        logger.info("请求标识[{}]，开始公开权限校验", requestId);
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }

        if (UrlMatchUtil.matchUrl(uri, passUrls)) {
            logger.info("请求标识[{}]，检测到白名单权限[{}]", requestId, uri);
            return true;
        }

        List<SecurityAuth> list = findOpenAuths();

        final String requestUrl = uri;

        return null != list && list.stream().anyMatch(auth -> (UrlMatchUtil.matchUrl(requestUrl, auth.getUrl()) || UrlMatchUtil.compUrl(requestUrl, auth.getUrl())));
    }

    protected Boolean checkAuth(String requestId, Long userId, String methodType, String uri, long time1) {
        Assert.notNull(userId, "没有用户");
        String userKey = "user_" + userId;
        List<SecurityAuth> auths = Lists.newArrayList();
        if (redisUtils.get(userKey) != null) {
            String strAuthObj = redisUtils.get(userKey + ":" + Constants.AUTH_INFO);
            auths = JSONObject.parseArray(strAuthObj, SecurityAuth.class);
        } else {
            SecurityAccount account = userDetailsService.getUserByUserId(userId);
            for (int i = 0; i < account.getRoles().size(); i++) {
                if (account.getRoles().get(i).getAuths() == null) {
                    logger.error("请求标识[{}]，鉴权失败，无权限访问[line 133]，AuthServiceImpl.hasPermission总计耗时：" + (System.currentTimeMillis() - time1) + "ms", requestId);
                    return false;
                }
                auths.addAll(account.getRoles().get(i).getAuths());
            }
            redisUtils.add(userKey + ":" + Constants.AUTH_INFO, JSONArray.toJSONString(auths));
        }

        if (null == auths) {
            logger.error("请求标识[{}]，redisKey[{}]取出失败！", requestId, userId + ":" + Constants.AUTH_INFO);
            return false;
        }

        for (SecurityAuth auth : auths) {
            if (auth == null) {
                continue;
            }
            if (UrlMatchUtil.compUrl(uri, auth.getUrl())) {
                long time2 = System.currentTimeMillis();
                logger.info("请求标识[{}]，鉴权成功，AuthServiceImpl.hasPermission总计耗时：" + (time2 - time1) + "ms", requestId);
                return true;
            }
        }
        long time2 = System.currentTimeMillis();
        logger.error("请求标识[{}]，鉴权失败，无权限访问，AuthServiceImpl.hasPermission总计耗时：" + (time2 - time1) + "ms", requestId);
        return false;
    }

}
