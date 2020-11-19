package com.flance.jdbc.jpa.service;

import com.flance.jdbc.jpa.common.Operator;
import com.flance.jdbc.jpa.page.PageResponse;
import com.google.common.collect.Table;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManagerFactory;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * service基类
 * @author jhf
 * @param <DTO>   对象
 * @param <PO>  PO
 * @param <ID>  主键
 */
public interface IBaseDomainService<PO, DTO, ID extends Serializable> {

    /**
     * 保存实体
     * @param t 实体
     * @return  返回实体
     */
    DTO save(DTO t);

    /**
     * 更新非空
     * @param t     实体
     * @param id    主键
     * @return      返回更新后的实体
     */
    DTO updateNotNull(DTO t, ID id);

    /**
     * 批量保存（循环）
     * @param items     实体集合
     */
    void saveBatch(List<DTO> items);

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
     * 物理删除
     * @param id    主键
     */
    void delete(ID id);

    /**
     * 批量物理删除
     * @param ids   主键集合
     */
    void deleteByIds(List<ID> ids);

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
     * 根据主键查询单条结果
     * @param id    主键
     * @return      返回结果
     */
    DTO findOne(ID id);

    /**
     * 根据属性查询单条结果
     * @param key 字段名
     * @param value 字段值
     * @return  返回结果
     */
    DTO findOneByProp(String key, String value);

    /**
     * 根据属性查询单条结果
     * @param params    参数 key-value
     * @return  返回结果
     */
    DTO findOneByProps(Map<String, String> params);
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

    /**
     * 获取主键
     * @return  返回主键
     */
    ID getId();
}
