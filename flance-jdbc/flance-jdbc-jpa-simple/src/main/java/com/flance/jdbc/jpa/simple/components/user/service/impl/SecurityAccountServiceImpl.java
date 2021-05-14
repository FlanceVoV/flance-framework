package com.flance.jdbc.jpa.simple.components.user.service.impl;

import com.flance.jdbc.jpa.simple.components.user.entity.Account;
import com.flance.jdbc.jpa.simple.components.user.entity.AccountRole;
import com.flance.jdbc.jpa.simple.components.user.entity.MidAccountRole;
import com.flance.jdbc.jpa.simple.components.user.service.AccountRoleService;
import com.flance.jdbc.jpa.simple.components.user.service.AccountService;
import com.flance.jdbc.jpa.simple.components.user.service.MidAccountRoleService;
import com.flance.jdbc.jpa.simple.utils.UserTypeConfig;
import com.flance.web.oauth.security.user.SecurityAccount;
import com.flance.web.oauth.security.user.SecurityAccountService;
import com.flance.web.oauth.security.user.SecurityUserInfo;
import com.flance.web.oauth.security.user.SecurityUserInfoService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class SecurityAccountServiceImpl implements SecurityAccountService {

    @Resource
    AccountService accountService;

    @Resource
    MidAccountRoleService midAccountRoleService;

    @Resource
    AccountRoleService accountRoleService;

    @Resource
    ApplicationContext applicationContext;

    @Override
    public SecurityAccount getUserByUserName(String userName) {
        return accountService.findOneByProp("loginName", userName);
    }

    @Override
    public SecurityAccount getUserByUserId(Long userId) {
        return accountService.findOne(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Account account = accountService.findOneByProp("loginName", s);
        buildRoles(account);
        if (StringUtils.isNotEmpty(account.getUserType())) {
            Class<? extends SecurityUserInfoService> beanClazz = UserTypeConfig.getUserService(account.getUserType());
            SecurityUserInfo object = applicationContext.getBean(beanClazz).getUserInfo(account.getId());
            account.setUserInfo(object);
        }
        return account;
    }

    private void buildRoles(Account account) {
        if (null == account.getRoles() || account.getRoles().size() == 0) {
            Map<String, Object> params = Maps.newHashMap();
            params.put("accountId", account.getId());
            List<MidAccountRole> list = midAccountRoleService.findAll(params);
            if (null != list) {
                List<Long> ids = Lists.newArrayList();
                list.forEach(item -> ids.add(item.getRoleId()));
                List<AccountRole> roles = accountRoleService.findByIds(ids);
                account.setRoles(roles);
            }
        }
    }


}
