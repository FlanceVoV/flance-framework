package com.flance.jdbc.jpa.simple.components.user.service.impl;

import com.flance.jdbc.jpa.simple.components.user.entity.AccountAuthority;
import com.flance.jdbc.jpa.simple.components.user.repository.AccountAuthorityDao;
import com.flance.jdbc.jpa.simple.components.user.service.AccountAuthorityService;
import com.flance.jdbc.jpa.simple.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountAuthorityServiceImpl extends BaseService<AccountAuthority, Long> implements AccountAuthorityService {

    AccountAuthorityDao accountAuthorityDao;

    @Autowired
    public void setAccountAuthorityDao(AccountAuthorityDao accountAuthorityDao) {
        this.accountAuthorityDao = accountAuthorityDao;
        super.setBaseDao(accountAuthorityDao);
    }
}
