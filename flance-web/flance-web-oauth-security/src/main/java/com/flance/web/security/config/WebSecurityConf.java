package com.flance.web.security.config;

import com.flance.web.security.service.UserDetailsAuthenticationImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.annotation.Resource;

/**
 * 继承security适配器，配置登录拦截、登录成功失败及登出的处理器、注入密码生成策略bean
 * @author jhf
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConf extends WebSecurityConfigurerAdapter {

    @Resource
    UserDetailsAuthenticationImpl userDetailsAuthentication;

    @Value("${loginPage:/login}")
    private String loginPage;

    /**
     * 用户认证
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(userDetailsAuthentication);
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

        http.authorizeRequests().antMatchers("/oauth/**","/api/permission/**","/api/user/**").permitAll()
                .and()
                .formLogin()
//                .loginPage("/oauth/token")
                .loginProcessingUrl(loginPage)
                .permitAll();
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
