package com.flance.web.auth.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * 资源权限增强
 * @author jhf
 */
public abstract class BaseAuthority implements GrantedAuthority {

    /**
     * 获取资源类型
     * @return  返回资源类型（按钮、url、菜单...等）
     */
    public abstract String getType();

    /**
     * 是否是公开权限
     * @return  返回是否为公开权限
     */
    public abstract Boolean isOpen();

    /**
     * 获取资源请求方法
     * @return  返回请求方法（可以自定义）
     */
    public abstract String getMethod();

    /**
     * 获取资源路径
     * @return  返回资源请求路径
     */
    public abstract String getUrl();

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        BaseAuthority target = (BaseAuthority) obj;
        return this.getUrl().equals(target.getUrl());
    }
}
