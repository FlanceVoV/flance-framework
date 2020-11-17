package com.flance.components.form.domain.dform.service.impl;

import com.flance.components.form.domain.dform.model.po.FlanceFormTmpTemplate;
import com.flance.components.form.domain.dform.repository.FlanceFormTmpTemplateDao;
import com.flance.components.form.domain.dform.service.FlanceFormTmpTemplateService;
import com.flance.jdbc.jpa.web.service.BaseWebDomainService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlanceFormTmpTemplateServiceImpl extends BaseWebDomainService<FlanceFormTmpTemplate, FlanceFormTmpTemplate, FlanceFormTmpTemplate, FlanceFormTmpTemplate, String> implements FlanceFormTmpTemplateService {

    private FlanceFormTmpTemplateDao flanceFormTmpTemplateDao;

    @Override
    public List<FlanceFormTmpTemplate> findAllByGroupId(String groupId) {
        return flanceFormTmpTemplateDao.findAllByGroupId(groupId);
    }
}
