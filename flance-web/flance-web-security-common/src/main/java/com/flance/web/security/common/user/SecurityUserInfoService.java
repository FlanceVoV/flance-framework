package com.flance.web.security.common.user;

public interface SecurityUserInfoService<T extends SecurityUserInfo, ID> {

    SecurityUserInfo getUserInfo(ID accountId);
}
