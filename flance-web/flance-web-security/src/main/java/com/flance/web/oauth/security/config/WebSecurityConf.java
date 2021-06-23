package com.flance.web.oauth.security.config;

import com.flance.web.oauth.security.handler.*;
import com.flance.web.security.common.user.UserDetailsAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import javax.annotation.Resource;

/**
 * 继承security适配器，配置登录拦截、登录成功失败及登出的处理器、注入密码生成策略bean
 * @author jhf
 */
@Configuration
@EnableWebSecurity
@EnableRedisHttpSession
public class WebSecurityConf extends WebSecurityConfigurerAdapter {

    @Resource
    UserDetailsAuthenticationProvider userDetailsAuthenticationProvider;

    @Resource
    LoginSuccessHandler loginSuccessHandler;

    @Resource
    LoginFailureHandler loginFailureHandler;

    @Resource
    AccessFailureHandler accessFailureHandler;

    @Resource
    CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Resource
    CustomExpiredSessionStrategy customExpiredSessionStrategy;

    /**
     * 用户认证
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(userDetailsAuthenticationProvider);
    }

    /**
     * 配置web的拦截器
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().and().csrf().disable();
        http.headers().frameOptions().disable();

        http.authorizeRequests()
                .antMatchers("/login", "/api/account/login/**")
                .permitAll()
                .and()
                .authorizeRequests().anyRequest().access("@authService.hasPermission(request, authentication)")
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessFailureHandler)
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
                .permitAll();
        // session配置
        http.sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredSessionStrategy(customExpiredSessionStrategy);
    }

    /**
     * 密码生成策略.
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
