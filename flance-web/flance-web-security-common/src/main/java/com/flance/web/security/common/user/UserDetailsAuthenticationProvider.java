package com.flance.web.security.common.user;

import com.flance.web.security.common.SecurityConstant;
import com.flance.web.security.common.exception.AuthException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 用户信息（执行userDetailsService.loadByUserName之前）
 * @author jhf
 */
@Component
public class UserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Resource
    SecurityAccountService securityAccountService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String s, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        UserDetails loadedUser = securityAccountService.getUserByUserName(s);
        if (loadedUser == null) {
            logger.error("UserDetails为空！");
            throw new AuthException(SecurityConstant.ERROR_NULL_USER, "用户账户错误，找不到账户!");
        }

        if (usernamePasswordAuthenticationToken.getCredentials() == null) {
            logger.error("认证信息为空!");
            throw new AuthException(SecurityConstant.ERROR_NULL_USER, "认证信息为空!");
        }

        String presentedPassword = usernamePasswordAuthenticationToken.getCredentials().toString();

        // 账户密码校验
        if (!passwordEncoder.matches(presentedPassword, loadedUser.getPassword())) {
            logger.error("密码不正确！");
            throw new AuthException(SecurityConstant.ERROR_PASSWORD, "账户或密码不正确!认证失败!");
        }

        loadedUser = securityAccountService.loadUserByUsername(s);

        return loadedUser;
    }
}
