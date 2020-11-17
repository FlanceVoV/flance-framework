package com.flance.components.form.domain.dform.repository;

import com.flance.components.form.domain.dform.model.po.FlanceFormTmpField;
import com.flance.jdbc.jpa.dao.BaseDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlanceFormTmpFieldDao extends BaseDao<FlanceFormTmpField, String> {

    @Query(" select self.id, self.name, self.code, self.fieldType, self.isstatic, self.isBigText, self.dicId, self.createBy, self.createDate, self.deleted, self.tableCode " +
            " from FlanceFormTmpField self " +
            " left join FlanceFormTmpTmpfield tmpField on (tmpField.fieldFk = self.id and self.deleted = 0) " +
            " where tmpField.templateFk = :templateId and self.isstatic = :isStatic ")
    List<FlanceFormTmpField> findAllByTemplateIdAndIsStatic(@Param("templateId") String templateId, @Param("isStatic") Short isStatic);

    @Query("  select self.id, self.name, self.code, self.fieldType, self.isstatic, self.isBigText, self.dicId, self.createBy, self.createDate, self.deleted, self.tableCode " +
            " from FlanceFormTmpField self " +
            " left join FlanceFormTmpTmpfield tmpField on (self.id = tmpField.fieldFk and self.deleted = 0) " +
            " where tmpField.templateFk = :templateId " +
            " order by tmpField.sort")
    List<FlanceFormTmpField> findAllByTemplateId(@Param("templateId") String templateId);
}
