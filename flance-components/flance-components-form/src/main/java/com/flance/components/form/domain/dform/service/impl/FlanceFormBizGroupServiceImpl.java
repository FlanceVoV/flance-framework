package com.flance.components.form.domain.dform.service.impl;

import com.flance.components.form.domain.dform.model.po.FlanceFormBizGroup;
import com.flance.components.form.domain.dform.parser.FlanceFormBizGroupParser;
import com.flance.components.form.domain.dform.repository.FlanceFormBizGroupDao;
import com.flance.components.form.domain.dform.service.FlanceFormBizGroupService;
import com.flance.jdbc.jpa.web.service.BaseWebDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlanceFormBizGroupServiceImpl extends BaseWebDomainService<FlanceFormBizGroup, FlanceFormBizGroup, FlanceFormBizGroup, FlanceFormBizGroup, String> implements FlanceFormBizGroupService {

    private FlanceFormBizGroupDao flanceFormBizGroupDao;

    private FlanceFormBizGroupParser flanceFormBizGroupParser;

    @Autowired
    public void setFlanceFormBizGroupDao(FlanceFormBizGroupDao flanceFormBizGroupDao) {
        this.flanceFormBizGroupDao = flanceFormBizGroupDao;
        super.setBaseDao(flanceFormBizGroupDao);
    }

    @Autowired
    public void setFlanceFormBizGroupParser(FlanceFormBizGroupParser flanceFormBizGroupParser) {
        this.flanceFormBizGroupParser = flanceFormBizGroupParser;
        super.setBaseParser(flanceFormBizGroupParser);
    }
}
