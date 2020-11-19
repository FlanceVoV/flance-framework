package com.flance.web.auth.model;

import com.google.common.collect.Lists;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户基类
 * @author jhf
 */
public abstract class BaseUser implements UserDetails {

    /**
     * 获取唯一表示
     * @return 返回主键
     */
    public abstract String getId();

    /**
     * 获取角色列表
     * @return  返回角色列表
     */
    public abstract List<? extends BaseRole> getRoles();

    /**
     * 获取角色的所有权限
     * @return  返回权限集合
     */
    public List<? extends BaseAuthority> getAllAuthorities() {
        List<? extends BaseRole> roles = getRoles();
        List<BaseAuthority> authorities = Lists.newArrayList();
        roles.forEach(role -> authorities.addAll(role.getAuthorities()));
        return authorities.stream().distinct().collect(Collectors.toList());
    }

}
