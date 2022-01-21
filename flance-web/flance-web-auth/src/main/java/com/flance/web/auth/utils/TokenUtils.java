package com.flance.web.auth.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flance.web.utils.RsaUtil;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

@Deprecated
public class TokenUtils {

    private final static Logger log = LoggerFactory.getLogger(TokenUtils.class);

    private static final String EXP = "expires_time";

    private static SignatureVerifier verifier;

    static {
        log.info("初始化[JwtUserHelper]");
        if (null == verifier) {
            try {
                RSAPublicKey rsaPublicKey = RsaUtil.getPublicKey("");
                verifier = new RsaVerifier(rsaPublicKey);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("生成[verifier]失败");
            }
        }
    }

    public static String getTokenByRequest(HttpServletRequest request) {
        String token = request.getParameter("access_token");
        if (StringUtils.isEmpty(token)) {
            token = request.getHeader("access_token");
            if (!StringUtils.isEmpty(token)) {
                // 如果有前缀则剔除前缀，不然无法解析
                token = token.replaceFirst("Bearer ", "");
                token = token.replaceFirst("bearer ", "");
            }
        }
        return token;
    }

    public static Boolean isExp(String token) {
        Assert.notNull(token, "token不允许为空！");
        log.info("校验token是否过期{}", token);
        long created = Long.parseLong(decode(token).get(EXP) + "");
        long nowTime = System.currentTimeMillis() / 1000L;
        log.info("校验结果为{}", nowTime > created);
        return nowTime > created;
    }


    /**
     * 验证token
     * @param token
     * @return
     */
    public static Boolean checkToken(String token) {
        Assert.notNull(token, "token不允许为空！");
        Jwt jwt = null;
        try {
            jwt = JwtHelper.decodeAndVerify(token, verifier);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 解析jwt token
     * @param token
     * @return
     */
    public static Map<String, Object> decode(String token) {
        Assert.notNull(token, "token不允许为空！");
        log.info("开始解析token{}", token);
        Jwt e = JwtHelper.decodeAndVerify(token, verifier);
        log.info("decodeAndVerify执行成功");
        String content = e.getClaims();
        JSONObject jsonobject = JSON.parseObject(content);
        if (null != jsonobject) {
            //map对象
            Map<String, Object> data = Maps.newHashMap();
            //循环转换
            for (Map.Entry<String, Object> entry : jsonobject.entrySet()) {
                data.put(entry.getKey(), entry.getValue());
            }
            return data;
        }
        return null;
    }

}
