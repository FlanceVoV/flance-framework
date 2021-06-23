package com.flance.web.security.common.user;

import org.springframework.security.core.GrantedAuthority;

public interface SecurityAuth extends GrantedAuthority {

    String getUrl();

}
