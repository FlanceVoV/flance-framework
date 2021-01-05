package com.flance.jdbc.jpa.simple.service;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Map;

/**
 * @author jhf
 * service 接口
 * @param <T>
 * @param <ID>
 * @param <PAGE>
 */
public interface IService<T, ID, PAGE> {

    /**
     * 保存一个对象
     * @param t 实体
     * @return  返回保存实体
     */
    T saveOne(T t);

    /**
     * 批量保存
     * @param items     实体集合
     */
    void saveBatch(List<T> items);

    /**
     * update非空
     * @param t     实体
     * @param id    主键
     * @return      返回实体
     */
    T updateNotNull(T t, ID id);

    /**
     * 批量更新
     * @param items 实体集合
     */
    void updateBatch(EntityManagerFactory factory, List<T> items);

    /**
     * 根据id删除
     * @param id    主键
     */
    void delete(ID id);

    /**
     * 批量删除
     * @param ids   主键集合
     */
    void deleteByIds(List<ID> ids);

    /**
     * 根据属性删除
     * @param propertyName      属性key
     * @param propertyValue     属性值
     */
    void deleteByProperty(String propertyName, Object propertyValue);

    /**
     * 根据实体删除
     * @param t     实体
     */
    void deleteEntity(T t);

    /**
     * 根据主键查询单个
     * @param id    主键
     * @return      返回实体
     */
    T findOne(ID id);

    /**
     * 根据某个属性查询单个
     * @param key       属性名
     * @param value     属性值
     * @return          返回实体
     */
    T findOneByProp(String key, Object value);

    /**
     * 根据某组属性查询单个
     * @param params    参数
     * @return  返回实体
     */
    T findOneByProps(Map<String, Object> params);

    /**
     * 分页查询
     * @param searchMap     参数
     * @return      返回分页对象
     */
    PAGE findPage(Map<String, Object> searchMap);

    /**
     * 查询全部
     * @param searchMap     参数
     * @return      返回列表
     */
    List<T> findAll(Map<String, Object> searchMap);

    /**
     * 查询全部，无条件
     * @return  返回列表
     */
    List<T> findAll();

    /**
     * 根据主键集合查询列表
     * @param ids   主键集合
     * @return      列表
     */
    List<T> findByIds(List<ID> ids);
}
