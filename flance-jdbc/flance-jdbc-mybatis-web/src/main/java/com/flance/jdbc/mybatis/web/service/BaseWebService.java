package com.flance.jdbc.mybatis.web.service;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.flance.jdbc.mybatis.common.BaseEntity;
import com.flance.jdbc.mybatis.service.BaseService;
import com.flance.web.common.service.IService;

import java.util.List;
import java.util.Map;


/**
 * webCommon -- mybatis整合
 * @author jhf
 * @param <T>
 */
public abstract class BaseWebService<T extends BaseEntity> extends BaseService<T> implements IService<T, Long, IPage<T>>  {

    @Override
    public boolean save(T entity) {
        return super.save(entity);
    }

    @Override
    public void saveBatch(List<T> items) {
        super.saveBatch(items);
    }

    @Override
    public T updateNotNull(T t, Long aLong) {
        super.updateById(t);
        return null;
    }

    @Override
    public void delete(Long id) {
        super.removeById(id);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        super.removeByIds(ids);
    }

    @Override
    public T findOne(Long id) {
        return super.getById(id);
    }

    @Override
    public T findOneByProp(String key, String value) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(key,value);
        return super.getOne(queryWrapper);
    }

    @Override
    public IPage<T> findPage(Map<String, Object> searchMap) {
        return null;
    }

    @Override
    public List<T> findAll(Map<String, Object> searchMap) {
        return super.listByMap(searchMap);
    }
}
