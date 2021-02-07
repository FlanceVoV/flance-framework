package com.flance.web.oauth.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * security url 配置
 * @author jhf
 */
@Component
public class SecurityUrlConfig {


    private static String urlContext;

    private static String[] filterUrls;

    @Value("${server.servlet.context-path}")
    public void setUrlContext(String urlContext) {
        SecurityUrlConfig.urlContext = urlContext;
    }

    @Value("${flance.security.filter.urls}")
    public void setFilterUrls(String[] filterUrls) {
        SecurityUrlConfig.filterUrls = filterUrls;
    }

    public static String getUrlContext() {
        return urlContext;
    }

    public static String[] getFilterUrls() {
        return filterUrls;
    }


}
