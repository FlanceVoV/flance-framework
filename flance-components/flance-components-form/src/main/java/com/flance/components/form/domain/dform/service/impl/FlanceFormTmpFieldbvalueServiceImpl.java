package com.flance.components.form.domain.dform.service.impl;

import com.flance.components.form.domain.dform.model.po.FlanceFormTmpFieldbvalue;
import com.flance.components.form.domain.dform.parser.FlanceFormTmpFieldbvalueParser;
import com.flance.components.form.domain.dform.repository.FlanceFormTmpFieldbvalueDao;
import com.flance.components.form.domain.dform.service.FlanceFormTmpFieldbvalueService;
import com.flance.jdbc.jpa.web.service.BaseWebDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlanceFormTmpFieldbvalueServiceImpl extends BaseWebDomainService<FlanceFormTmpFieldbvalue, FlanceFormTmpFieldbvalue, FlanceFormTmpFieldbvalue, FlanceFormTmpFieldbvalue, String> implements FlanceFormTmpFieldbvalueService {

    private FlanceFormTmpFieldbvalueDao flanceFormTmpFieldbvalueDao;

    private FlanceFormTmpFieldbvalueParser flanceFormTmpFieldbvalueParser;

    @Autowired
    public void setFlanceFormTmpFieldbvalueDao(FlanceFormTmpFieldbvalueDao flanceFormTmpFieldbvalueDao) {
        this.flanceFormTmpFieldbvalueDao = flanceFormTmpFieldbvalueDao;
        super.setBaseDao(flanceFormTmpFieldbvalueDao);
    }

    @Autowired
    public void setFlanceFormTmpFieldbvalueParser(FlanceFormTmpFieldbvalueParser flanceFormTmpFieldbvalueParser) {
        this.flanceFormTmpFieldbvalueParser = flanceFormTmpFieldbvalueParser;
        super.setBaseParser(flanceFormTmpFieldbvalueParser);
    }
}
