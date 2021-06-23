package com.flance.web.security.common.user;

import java.util.List;

public interface SecurityRole {

    String getRoleCode();

    List<? extends SecurityAuth> getAuths();

}
