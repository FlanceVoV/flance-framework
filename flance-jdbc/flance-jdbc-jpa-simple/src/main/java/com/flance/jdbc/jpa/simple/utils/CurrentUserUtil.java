package com.flance.jdbc.jpa.simple.utils;

import com.flance.jdbc.jpa.simple.components.user.entity.Account;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUserUtil {

    /**
     * 返回当前登录账户信息
     * @return
     */
    public static Account getAccountCurrent() {
        try {
            Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return account;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
