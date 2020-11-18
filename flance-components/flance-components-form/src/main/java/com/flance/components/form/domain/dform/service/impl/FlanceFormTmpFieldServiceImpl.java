package com.flance.components.form.domain.dform.service.impl;

import com.flance.components.form.domain.dform.model.po.FlanceFormTmpField;
import com.flance.components.form.domain.dform.parser.FlanceFormTmpFieldParser;
import com.flance.components.form.domain.dform.repository.FlanceFormTmpFieldDao;
import com.flance.components.form.domain.dform.service.FlanceFormTmpFieldService;
import com.flance.jdbc.jpa.web.service.BaseWebDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlanceFormTmpFieldServiceImpl extends BaseWebDomainService<FlanceFormTmpField, FlanceFormTmpField, FlanceFormTmpField, FlanceFormTmpField, String> implements FlanceFormTmpFieldService {

    private FlanceFormTmpFieldDao flanceFormTmpFieldDao;

    private FlanceFormTmpFieldParser flanceFormTmpFieldParser;

    @Autowired
    public void setFlanceFormTmpFieldDao(FlanceFormTmpFieldDao flanceFormTmpFieldDao) {
        this.flanceFormTmpFieldDao = flanceFormTmpFieldDao;
        super.setBaseDao(flanceFormTmpFieldDao);
    }

    @Autowired
    public void setFlanceFormTmpFieldParser(FlanceFormTmpFieldParser flanceFormTmpFieldParser) {
        this.flanceFormTmpFieldParser = flanceFormTmpFieldParser;
        super.setBaseParser(flanceFormTmpFieldParser);
    }

    @Override
    public List<FlanceFormTmpField> findAllByTemplateIdAndIsStatic(String templateId, Short isStatic) {
        return flanceFormTmpFieldDao.findAllByTemplateIdAndIsStatic(templateId, isStatic);
    }

    @Override
    public List<FlanceFormTmpField> findAllByTemplateId(String templateId) {
        return flanceFormTmpFieldDao.findAllByTemplateId(templateId);
    }
}
