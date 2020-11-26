package com.flance.jdbc.mybatis.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.flance.jdbc.mybatis.common.BaseEntity;
import com.flance.jdbc.mybatis.service.BaseService;
import com.flance.jdbc.mybatis.web.service.BaseWebService;
import com.flance.web.common.controller.BaseController;
import com.flance.web.common.request.WebRequest;
import com.flance.web.common.request.WebResponse;
import com.flance.web.common.service.IService;
import com.flance.web.common.utils.ResponseBuilder;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BaseWebController<T extends BaseEntity> extends BaseController<T,T,Long, IPage<T>> {

    private BaseWebService<T> service;

    public void setService(BaseWebService<T> baseService) {
        this.service = baseService;
        super.setBaseService(baseService);
    }

    @Override
    public WebResponse addBatch(WebRequest<T, Long> request) {
        service.saveBatch(request.getMultiParam());
        return ResponseBuilder.getSuccess(WebResponse.builder().build());
    }

    @Override
    public WebResponse updateBatch(WebRequest<T, Long> request) {
        return ResponseBuilder.getSuccess(WebResponse.builder().singleResult(service.updateBatchById(request.getMultiParam())).build());
    }

    @Override
    public WebResponse get(WebRequest<T, Long> request) {
        return ResponseBuilder.getSuccess(WebResponse.builder().singleResult(service.getById(request.getId())).build());
    }

    @Override
    public WebResponse<T, T, Long, IPage<T>> list(WebRequest<T, Long> request) {
        List list = service.listByMap(request.getParamsMap());
        return ResponseBuilder.getSuccess(WebResponse.builder().multiResult(list).build());
    }

}
