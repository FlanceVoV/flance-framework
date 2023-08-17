package com.flance.web.oauth.security.service;

import com.flance.web.oauth.security.exception.AuthException;
import com.flance.web.security.common.user.SecurityAccountService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * 集成用户认证服务，重写认证方法
 * @author jhf
 */
@Component
public class UserDetailsAuthenticationImpl extends AbstractUserDetailsAuthenticationProvider {

    @Resource
    SecurityAccountService userDetailsService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String s, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        UserDetails loadedUser = userDetailsService.getUserByUserName(s);
        if (loadedUser == null) {
            logger.debug("UserDetails为空！");
            throw new AuthException("认证信息为空!");
        }

        boolean flag = true;

        if (usernamePasswordAuthenticationToken.getCredentials() == null) {
            logger.debug("认证信息为空!");
            throw new AuthException("认证信息为空!");
        }

        String presentedPassword = usernamePasswordAuthenticationToken.getCredentials().toString();

        // 账户密码校验
        if (!passwordEncoder.matches(presentedPassword, loadedUser.getPassword())) {
            logger.debug("密码不正确！");
            flag = false;
        }

        // 其他认证信息校验
        if (false) {
            flag = false;
        }

        // 认证失败
        if (!flag) {
            throw new AuthException("账号密码不正确!认证失败");
        }

        loadedUser = userDetailsService.loadUserByUsername(s);

        return loadedUser;
    }
}
