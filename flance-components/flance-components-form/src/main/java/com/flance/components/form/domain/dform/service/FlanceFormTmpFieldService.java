package com.flance.components.form.domain.dform.service;

import com.flance.components.form.domain.dform.model.po.FlanceFormTmpField;
import com.flance.jdbc.jpa.web.service.IBaseWebDomainService;

import java.util.List;

public interface FlanceFormTmpFieldService extends IBaseWebDomainService<FlanceFormTmpField, FlanceFormTmpField, String> {

    List<FlanceFormTmpField> findAllByTemplateIdAndIsStatic(String templateId, Short isStatic);

    List<FlanceFormTmpField> findAllByTemplateId(String templateId);

}
