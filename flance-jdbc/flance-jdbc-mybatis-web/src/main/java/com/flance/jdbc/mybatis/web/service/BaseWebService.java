package com.flance.jdbc.mybatis.web.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.flance.jdbc.mybatis.common.BaseEntity;
import com.flance.jdbc.mybatis.service.BaseService;
import com.flance.web.common.service.IService;




public abstract class BaseWebService<T extends BaseEntity> extends BaseService<T> implements IService<T, Long, IPage<T>>  {



}
