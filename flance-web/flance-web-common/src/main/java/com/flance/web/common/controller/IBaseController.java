package com.flance.web.common.controller;

import com.flance.web.common.request.WebRequest;
import com.flance.web.common.request.WebResponse;

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
    WebResponse<DTO, VO, ID, PAGE> add(WebRequest<DTO, ID> request);

    /**
     * 批量新增
     * @param request   请求
     * @return  响应
     */
    WebResponse<DTO, VO, ID, PAGE> addBatch(WebRequest<DTO, ID> request);

    /**
     * 编辑
     * @param request   请求
     * @return  响应
     */
    WebResponse<DTO, VO, ID, PAGE> update(WebRequest<DTO, ID> request);

    /**
     * 批量编辑
     * @param request   请求
     * @return  响应
     */
    WebResponse<DTO, VO, ID, PAGE> updateBatch(WebRequest<DTO, ID> request);

    /**
     * 删除
     * @param request 请求
     * @return  响应
     */
    WebResponse<DTO, VO, ID, PAGE> delete(WebRequest<DTO, ID> request);

    /**
     * 批量删除
     * @param request   请求
     * @return  响应
     */
    WebResponse<DTO, VO, ID, PAGE> deleteBatch(WebRequest<DTO, ID> request);

    /**
     * 查询单个结果
     * @param request   请求
     * @return  响应
     */
    WebResponse<DTO, VO, ID, PAGE> get(WebRequest<DTO, ID> request);

    /**
     * 查询分页结果
     * @param request   请求
     * @return  响应
     */
    WebResponse<DTO, VO, ID, PAGE> page(WebRequest<DTO, ID> request);

    /**
     * 查询列表结果（无分页）
     * @param request   请求
     * @return  响应
     */
    WebResponse<DTO, VO, ID, PAGE> list(WebRequest<DTO, ID> request);
}
