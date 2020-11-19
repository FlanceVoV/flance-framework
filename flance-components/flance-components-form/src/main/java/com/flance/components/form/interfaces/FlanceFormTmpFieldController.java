package com.flance.components.form.interfaces;

import com.flance.components.form.domain.dform.model.po.FlanceFormTmpField;
import com.flance.components.form.domain.dform.parser.FlanceFormTmpFieldParser;
import com.flance.components.form.domain.dform.service.FlanceFormTmpFieldService;
import com.flance.jdbc.jpa.web.controller.BaseWebController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tmp_field")
public class FlanceFormTmpFieldController extends BaseWebController<FlanceFormTmpField, FlanceFormTmpField, FlanceFormTmpField, FlanceFormTmpField, String> {

    private FlanceFormTmpFieldService flanceFormTmpFieldService;

    private FlanceFormTmpFieldParser flanceFormTmpFieldParser;

    @Autowired
    public void setFlanceFormTmpFieldService(FlanceFormTmpFieldService flanceFormTmpFieldService) {
        this.flanceFormTmpFieldService = flanceFormTmpFieldService;
        super.setBaseWebDomainService(flanceFormTmpFieldService);
    }

    @Autowired
    public void setFlanceFormTmpFieldParser(FlanceFormTmpFieldParser flanceFormTmpFieldParser) {
        this.flanceFormTmpFieldParser = flanceFormTmpFieldParser;
        super.setBaseParser(flanceFormTmpFieldParser);
    }
}
