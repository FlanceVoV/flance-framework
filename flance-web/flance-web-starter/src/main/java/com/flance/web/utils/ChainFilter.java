package com.flance.web.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Slf4j
@WebFilter(urlPatterns = { "/**" }, filterName = "chainFilter")
public class ChainFilter  implements Filter {

    @Resource
    Environment environment;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(request);
        String chain = request.getHeader(RequestConstant.HEADER_REQUEST_CHAIN);
        String logId = request.getHeader(RequestConstant.HEADER_LOG_ID);
        try {
            String name = environment.getProperty("spring.application.name");
            if (null != chain) {
                chain = chain.replaceAll("\\[", "").replaceAll("]", "") + " -> [" + name + "]";
            } else {
                chain = "null";
            }
            log.info("请求链路追踪 ---- 【{}】【{}】", logId, chain + "");
        } catch (Exception e) {
            log.error("加载链路名称失败！[{}]", e.getMessage());
        }
        requestWrapper.addHeader(RequestConstant.HEADER_REQUEST_CHAIN, chain);
        filterChain.doFilter(requestWrapper, servletResponse);
    }

}
