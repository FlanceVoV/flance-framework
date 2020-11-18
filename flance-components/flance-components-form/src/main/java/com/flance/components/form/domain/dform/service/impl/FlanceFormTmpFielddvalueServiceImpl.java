package com.flance.components.form.domain.dform.service.impl;

import com.flance.components.form.domain.dform.model.po.FlanceFormTmpFielddvalue;
import com.flance.components.form.domain.dform.parser.FlanceFormTmpFielddvalueParser;
import com.flance.components.form.domain.dform.repository.FlanceFormTmpFielddvalueDao;
import com.flance.components.form.domain.dform.service.FlanceFormTmpFielddvalueService;
import com.flance.jdbc.jpa.web.service.BaseWebDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlanceFormTmpFielddvalueServiceImpl extends BaseWebDomainService<FlanceFormTmpFielddvalue, FlanceFormTmpFielddvalue, FlanceFormTmpFielddvalue, FlanceFormTmpFielddvalue, String> implements FlanceFormTmpFielddvalueService {

    private FlanceFormTmpFielddvalueDao flanceFormTmpFielddvalueDao;

    private FlanceFormTmpFielddvalueParser flanceFormTmpFielddvalueParser;

    @Autowired
    public void setFlanceFormTmpFielddvalueDao(FlanceFormTmpFielddvalueDao flanceFormTmpFielddvalueDao) {
        this.flanceFormTmpFielddvalueDao = flanceFormTmpFielddvalueDao;
        super.setBaseDao(flanceFormTmpFielddvalueDao);
    }

    @Autowired
    public void setFlanceFormTmpFielddvalueParser(FlanceFormTmpFielddvalueParser flanceFormTmpFielddvalueParser) {
        this.flanceFormTmpFielddvalueParser = flanceFormTmpFielddvalueParser;
        super.setBaseParser(flanceFormTmpFielddvalueParser);
    }
}
