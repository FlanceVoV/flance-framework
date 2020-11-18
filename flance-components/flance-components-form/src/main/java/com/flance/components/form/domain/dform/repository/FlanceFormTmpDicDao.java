package com.flance.components.form.domain.dform.repository;

import com.flance.components.form.domain.dform.model.po.FlanceFormTmpDic;
import com.flance.jdbc.jpa.dao.BaseDao;
import org.springframework.stereotype.Repository;

@Repository
public interface FlanceFormTmpDicDao extends BaseDao<FlanceFormTmpDic, String> {
}
