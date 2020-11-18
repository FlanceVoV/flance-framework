package com.flance.components.form.domain.dform.service.impl;

import com.flance.components.form.domain.dform.model.po.FlanceFormTmpTmpfield;
import com.flance.components.form.domain.dform.parser.FlanceFormTmpTmpfieldParser;
import com.flance.components.form.domain.dform.repository.FlanceFormTmpTmpfieldDao;
import com.flance.components.form.domain.dform.service.FlanceFormTmpTmpfieldService;
import com.flance.jdbc.jpa.web.service.BaseWebDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlanceFormTmpTmpfieldServiceImpl extends BaseWebDomainService<FlanceFormTmpTmpfield, FlanceFormTmpTmpfield, FlanceFormTmpTmpfield, FlanceFormTmpTmpfield, String> implements FlanceFormTmpTmpfieldService {

    private FlanceFormTmpTmpfieldDao flanceFormTmpTmpfieldDao;

    private FlanceFormTmpTmpfieldParser flanceFormTmpTmpfieldParser;

    @Autowired
    public void setFlanceFormTmpTmpfieldDao(FlanceFormTmpTmpfieldDao flanceFormTmpTmpfieldDao) {
        this.flanceFormTmpTmpfieldDao = flanceFormTmpTmpfieldDao;
        super.setBaseDao(flanceFormTmpTmpfieldDao);
    }

    @Autowired
    public void setFlanceFormTmpTmpfieldParser(FlanceFormTmpTmpfieldParser flanceFormTmpTmpfieldParser) {
        this.flanceFormTmpTmpfieldParser = flanceFormTmpTmpfieldParser;
        super.setBaseParser(flanceFormTmpTmpfieldParser);
    }
}
