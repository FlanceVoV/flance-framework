package com.flance.components.form.domain.dform.service;

import com.flance.components.form.domain.dform.model.po.FlanceFormTmpTemplate;
import com.flance.jdbc.jpa.web.service.IBaseWebDomainService;

import java.util.List;

public interface FlanceFormTmpTemplateService extends IBaseWebDomainService<FlanceFormTmpTemplate, FlanceFormTmpTemplate, String> {

    List<FlanceFormTmpTemplate> findAllByGroupId(String groupId);

}
