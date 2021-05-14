package com.flance.jdbc.jpa.simple.components.user.service;

import com.flance.jdbc.jpa.simple.common.request.PageResponse;
import com.flance.jdbc.jpa.simple.components.user.entity.AccountAuthority;
import com.flance.jdbc.jpa.simple.service.IService;

public interface AccountAuthorityService extends IService<AccountAuthority, Long, PageResponse<AccountAuthority>> {
}
