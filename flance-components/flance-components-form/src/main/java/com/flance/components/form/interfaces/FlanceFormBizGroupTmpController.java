package com.flance.components.form.interfaces;

import com.flance.components.form.domain.dform.model.po.FlanceFormBizGrouptmp;
import com.flance.components.form.domain.dform.parser.FlanceFormBizGroupTmpParser;
import com.flance.components.form.domain.dform.service.FlanceFormBizGroupTmpService;
import com.flance.jdbc.jpa.web.controller.BaseWebController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 中间表接口，用于关系绑定
 * @author jhf
 */
@RestController
@RequestMapping("/api/group_tmp")
public class FlanceFormBizGroupTmpController extends BaseWebController<FlanceFormBizGrouptmp, FlanceFormBizGrouptmp, FlanceFormBizGrouptmp, FlanceFormBizGrouptmp, String> {

    private FlanceFormBizGroupTmpService flanceFormBizGroupTmpService;

    private FlanceFormBizGroupTmpParser flanceFormBizGroupTmpParser;

    @Autowired
    public void setFlanceFormBizGroupTmpService(FlanceFormBizGroupTmpService flanceFormBizGroupTmpService) {
        this.flanceFormBizGroupTmpService = flanceFormBizGroupTmpService;
        super.setBaseWebDomainService(flanceFormBizGroupTmpService);
    }

    @Autowired
    public void setFlanceFormBizGroupTmpParser(FlanceFormBizGroupTmpParser flanceFormBizGroupTmpParser) {
        this.flanceFormBizGroupTmpParser = flanceFormBizGroupTmpParser;
        super.setBaseParser(flanceFormBizGroupTmpParser);
    }
}
