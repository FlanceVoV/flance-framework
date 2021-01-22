package com.flance.web.common.controller;

import com.flance.web.utils.web.request.WebRequest;
import com.flance.web.utils.web.response.WebResponse;

/**
 * rest api
 * @author jhf
 * @param <DTO> 传输对象
 * @param <VO>  视图对象
 */
public interface IBaseController<DTO, VO, ID, PAGE> {

    /**
     * 新增对象
     * @param request   请求
     * @return  响应
     */
    WebResponse add(WebRequest<DTO, ID> request);

    /**
     * 批量新增
     * @param request   请求
     * @return  响应
     */
    WebResponse addBatch(WebRequest<DTO, ID> request);

    /**
     * 编辑
     * @param request   请求
     * @return  响应
     */
    WebResponse update(WebRequest<DTO, ID> request);

    /**
     * 批量编辑
     * @param request   请求
     * @return  响应
     */
    WebResponse updateBatch(WebRequest<DTO, ID> request);

    /**
     * 删除
     * @param request 请求
     * @return  响应
     */
    WebResponse delete(WebRequest<DTO, ID> request);

    /**
     * 批量删除
     * @param request   请求
     * @return  响应
     */
    WebResponse deleteBatch(WebRequest<DTO, ID> request);

    /**
     * 查询单个结果
     * @param request   请求
     * @return  响应
     */
    WebResponse get(WebRequest<DTO, ID> request);

    /**
     * 查询分页结果
     * @param request   请求
     * @return  响应
     */
    WebResponse page(WebRequest<DTO, ID> request);

    /**
     * 查询列表结果（无分页）
     * @param request   请求
     * @return  响应
     */
    WebResponse list(WebRequest<DTO, ID> request);
}
