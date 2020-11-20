package com.flance.web.common.utils;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;

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
            try {
                currentUser = gson.fromJson(URLDecoder.decode(headerUser, "UTF-8"), userClass);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            currentUser = (User) sessionUser;
        }
        return currentUser;
    }

    public static void setSessionUser(HttpServletRequest request, Object user) {
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("user_info", user);
    }

}
