package com.flance.web.security.auth;

import com.flance.web.security.user.SecurityAccount;
import com.flance.web.security.user.SecurityAuth;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 鉴权
 * @author jhf
 */
@Slf4j
@Component
public class AuthService {

    @Resource
    AuthenticationManager authenticationManager;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 放行url
     */
    private final static String[] IGNORE_URLS = {
    };

    /**
     * 鉴权
     * @return  返回是有权限
     */
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {

        Object principal = authentication.getPrincipal();
        String methodType = request.getMethod();
        String openId = request.getHeader("openId");
        String url = request.getRequestURI();

        if (!checkLogin(principal, openId)) {
            return false;
        }

        SecurityAccount account = (SecurityAccount) principal;

        return checkPermission(account, url, methodType);
    }

    /**
     * 检查登录状态
     * @param principal     securitySession用户
     * @param openId        openId
     * @return              返回是否登录
     */
    private boolean checkLogin(Object principal, String openId) {

        // 当前已经登录
        if (principal instanceof SecurityAccount) {
            return true;
        }

        // 当前未登录
        if (StringUtils.isNotEmpty(openId)) {
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(openId, openId, AuthorityUtils.commaSeparatedStringToAuthorityList(""));
            Authentication auth = authenticationManager.authenticate(authRequest);
            SecurityContextHolder.getContext().setAuthentication(auth);
            return true;
        }

        return false;
    }

    /**
     * 检查权限
     * @param account   当前登录账号
     * @param url       请求地址
     * @param method    请求方法
     * @return          返回是否有权限
     */
    private boolean checkPermission(SecurityAccount account, String url, String method) {
        log.info("[auth]鉴权，url[{}]", url);
        log.info("[auth]鉴权，开放url校验");
        // 检查放行权限
        if (matchUrl(url)) {
            log.info("[auth]鉴权，开放url校验，成功");
            return true;
        }

        log.info("[auth]鉴权，角色校验");
        // 检查角色
        if (null == account.getRoles() || account.getRoles().size() == 0) {
            log.info("[auth]鉴权，角色校验，失败，没有角色信息");
            return false;
        }

        log.info("[auth]鉴权，url校验");
        // 检查url
        List<SecurityAuth> list = (List) account.getAuthorities();
        Assert.notNull(list, "权限为null!");
        boolean flag = list.stream().anyMatch(auth -> matchUrl(auth.getAuthority(), url));
        log.info("[auth]鉴权，url校验，校验结果[{}]", flag);
        return flag;
    }

    public Boolean matchUrl(String url) {
        return Lists.newArrayList(IGNORE_URLS).stream().anyMatch(item -> antPathMatcher.match(item, url));
    }

    public boolean matchUrl(String reg, String url) {
        return antPathMatcher.match(reg, url);
    }

}
