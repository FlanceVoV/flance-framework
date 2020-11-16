package com.flance.web.common.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * web层 service接口标准 可以继承扩展
 * @author jhf
 * @param <T>   实例
 * @param <ID>  主键
 * @param <PAGE> 分页模板
 */
public interface IService<T, ID, PAGE> {


    /**
     * 保存实体
     * @param t 实体
     * @return  返回实体
     */
    T save(T t);

    /**
     * 批量保存（循环）
     * @param items     实体集合
     */
    void saveBatch(List<T> items);


    /**
     * 更新非空
     * @param t     实体
     * @param id    主键
     * @return      返回更新后的实体
     */
    T updateNotNull(T t, ID id);


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
     * 根据主键查询单条结果
     * @param id    主键
     * @return      返回结果
     */
    T findOne(ID id);

    /**
     * 根据属性查询单条结果
     * @param key 字段名
     * @param value 字段值
     * @return  返回结果
     */
    T findOneByProp(String key, String value);

    /**
     * 分页查询
     * @param searchMap  查询条件
     * @return  返回结果
     */
    PAGE findPage(Map<String, Object> searchMap);

    /**
     * 返回集合
     * @param searchMap 查询条件
     * @return  返回结果
     */
    List<T> findAll(Map<String, Object> searchMap);

}
