package com.flance.components.form.interfaces;

import com.flance.components.form.domain.dform.model.po.FlanceFormTmpTmpfield;
import com.flance.components.form.domain.dform.parser.FlanceFormTmpTmpfieldParser;
import com.flance.components.form.domain.dform.service.FlanceFormTmpTmpfieldService;
import com.flance.jdbc.jpa.web.controller.BaseWebController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/template_field")
public class FlanceFormTemplateFieldController extends BaseWebController<FlanceFormTmpTmpfield, FlanceFormTmpTmpfield, FlanceFormTmpTmpfield, FlanceFormTmpTmpfield, String> {

    private FlanceFormTmpTmpfieldService flanceFormTmpTmpfieldService;

    private FlanceFormTmpTmpfieldParser flanceFormTmpTmpfieldParser;

    @Autowired
    public void setFlanceFormTmpTmpfieldService(FlanceFormTmpTmpfieldService flanceFormTmpTmpfieldService) {
        this.flanceFormTmpTmpfieldService = flanceFormTmpTmpfieldService;
        super.setBaseWebDomainService(flanceFormTmpTmpfieldService);
    }

    @Autowired
    public void setFlanceFormTmpTmpfieldParser(FlanceFormTmpTmpfieldParser flanceFormTmpTmpfieldParser) {
        this.flanceFormTmpTmpfieldParser = flanceFormTmpTmpfieldParser;
        super.setBaseParser(flanceFormTmpTmpfieldParser);
    }
}
