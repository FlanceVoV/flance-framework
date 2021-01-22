package com.flance.web.common.controller;


import com.flance.web.common.service.IService;
import com.flance.web.utils.web.request.WebRequest;
import com.flance.web.utils.web.response.WebResponse;
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
        boolean flag = baseService.save(request.getSingleParam());
        return WebResponse.getSucceed(flag, "新增成功！");
    }

    @Override
    @PatchMapping("/update")
    public WebResponse update(@RequestBody WebRequest<DTO, ID> request) {
        DTO dto = request.getSingleParam();
        ID id = request.getId();
        baseService.updateNotNull(dto, id);
        return WebResponse.getSucceed(dto, "编辑成功！");
    }

    @Override
    @DeleteMapping("/delete")
    public WebResponse delete(@RequestBody WebRequest<DTO, ID> request) {
        ID id = request.getId();
        baseService.delete(id);
        return WebResponse.getSucceed(null, "删除成功！");
    }

    @Override
    @DeleteMapping("/delete/ids")
    public WebResponse deleteBatch(@RequestBody WebRequest<DTO, ID> request) {
        List<ID> ids = request.getIds();
        baseService.deleteByIds(ids);
        return WebResponse.getSucceed(null, "删除成功！");
    }

    @Override
    @PostMapping("/page")
    public WebResponse page(@RequestBody WebRequest<DTO, ID> request) {
        Map<String, Object> searchMap = request.getParamsMap();
        PAGE page = baseService.findPage(searchMap);
        return WebResponse.getSucceed(page, "查询成功！");
    }

}
