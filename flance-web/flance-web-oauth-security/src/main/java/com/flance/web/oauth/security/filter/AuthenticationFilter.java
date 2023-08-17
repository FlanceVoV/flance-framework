package com.flance.web.oauth.security.filter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author jhf
 * @Date 14:56 2020/1/16
 * @Param
 * @return
 *
 * 鉴权过滤器
 *
 **/
public class AuthenticationFilter extends OncePerRequestFilter {

    private static Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);


    /**
     * 返回true表示不进行拦截
     *
     * @param request
     * @return
     * @throws ServletException
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        return true;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        try {
            long time1 = System.currentTimeMillis();



            long time2 = System.currentTimeMillis();
            log.info("AuthenticationFilter.doFilterInternal耗时：" + (time2 - time1) + "ms");
            chain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
