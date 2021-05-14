package com.flance.jdbc.jpa.simple.components.user.service;

import com.flance.jdbc.jpa.simple.common.request.PageResponse;
import com.flance.jdbc.jpa.simple.components.user.entity.AccountMenu;
import com.flance.jdbc.jpa.simple.service.IService;

public interface AccountMenuService extends IService<AccountMenu, Long, PageResponse<AccountMenu>> {
}
