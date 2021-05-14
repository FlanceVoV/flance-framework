package com.flance.jdbc.jpa.simple.components.user.service;

import com.flance.jdbc.jpa.simple.common.request.PageResponse;
import com.flance.jdbc.jpa.simple.components.user.entity.AccountRole;
import com.flance.jdbc.jpa.simple.service.IService;


public interface AccountRoleService extends IService<AccountRole, Long, PageResponse<AccountRole>> {
}
