package com.flance.components.form.domain.dform.service.impl;

import com.flance.components.form.domain.dform.model.po.FlanceFormRecServiceform;
import com.flance.components.form.domain.dform.parser.FlanceFormRecServiceformParser;
import com.flance.components.form.domain.dform.repository.FlanceFormRecServiceformDao;
import com.flance.components.form.domain.dform.service.FlanceFormRecServiceformService;
import com.flance.jdbc.jpa.web.service.BaseWebDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlanceFormRecServiceformServiceImpl extends BaseWebDomainService<FlanceFormRecServiceform, FlanceFormRecServiceform, FlanceFormRecServiceform, FlanceFormRecServiceform, String> implements FlanceFormRecServiceformService {

    private FlanceFormRecServiceformDao flanceFormRecServiceformDao;

    private FlanceFormRecServiceformParser flanceFormRecServiceformParser;

    @Autowired
    public void setFlanceFormRecServiceformDao(FlanceFormRecServiceformDao flanceFormRecServiceformDao) {
        this.flanceFormRecServiceformDao = flanceFormRecServiceformDao;
        super.setBaseDao(flanceFormRecServiceformDao);
    }

    @Autowired
    public void setFlanceFormRecServiceformParser(FlanceFormRecServiceformParser flanceFormRecServiceformParser) {
        this.flanceFormRecServiceformParser = flanceFormRecServiceformParser;
        super.setBaseParser(flanceFormRecServiceformParser);
    }
}
