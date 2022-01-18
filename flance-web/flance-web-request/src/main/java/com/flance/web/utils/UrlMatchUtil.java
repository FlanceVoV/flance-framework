package com.flance.web.utils;

import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.regex.Pattern;

/**
 * url工具类
 * @author jhf
 */
public class UrlMatchUtil {

    public static Boolean matchUrl(String url, List<String> urls) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (String str : urls) {
            if (antPathMatcher.match(str, url)) {
                return true;
            }
        }
        return false;
    }

    public static Boolean matchUrl(String url, String templateUrl) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        return antPathMatcher.match(templateUrl, url);
    }

    /**
     * 用于处理参数的url
     * 例如：/test/{param}
     * TODO 优化为递归处理，每次第一个参数，直至处理完成所有参数，以支持多参数
     */
    public static boolean compUrl(String uri, String templateUrl) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        if (templateUrl.indexOf("{") > 0 && templateUrl.indexOf("}") > 0) {
            templateUrl = templateUrl.replaceAll("\\{(.*?)\\}", ".+");
            String regEx = "^" + templateUrl + "$";
            return Pattern.compile(regEx).matcher(uri).find();
        }
        return antPathMatcher.match(templateUrl, uri);
    }


}
