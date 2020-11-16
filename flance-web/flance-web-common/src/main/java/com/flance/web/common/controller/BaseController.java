package com.flance.web.common.controller;


import com.flance.web.common.request.WebRequest;
import com.flance.web.common.request.WebResponse;
import com.flance.web.common.service.IService;
import com.flance.web.common.utils.ResponseBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * rest api 基础实现
 * @author jhf
 * @param <DTO> 传输对象
 * @param <VO>  视图对象
 */
public abstract class BaseController<DTO, VO, ID, PAGE> implements IBaseController<DTO, VO, ID, PAGE> {

    IService<DTO, ID, PAGE> baseService;

    public void setBaseService(IService<DTO, ID, PAGE> baseService) {
        this.baseService = baseService;
    }

    @Override
    @PostMapping("/add")
    public WebResponse add(@RequestBody WebRequest<DTO, ID> request) {
        DTO dto = baseService.save(request.getSingleParam());
        return ResponseBuilder.getSuccess(WebResponse.builder().singleResult(dto).build());
    }

    @Override
    @PatchMapping("/update")
    public WebResponse update(@RequestBody WebRequest<DTO, ID> request) {
        DTO dto = request.getSingleParam();
        ID id = request.getId();
        baseService.updateNotNull(dto, id);
        return ResponseBuilder.getSuccess(WebResponse.builder().singleResult(dto).build());
    }

    @Override
    @DeleteMapping("/delete")
    public WebResponse delete(@RequestBody WebRequest<DTO, ID> request) {
        ID id = request.getId();
        baseService.delete(id);
        return ResponseBuilder.getSuccess(WebResponse.builder().build());
    }

    @Override
    @DeleteMapping("/delete/ids")
    public WebResponse deleteBatch(@RequestBody WebRequest<DTO, ID> request) {
        List<ID> ids = request.getIds();
        baseService.deleteByIds(ids);
        return ResponseBuilder.getSuccess(WebResponse.builder().build());
    }

    @Override
    @PostMapping("/page")
    public WebResponse page(@RequestBody WebRequest<DTO, ID> request) {
        Map<String, Object> searchMap = request.getParamsMap();
        PAGE page = baseService.findPage(searchMap);
        return ResponseBuilder.getSuccess(WebResponse.builder().pageResult(page).build());
    }

}
