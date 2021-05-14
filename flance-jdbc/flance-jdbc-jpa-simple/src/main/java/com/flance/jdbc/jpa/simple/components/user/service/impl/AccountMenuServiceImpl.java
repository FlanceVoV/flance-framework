package com.flance.jdbc.jpa.simple.components.user.service.impl;

import com.flance.jdbc.jpa.simple.components.user.entity.AccountMenu;
import com.flance.jdbc.jpa.simple.components.user.repository.AccountMenuDao;
import com.flance.jdbc.jpa.simple.components.user.service.AccountMenuService;
import com.flance.jdbc.jpa.simple.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountMenuServiceImpl extends BaseService<AccountMenu, Long> implements AccountMenuService {

    AccountMenuDao accountMenuDao;

    @Autowired
    public void setAccountMenuDao(AccountMenuDao accountMenuDao) {
        this.accountMenuDao = accountMenuDao;
        super.setBaseDao(accountMenuDao);
    }
}
