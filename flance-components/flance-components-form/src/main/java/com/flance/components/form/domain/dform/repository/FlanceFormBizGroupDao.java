package com.flance.components.form.domain.dform.repository;

import com.flance.components.form.domain.dform.model.po.FlanceFormBizGroup;
import com.flance.jdbc.jpa.dao.BaseDao;
import org.springframework.stereotype.Repository;

@Repository
public interface FlanceFormBizGroupDao extends BaseDao<FlanceFormBizGroup, String> {
}
