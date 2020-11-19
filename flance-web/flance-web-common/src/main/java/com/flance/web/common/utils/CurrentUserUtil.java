package com.flance.web.common.utils;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户会话工具
 * @author jhf
 */
public class CurrentUserUtil {

    public static <User> User getUser(HttpServletRequest request, Class<User> userClass) {
        User currentUser = null;
        Object sessionUser = request.getSession().getAttribute("user_info");
        String headerUser = request.getHeader("user_info");
        if (null == sessionUser) {
            Gson gson = new Gson();
            currentUser = gson.fromJson(headerUser, userClass);
        } else {
            currentUser = (User) sessionUser;
        }
        return currentUser;
    }

}
