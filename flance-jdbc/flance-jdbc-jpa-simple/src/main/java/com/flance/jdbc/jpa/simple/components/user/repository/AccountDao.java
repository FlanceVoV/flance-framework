package com.flance.jdbc.jpa.simple.components.user.repository;

import com.flance.jdbc.jpa.simple.components.user.entity.Account;
import com.flance.jdbc.jpa.simple.dao.BaseRepository;
import org.springframework.stereotype.Repository;

/**
 * 账户dao
 * @author flance
 */
@Repository
public interface AccountDao extends BaseRepository<Account, Long> {
}
