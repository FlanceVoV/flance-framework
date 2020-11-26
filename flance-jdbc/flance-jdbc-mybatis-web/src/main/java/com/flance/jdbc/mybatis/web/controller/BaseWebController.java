package com.flance.jdbc.mybatis.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.flance.jdbc.mybatis.common.BaseEntity;
import com.flance.jdbc.mybatis.service.BaseService;
import com.flance.web.common.controller.BaseController;
import com.flance.web.common.request.WebRequest;
import com.flance.web.common.request.WebResponse;
import com.flance.web.common.utils.ResponseBuilder;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseWebController<T extends BaseEntity> extends BaseController<T,T,Long, IPage<T>> {

    private BaseService<T> service;

    public void setService(BaseService<T> baseService) {
        this.service = baseService;
    }

    @Override
    public WebResponse addBatch(WebRequest<T, Long> request) {
        return ResponseBuilder.getSuccess(WebResponse.builder().singleResult(service.saveBatch(request.getMultiParam())).build());
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
    public WebResponse list(WebRequest<T, Long> request) {
        return null;

    }
}
