package com.flance.components.form.domain.dform.service;

import com.flance.components.form.domain.dform.model.po.FlanceFormTmpFieldvalue;
import com.flance.jdbc.jpa.web.service.IBaseWebDomainService;

import java.util.List;

public interface FlanceFormTmpFieldvalueService extends IBaseWebDomainService<FlanceFormTmpFieldvalue, FlanceFormTmpFieldvalue, String> {

    List<FlanceFormTmpFieldvalue> findAllByMainflowIdAndGroupIdAndTmpId(String mainflowId, String groupId, String templateId);

}
