package com.flance.components.form.domain.dform.service.impl;

import com.flance.components.form.domain.dform.model.po.FlanceFormBizBusiness;
import com.flance.components.form.domain.dform.parser.FlanceFormBizBusinessParser;
import com.flance.components.form.domain.dform.repository.FlanceFormBizBusinessDao;
import com.flance.components.form.domain.dform.service.FlanceFormBizBusinessService;
import com.flance.jdbc.jpa.web.service.BaseWebDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlanceFormBizBusinessServiceImpl extends BaseWebDomainService<FlanceFormBizBusiness, FlanceFormBizBusiness, FlanceFormBizBusiness, FlanceFormBizBusiness, String> implements FlanceFormBizBusinessService {

    private FlanceFormBizBusinessDao flanceFormBizBusinessDao;

    private FlanceFormBizBusinessParser flanceFormBizBusinessParser;

    @Autowired
    public void setFlanceFormBizBusinessDao(FlanceFormBizBusinessDao flanceFormBizBusinessDao) {
        this.flanceFormBizBusinessDao = flanceFormBizBusinessDao;
        super.setBaseDao(flanceFormBizBusinessDao);
    }

    @Autowired
    public void setFlanceFormBizBusinessParser(FlanceFormBizBusinessParser flanceFormBizBusinessParser) {
        this.flanceFormBizBusinessParser = flanceFormBizBusinessParser;
        super.setBaseParser(flanceFormBizBusinessParser);
    }
}
