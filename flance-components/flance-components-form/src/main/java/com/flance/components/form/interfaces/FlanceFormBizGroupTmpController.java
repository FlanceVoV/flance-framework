package com.flance.components.form.interfaces;

import com.flance.components.form.domain.dform.model.po.FlanceFormBizGrouptmp;
import com.flance.jdbc.jpa.web.controller.BaseWebController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 中间表接口，用于关系绑定
 * @author jhf
 */
@RestController
@RequestMapping("/api/group_tmp")
public class FlanceFormBizGroupTmpController extends BaseWebController<FlanceFormBizGrouptmp, FlanceFormBizGrouptmp, FlanceFormBizGrouptmp, FlanceFormBizGrouptmp, String> {

}
