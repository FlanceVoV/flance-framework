package com.flance.components.form.interfaces;

import com.flance.components.form.domain.dform.model.po.FlanceFormBizGroup;
import com.flance.components.form.domain.dform.parser.FlanceFormBizGroupParser;
import com.flance.components.form.domain.dform.service.FlanceFormBizGroupService;
import com.flance.jdbc.jpa.web.controller.BaseWebController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 组接口
 * @author jhf
 */
@RestController
@RequestMapping("/api/group")
public class FlanceFormBizGroupController extends BaseWebController<FlanceFormBizGroup, FlanceFormBizGroup, FlanceFormBizGroup, FlanceFormBizGroup, String> {

    private FlanceFormBizGroupService flanceFormBizGroupService;

    private FlanceFormBizGroupParser flanceFormBizGroupParser;

    @Autowired
    public void setFlanceFormBizGroupService(FlanceFormBizGroupService flanceFormBizGroupService) {
        this.flanceFormBizGroupService = flanceFormBizGroupService;
        super.setBaseWebDomainService(flanceFormBizGroupService);
    }

    @Autowired
    public void setFlanceFormBizGroupParser(FlanceFormBizGroupParser flanceFormBizGroupParser) {
        this.flanceFormBizGroupParser = flanceFormBizGroupParser;
        super.setBaseParser(flanceFormBizGroupParser);
    }
}
