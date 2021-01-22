package com.flance.web.security.user;

public interface SecurityUserInfoService<T extends SecurityUserInfo, ID> {

    SecurityUserInfo getUserInfo(ID accountId);
}
