package com.flance.components.form.domain.dform.repository;

import com.flance.components.form.domain.dform.model.po.FlanceFormTmpFieldvalue;
import com.flance.jdbc.jpa.dao.BaseDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlanceFormTmpFieldvalueDao extends BaseDao<FlanceFormTmpFieldvalue, String> {

    @Query(" select self.id, self.fieldFk, self.businessFk, self.commonValue, self.objGroup, self.sort, tmpField.sort AS fieldSort from " +
            " FlanceFormTmpFieldvalue self left join FlanceFormBizBusiness bizBusiness on self.businessFk = bizBusiness.id " +
            " left join FlanceFormTmpTemplate template on bizBusiness.templateFk = template.id " +
            " left join FlanceFormTmpTmpfield tmpField on (tmpField.fieldFk = self.fieldFk and tmpField.templateFk = template.id) " +
            " where bizBusiness.groupFk = :groupId and bizBusiness.mainflowFk = :mainflowId and template.id = :templateId ")
    List<FlanceFormTmpFieldvalue> findAllByMainflowIdAndGroupIdAndTmpId(@Param("mainflowId") String mainflowId,
                                                                        @Param("groupId") String groupId,
                                                                        @Param("templateId") String templateId);

}
