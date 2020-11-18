package com.flance.components.form.domain.dform.service.impl;

import com.flance.components.form.domain.dform.model.po.FlanceFormBizServiceform;
import com.flance.components.form.domain.dform.parser.FlanceFormBizServiceformParser;
import com.flance.components.form.domain.dform.repository.FlanceFormBizServiceformDao;
import com.flance.components.form.domain.dform.service.FlanceFormBizServiceformService;
import com.flance.jdbc.jpa.web.service.BaseWebDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlanceFormBizServiceformServiceImpl extends BaseWebDomainService<FlanceFormBizServiceform, FlanceFormBizServiceform, FlanceFormBizServiceform, FlanceFormBizServiceform, String> implements FlanceFormBizServiceformService {

    private FlanceFormBizServiceformDao flanceFormBizServiceformDao;

    private FlanceFormBizServiceformParser flanceFormBizServiceformParser;

    @Autowired
    public void setFlanceFormBizServiceformDao(FlanceFormBizServiceformDao flanceFormBizServiceformDao) {
        this.flanceFormBizServiceformDao = flanceFormBizServiceformDao;
        super.setBaseDao(flanceFormBizServiceformDao);
    }

    @Autowired
    public void setFlanceFormBizServiceformParser(FlanceFormBizServiceformParser flanceFormBizServiceformParser) {
        this.flanceFormBizServiceformParser = flanceFormBizServiceformParser;
        super.setBaseParser(flanceFormBizServiceformParser);
    }
}
