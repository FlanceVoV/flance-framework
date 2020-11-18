package com.flance.components.form.domain.dform.service.impl;

import com.flance.components.form.domain.dform.model.po.FlanceFormRecMainflowform;
import com.flance.components.form.domain.dform.parser.FlanceFormRecMainflowformParser;
import com.flance.components.form.domain.dform.repository.FlanceFormRecMainflowformDao;
import com.flance.components.form.domain.dform.service.FlanceFormRecMainflowformService;
import com.flance.jdbc.jpa.web.service.BaseWebDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlanceFormRecMainflowformServiceImpl extends BaseWebDomainService<FlanceFormRecMainflowform, FlanceFormRecMainflowform, FlanceFormRecMainflowform, FlanceFormRecMainflowform, String> implements FlanceFormRecMainflowformService {

    private FlanceFormRecMainflowformDao flanceFormRecMainflowformDao;

    private FlanceFormRecMainflowformParser flanceFormRecMainflowformParser;

    @Autowired
    public void setFlanceFormRecMainflowformDao(FlanceFormRecMainflowformDao flanceFormRecMainflowformDao) {
        this.flanceFormRecMainflowformDao = flanceFormRecMainflowformDao;
        super.setBaseDao(flanceFormRecMainflowformDao);
    }

    @Autowired
    public void setFlanceFormRecMainflowformParser(FlanceFormRecMainflowformParser flanceFormRecMainflowformParser) {
        this.flanceFormRecMainflowformParser = flanceFormRecMainflowformParser;
        super.setBaseParser(flanceFormRecMainflowformParser);
    }
}
