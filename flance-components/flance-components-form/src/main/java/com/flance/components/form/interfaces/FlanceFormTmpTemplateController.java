package com.flance.components.form.interfaces;

import com.flance.components.form.domain.dform.model.po.FlanceFormTmpTemplate;
import com.flance.components.form.domain.dform.parser.FlanceFormTmpTemplateParser;
import com.flance.components.form.domain.dform.service.FlanceFormTmpTemplateService;
import com.flance.jdbc.jpa.web.controller.BaseWebController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tmp_template")
public class FlanceFormTmpTemplateController extends BaseWebController<FlanceFormTmpTemplate, FlanceFormTmpTemplate, FlanceFormTmpTemplate, FlanceFormTmpTemplate, String> {

    private FlanceFormTmpTemplateService flanceFormTmpTemplateService;

    private FlanceFormTmpTemplateParser flanceFormTmpTemplateParser;

    @Autowired
    public void setFlanceFormTmpTemplateService(FlanceFormTmpTemplateService flanceFormTmpTemplateService) {
        this.flanceFormTmpTemplateService = flanceFormTmpTemplateService;
        super.setBaseWebDomainService(flanceFormTmpTemplateService);
    }

    @Autowired
    public void setFlanceFormTmpTemplateParser(FlanceFormTmpTemplateParser flanceFormTmpTemplateParser) {
        this.flanceFormTmpTemplateParser = flanceFormTmpTemplateParser;
        super.setBaseParser(flanceFormTmpTemplateParser);
    }
}
