package com.flance.jdbc.jpa.simple.components.user.repository;


import com.flance.jdbc.jpa.simple.components.user.entity.MidAccountRole;
import com.flance.jdbc.jpa.simple.dao.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MidAccountRoleDao extends BaseRepository<MidAccountRole, Long> {

}
