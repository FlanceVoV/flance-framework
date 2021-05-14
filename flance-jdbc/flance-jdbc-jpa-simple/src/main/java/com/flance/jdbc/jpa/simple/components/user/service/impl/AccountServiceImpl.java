package com.flance.jdbc.jpa.simple.components.user.service.impl;

import com.flance.jdbc.jpa.simple.components.user.entity.Account;
import com.flance.jdbc.jpa.simple.components.user.repository.AccountDao;
import com.flance.jdbc.jpa.simple.components.user.service.AccountService;
import com.flance.jdbc.jpa.simple.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 账户实现
 * @author jhf
 */
@Service
public class AccountServiceImpl extends BaseService<Account, Long> implements AccountService {

    AccountDao accountDao;

    @Autowired
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
        super.setBaseDao(accountDao);
    }
}
