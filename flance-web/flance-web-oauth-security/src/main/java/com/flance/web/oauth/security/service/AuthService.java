package com.flance.web.oauth.security.service;

import com.flance.web.auth.model.BaseAuthority;

import java.util.List;

/**
 * 权限接口
 * @author jhf
 */
public interface AuthService {

    /**
     * 根据token鉴权
     * @param token     token
     * @param uri       访问的资源
     * @param methodType    请求方式
     * @param urlId         url标识
     * @return
     */
    boolean hasPermission(String token, String uri, String methodType, String urlId);

    /**
     * 查询所有公开权限
     * @return  返回公开权限集合
     */
    List<BaseAuthority> findOpenAuths();

}
