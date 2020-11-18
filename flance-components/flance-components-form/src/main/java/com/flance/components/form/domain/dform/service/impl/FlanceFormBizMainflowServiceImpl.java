package com.flance.components.form.domain.dform.service.impl;

import com.flance.components.form.domain.dform.model.po.FlanceFormBizMainflow;
import com.flance.components.form.domain.dform.parser.FlanceFormBizMainflowParser;
import com.flance.components.form.domain.dform.repository.FlanceFormBizMainflowDao;
import com.flance.components.form.domain.dform.service.FlanceFormBizMainflowService;
import com.flance.jdbc.jpa.web.service.BaseWebDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlanceFormBizMainflowServiceImpl extends BaseWebDomainService<FlanceFormBizMainflow, FlanceFormBizMainflow, FlanceFormBizMainflow, FlanceFormBizMainflow, String> implements FlanceFormBizMainflowService {

    private FlanceFormBizMainflowDao flanceFormBizMainflowDao;

    private FlanceFormBizMainflowParser flanceFormBizMainflowParser;

    @Autowired
    public void setFlanceFormBizMainflowDao(FlanceFormBizMainflowDao flanceFormBizMainflowDao) {
        this.flanceFormBizMainflowDao = flanceFormBizMainflowDao;
        super.setBaseDao(flanceFormBizMainflowDao);
    }

    @Autowired
    public void setFlanceFormBizMainflowParser(FlanceFormBizMainflowParser flanceFormBizMainflowParser) {
        this.flanceFormBizMainflowParser = flanceFormBizMainflowParser;
        super.setBaseParser(flanceFormBizMainflowParser);
    }
}
