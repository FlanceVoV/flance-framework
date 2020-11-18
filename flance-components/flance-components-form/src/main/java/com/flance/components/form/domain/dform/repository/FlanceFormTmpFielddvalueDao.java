package com.flance.components.form.domain.dform.repository;

import com.flance.components.form.domain.dform.service.FlanceFormTmpFielddvalueService;
import com.flance.jdbc.jpa.dao.BaseDao;
import org.springframework.stereotype.Repository;

@Repository
public interface FlanceFormTmpFielddvalueDao extends BaseDao<FlanceFormTmpFielddvalueService, String> {
}
