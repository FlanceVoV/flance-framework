package com.flance.web.security.user;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface SecurityAccount extends UserDetails {

    SecurityUserInfo getUserInfo();

    List<SecurityRole> getRoles();

}
