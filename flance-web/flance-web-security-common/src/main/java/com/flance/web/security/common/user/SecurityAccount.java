package com.flance.web.security.common.user;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface SecurityAccount extends UserDetails {

    Long getAccountId();

    SecurityUserInfo getUserInfo();

    List<? extends SecurityRole> getRoles();

}
