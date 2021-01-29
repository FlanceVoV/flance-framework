package com.flance.web.security.intercept;

import com.flance.web.security.config.SecurityUrlConfig;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * requestBody复读filter
 * @author jhf
 */
@Slf4j
@Component
public class HttpServletRequestFilter implements Filter {



    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String url = ((HttpServletRequest) servletRequest).getRequestURI();
        log.info("filter-请求url[{}]", url);
        // 去除servlet-context-path
        if (matchUrl(url.replaceFirst(SecurityUrlConfig.getUrlContext(), ""))) {
            ParamsRequestWrapper requestWrapper = new ParamsRequestWrapper((HttpServletRequest) servletRequest);
            filterChain.doFilter(requestWrapper, servletResponse);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }

    public Boolean matchUrl(String url) {
        return Lists.newArrayList(SecurityUrlConfig.getFilterUrls()).stream().anyMatch(item -> antPathMatcher.match(item, url));
    }

}
