package com.flance.components.form.domain.dform.service.impl;

import com.flance.components.form.domain.dform.model.po.FlanceFormTmpTemplate;
import com.flance.components.form.domain.dform.parser.FlanceFormTmpTemplateParser;
import com.flance.components.form.domain.dform.repository.FlanceFormTmpTemplateDao;
import com.flance.components.form.domain.dform.service.FlanceFormTmpTemplateService;
import com.flance.jdbc.jpa.web.service.BaseWebDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlanceFormTmpTemplateServiceImpl extends BaseWebDomainService<FlanceFormTmpTemplate, FlanceFormTmpTemplate, FlanceFormTmpTemplate, FlanceFormTmpTemplate, String> implements FlanceFormTmpTemplateService {

    private FlanceFormTmpTemplateDao flanceFormTmpTemplateDao;

    private FlanceFormTmpTemplateParser flanceFormTmpTemplateParser;

    @Autowired
    public void setFlanceFormTmpTemplateDao(FlanceFormTmpTemplateDao flanceFormTmpTemplateDao) {
        this.flanceFormTmpTemplateDao = flanceFormTmpTemplateDao;
        super.setBaseDao(flanceFormTmpTemplateDao);
    }

    @Autowired
    public void setFlanceFormTmpTemplateParser(FlanceFormTmpTemplateParser flanceFormTmpTemplateParser) {
        this.flanceFormTmpTemplateParser = flanceFormTmpTemplateParser;
        super.setBaseParser(flanceFormTmpTemplateParser);
    }

    @Override
    public List<FlanceFormTmpTemplate> findAllByGroupId(String groupId) {
        return flanceFormTmpTemplateDao.findAllByGroupId(groupId);
    }
}
