package com.flance.components.form.interfaces;

import com.flance.components.form.domain.dform.model.po.FlanceFormBizServiceform;
import com.flance.components.form.domain.dform.parser.FlanceFormBizServiceformParser;
import com.flance.components.form.domain.dform.service.FlanceFormBizServiceformService;
import com.flance.components.form.infrastructure.sync.FlanceFormServiceFormSyncService;
import com.flance.jdbc.jpa.web.controller.BaseWebController;
import com.flance.web.utils.web.response.WebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("/api/service_form")
public class FlanceFormBizServiceFormController extends BaseWebController<FlanceFormBizServiceform, FlanceFormBizServiceform, FlanceFormBizServiceform, FlanceFormBizServiceform, String> {

    private FlanceFormBizServiceformService flanceFormBizServiceformService;

    private FlanceFormBizServiceformParser flanceFormBizServiceformParser;

    @Resource
    private FlanceFormServiceFormSyncService flanceFormServiceFormSyncService;


    @Autowired
    public void setFlanceFormBizServiceformService(FlanceFormBizServiceformService flanceFormBizServiceformService) {
        this.flanceFormBizServiceformService = flanceFormBizServiceformService;
        super.setBaseWebDomainService(flanceFormBizServiceformService);
    }

    @Autowired
    public void setFlanceFormBizServiceformParser(FlanceFormBizServiceformParser flanceFormBizServiceformParser) {
        this.flanceFormBizServiceformParser = flanceFormBizServiceformParser;
        super.setBaseParser(flanceFormBizServiceformParser);
    }


    /**
     *  生成表单json
     */
    @GetMapping("/generateJson/{serviceFormId}")
    public WebResponse generateJson(@PathVariable("serviceFormId")String serviceFormId){
        flanceFormServiceFormSyncService.syncServiceFormRec(serviceFormId);
        return WebResponse.getSucceed(null, "生成成功！");
    }
}
