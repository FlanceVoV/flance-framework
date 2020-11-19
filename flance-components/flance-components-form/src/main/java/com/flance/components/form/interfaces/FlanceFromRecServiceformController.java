package com.flance.components.form.interfaces;


import com.flance.components.form.domain.dform.model.po.FlanceFormRecServiceform;
import com.flance.components.form.domain.dform.parser.FlanceFormRecServiceformParser;
import com.flance.components.form.domain.dform.service.FlanceFormRecServiceformService;
import com.flance.jdbc.jpa.web.controller.BaseWebController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户端
 * 业务表单记录接口
 * @author jhf
 *
 */
@RestController
@RequestMapping("/api/service_form_rec")
public class FlanceFromRecServiceformController extends BaseWebController<FlanceFormRecServiceform, FlanceFormRecServiceform, FlanceFormRecServiceform, FlanceFormRecServiceform, String> {

    private FlanceFormRecServiceformService flanceFormRecServiceformService;

    private FlanceFormRecServiceformParser flanceFormRecServiceformParser;

    @Autowired
    public void setFlanceFormRecServiceformService(FlanceFormRecServiceformService flanceFormRecServiceformService) {
        this.flanceFormRecServiceformService = flanceFormRecServiceformService;
        super.setBaseWebDomainService(flanceFormRecServiceformService);
    }

    @Autowired
    public void setFlanceFormRecServiceformParser(FlanceFormRecServiceformParser flanceFormRecServiceformParser) {
        this.flanceFormRecServiceformParser = flanceFormRecServiceformParser;
        super.setBaseParser(flanceFormRecServiceformParser);
    }
}
