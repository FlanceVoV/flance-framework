package com.flance.jdbc.jpa.simple.components.user.service;


import com.flance.jdbc.jpa.simple.common.request.PageResponse;
import com.flance.jdbc.jpa.simple.components.user.entity.Account;
import com.flance.jdbc.jpa.simple.service.IService;

/**
 * 账户接口
 * @author flance
 */
public interface AccountService extends IService<Account, Long, PageResponse<Account>> {
}
