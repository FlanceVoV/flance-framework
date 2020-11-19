package com.flance.components.form.interfaces;


import com.flance.components.form.domain.dform.model.po.FlanceFormBizService;
import com.flance.components.form.domain.dform.parser.FlanceFormBizServiceParser;
import com.flance.components.form.domain.dform.service.FlanceFormBizServiceService;
import com.flance.jdbc.jpa.web.controller.BaseWebController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 业务表API
 * @author jhf
 */
@RestController
@RequestMapping("/api/service")
public class FlanceFromBizServiceController extends BaseWebController<FlanceFormBizService, FlanceFormBizService, FlanceFormBizService, FlanceFormBizService, String> {

    private FlanceFormBizServiceService flanceFormBizServiceService;

    private FlanceFormBizServiceParser flanceFormBizServiceParser;

    @Autowired
    public void setFlanceFormBizServiceService(FlanceFormBizServiceService flanceFormBizServiceService) {
        this.flanceFormBizServiceService = flanceFormBizServiceService;
        super.setBaseWebDomainService(flanceFormBizServiceService);
    }

    @Autowired
    public void setFlanceFormBizServiceParser(FlanceFormBizServiceParser flanceFormBizServiceParser) {
        this.flanceFormBizServiceParser = flanceFormBizServiceParser;
        super.setBaseParser(flanceFormBizServiceParser);
    }
}
