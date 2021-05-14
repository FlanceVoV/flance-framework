package com.flance.jdbc.jpa.simple.components.user.repository;

import com.flance.jdbc.jpa.simple.components.user.entity.AccountAuthority;
import com.flance.jdbc.jpa.simple.components.user.entity.AccountMenu;
import com.flance.jdbc.jpa.simple.dao.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountAuthorityDao extends BaseRepository<AccountAuthority, Long> {
}
