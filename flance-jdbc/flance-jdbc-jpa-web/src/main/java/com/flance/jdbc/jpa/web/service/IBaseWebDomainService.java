package com.flance.jdbc.jpa.web.service;

import com.flance.jdbc.jpa.common.Operator;
import com.flance.jdbc.jpa.page.PageResponse;
import com.flance.web.common.service.IService;
import com.google.common.collect.Table;

import javax.persistence.EntityManagerFactory;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 接口提取，整合 web模块和jpa模块
 * @author jhf
 * @param <PO>  po
 * @param <DTO> dto
 * @param <ID>  主键
 */
public interface IBaseWebDomainService<PO, DTO, ID extends Serializable> extends IService<DTO, ID, PageResponse>  {

    /**
     * 批量插入
     * @param ef        实体管理工厂
     * @param items     实体集合
     */
    void insertBatch(EntityManagerFactory ef, List<DTO> items);

    /**
     * 批量更新
     * @param ef        实体管理工厂
     * @param items     实体集合
     */
    void updateBatch(EntityManagerFactory ef, List<DTO> items);

    /**
     * 批量删除
     * @param items 对象集合
     */
    void deleteBatch(List<DTO> items);

    /**
     * 根据属性删除
     * @param propName  属性名
     * @param propValue 属性值
     */
    void deleteByProperty(String propName, Object propValue);

    /**
     * 删除对象
     * @param t 对象
     */
    void deleteEntity(DTO t);

    /**
     * 根据主键集合返回列表
     * @param ids  主键集合
     * @return      返回列表
     */
    List<DTO> findByIds(List<ID> ids);

    /**
     * 无条件查询全部
     * @return  返回列表
     */
    List<DTO> findAll();

    /**
     * 有条件查询全部
     * @param table 格式(fieldName, 操作符, 值)
     * @return 返回列表
     */
    List<DTO> findAll(Table<String, Operator, Object> table);

    /**
     * 根据属性查询单条结果
     * @param params    参数 key-value
     * @return  返回结果
     */
    DTO findOneByProps(Map<String, String> params);

    /**
     * 分页查询
     * @param searchMap  查询条件
     * @return  返回结果
     */
    PageResponse<DTO> customSearch(Map<String, Object> searchMap);

    /**
     * 总数查询
     * @param searchMap 查询条件
     * @return  返回结果
     */
    long customSearchCount(HashMap<String, Object> searchMap);

}
