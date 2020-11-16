package com.flance.web.security.service;

import com.flance.web.auth.model.BaseUser;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 增强用户明细接口
 * @author jhf
 */
public interface SecurityUserDetailsService<T extends BaseUser> extends UserDetailsService {

    /**
     * 根据用户登录名获取用户信息，不包含角色权限等数据
     * @param userName  用户名
     * @return  返回用户基本信息
     */
    T getUserByUserName(String userName);

    /**
     * 根据用户id获取信息，返回所有信息，包含角色权限
     * @param userId    用户id
     * @return  用户明细
     */
    T getUserByUserId(String userId);

}
