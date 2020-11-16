package com.flance.web.auth.model;

import java.util.List;

public abstract class BaseRole {

    /**
     * 获得角色code
     * @return  返回角色code
     */
    public abstract String getCode();

    /**
     * 获取的角色的权限
     * @return  返回角色的权限
     */
    public abstract List<? extends BaseAuthority> getAuthorities();

}
