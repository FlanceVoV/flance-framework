package com.flance.components.form.domain.dform.repository;

import com.flance.components.form.domain.dform.model.po.FlanceFormTmpTemplate;
import com.flance.jdbc.jpa.dao.BaseDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FlanceFormTmpTemplateDao extends BaseDao<FlanceFormTmpTemplate, String> {

    @Query(" select self.id, self.name, self.code, self.createBy, self.createDate, self.deleted, self.isList, self.limits, self.remark " +
            " from FlanceFormTmpTemplate self " +
            " left join FlanceFormBizGrouptmp tmpGroup on (self.id = tmpGroup.groupFk and self.deleted = 0 ) " +
            " where tmpGroup.groupFk = :groupId " +
            " order by tmpGroup.sort, self.createDate ")
    List<FlanceFormTmpTemplate> findAllByGroupId(@Param("groupId") String groupId);

}
