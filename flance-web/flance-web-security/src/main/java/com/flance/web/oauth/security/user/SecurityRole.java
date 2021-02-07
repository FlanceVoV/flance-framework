package com.flance.web.oauth.security.user;

import java.util.List;

public interface SecurityRole {

    List<? extends SecurityAuth> getAuths();

}
