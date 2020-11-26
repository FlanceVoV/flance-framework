package com.flance.jdbc.mybatis.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flance.jdbc.mybatis.common.BaseEntity;

/**
 * mapper基类
 * @author jhf
 * @param <T>
 */
public interface IBaseMapper<T extends BaseEntity> extends BaseMapper<T> {
}
