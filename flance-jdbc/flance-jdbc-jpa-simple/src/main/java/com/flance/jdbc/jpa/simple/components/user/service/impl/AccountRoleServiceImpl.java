package com.flance.jdbc.jpa.simple.components.user.service.impl;

import com.flance.jdbc.jpa.simple.components.user.entity.AccountRole;
import com.flance.jdbc.jpa.simple.components.user.repository.AccountRoleDao;
import com.flance.jdbc.jpa.simple.components.user.service.AccountRoleService;
import com.flance.jdbc.jpa.simple.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountRoleServiceImpl extends BaseService<AccountRole, Long> implements AccountRoleService {

    AccountRoleDao accountRoleDao;

    @Autowired
    public void setAccountRoleDao(AccountRoleDao accountRoleDao) {
        this.accountRoleDao = accountRoleDao;
        super.setBaseDao(accountRoleDao);
    }
}
