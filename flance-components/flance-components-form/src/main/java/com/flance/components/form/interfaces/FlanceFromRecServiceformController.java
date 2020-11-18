package com.flance.components.form.interfaces;


import com.flance.components.form.domain.dform.service.FlanceFormRecServiceformService;
import com.flance.jdbc.jpa.page.PageResponse;
import com.flance.web.common.request.WebRequest;
import com.flance.web.common.request.WebResponse;
import com.flance.web.common.utils.ResponseBuilder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户端
 * 业务表单记录接口
 * @author jhf
 *
 */
@RestController
@RequestMapping("/api/service_form_rec")
public class FlanceFromRecServiceformController {

    @Resource
    private FlanceFormRecServiceformService flanceFormRecServiceformService;

    /***
     * 根据业务id分页查询业务表单记录
     * @param request 请求封装
     * @return     响应分页
     */
    @PostMapping("/queryByService")
    public WebResponse queryByService(@RequestBody WebRequest request) {
        PageResponse pageResponse = flanceFormRecServiceformService.findPage(request.getParamsMap());
        return ResponseBuilder.getSuccess(WebResponse.builder().pageResult(pageResponse).build());
    }

    /**
     * 根据业务表单id查询业务表单记录
     * @param serviceFormId
     * @return
     */
    @GetMapping("/getOneByServiceFormId/{serviceFormId}")
    public WebResponse getOneByServiceFormId(@PathVariable("serviceFormId") String serviceFormId) {
        return ResponseBuilder.getSuccess(WebResponse.builder().singleResult(flanceFormRecServiceformService.findOneByProp("serviceFormFk", serviceFormId)).build());
    }

}
