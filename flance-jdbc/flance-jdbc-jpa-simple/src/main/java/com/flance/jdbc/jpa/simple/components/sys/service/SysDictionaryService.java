package com.flance.jdbc.jpa.simple.components.sys.service;


import com.flance.jdbc.jpa.simple.common.request.PageResponse;
import com.flance.jdbc.jpa.simple.components.sys.entity.SysDictionary;
import com.flance.jdbc.jpa.simple.service.IService;

/**
 * 业务字典接口
 * @author jhf
 */
public interface SysDictionaryService extends IService<SysDictionary, Long, PageResponse<SysDictionary>> {

}
