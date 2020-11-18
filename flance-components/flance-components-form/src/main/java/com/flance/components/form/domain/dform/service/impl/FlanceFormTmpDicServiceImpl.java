package com.flance.components.form.domain.dform.service.impl;

import com.flance.components.form.domain.dform.model.po.FlanceFormTmpDic;
import com.flance.components.form.domain.dform.parser.FlanceFormTmpDicParser;
import com.flance.components.form.domain.dform.repository.FlanceFormTmpDicDao;
import com.flance.components.form.domain.dform.service.FlanceFormTmpDicService;
import com.flance.jdbc.jpa.web.service.BaseWebDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlanceFormTmpDicServiceImpl extends BaseWebDomainService<FlanceFormTmpDic, FlanceFormTmpDic, FlanceFormTmpDic, FlanceFormTmpDic, String> implements FlanceFormTmpDicService {

    private FlanceFormTmpDicDao flanceFormTmpDicDao;

    private FlanceFormTmpDicParser flanceFormTmpDicParser;

    @Autowired
    public void setFlanceFormTmpDicDao(FlanceFormTmpDicDao flanceFormTmpDicDao) {
        this.flanceFormTmpDicDao = flanceFormTmpDicDao;
        super.setBaseDao(flanceFormTmpDicDao);
    }

    @Autowired
    public void setFlanceFormTmpDicParser(FlanceFormTmpDicParser flanceFormTmpDicParser) {
        this.flanceFormTmpDicParser = flanceFormTmpDicParser;
        super.setBaseParser(flanceFormTmpDicParser);
    }
}
