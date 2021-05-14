package com.flance.jdbc.jpa.simple.components.user.service.impl;


import com.flance.jdbc.jpa.simple.components.user.entity.MidAccountRole;
import com.flance.jdbc.jpa.simple.components.user.repository.MidAccountRoleDao;
import com.flance.jdbc.jpa.simple.components.user.service.MidAccountRoleService;
import com.flance.jdbc.jpa.simple.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MidAccountRoleServiceImpl extends BaseService<MidAccountRole, Long> implements MidAccountRoleService {

    MidAccountRoleDao midAccountRoleDao;

    @Autowired
    public void setMidAccountRoleDao(MidAccountRoleDao midAccountRoleDao) {
        this.midAccountRoleDao = midAccountRoleDao;
        super.setBaseDao(midAccountRoleDao);
    }
}
