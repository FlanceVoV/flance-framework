package com.flance.web.oauth.security.user;

public interface SecurityUserInfoService<T extends SecurityUserInfo, ID> {

    SecurityUserInfo getUserInfo(ID accountId);
}
