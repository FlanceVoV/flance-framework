package com.flance.components.form.domain.dform.service.impl;

import com.flance.components.form.domain.dform.model.po.FlanceFormBizService;
import com.flance.components.form.domain.dform.parser.FlanceFormBizServiceParser;
import com.flance.components.form.domain.dform.repository.FlanceFormBizServiceDao;
import com.flance.components.form.domain.dform.service.FlanceFormBizServiceService;
import com.flance.jdbc.jpa.web.service.BaseWebDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlanceFormBizServiceServiceImpl extends BaseWebDomainService<FlanceFormBizService, FlanceFormBizService, FlanceFormBizService, FlanceFormBizService, String> implements FlanceFormBizServiceService {

    private FlanceFormBizServiceDao flanceFormBizServiceDao;

    private FlanceFormBizServiceParser flanceFormBizServiceParser;

    @Autowired
    public void setFlanceFormBizServiceDao(FlanceFormBizServiceDao flanceFormBizServiceDao) {
        this.flanceFormBizServiceDao = flanceFormBizServiceDao;
        super.setBaseDao(flanceFormBizServiceDao);
    }

    @Autowired
    public void setFlanceFormBizServiceParser(FlanceFormBizServiceParser flanceFormBizServiceParser) {
        this.flanceFormBizServiceParser = flanceFormBizServiceParser;
        super.setBaseParser(flanceFormBizServiceParser);
    }
}
