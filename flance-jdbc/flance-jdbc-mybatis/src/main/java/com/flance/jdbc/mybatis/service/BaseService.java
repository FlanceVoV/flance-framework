package com.flance.jdbc.mybatis.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flance.jdbc.mybatis.common.IEntity;

/**
 * service基类
 * @author jhf
 * @param <T>
 */
public class BaseService<ID, M extends BaseMapper<T>, T extends IEntity<ID>> extends ServiceImpl<M,T> {


}