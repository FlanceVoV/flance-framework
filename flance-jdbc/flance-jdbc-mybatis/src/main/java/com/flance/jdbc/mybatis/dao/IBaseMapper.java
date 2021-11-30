package com.flance.jdbc.mybatis.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flance.jdbc.mybatis.common.BaseEntity;
import com.flance.jdbc.mybatis.common.IEntity;

/**
 * mapper基类
 * @author jhf
 * @param <T>
 */
@Deprecated
public interface IBaseMapper<ID, T extends IEntity<ID>> extends BaseMapper<T> {



}
