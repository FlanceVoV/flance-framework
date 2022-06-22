package com.flance.tx.demo1.service.impl;

import com.flance.jdbc.mybatis.service.BaseService;
import com.flance.tx.demo1.entity.SysFlanceApi;
import com.flance.tx.demo1.mapper.SysFlanceApiMapper;
import com.flance.tx.demo1.service.SysFlanceApiService;
import org.springframework.stereotype.Service;

@Service
public class SysFlanceApiServiceImpl extends BaseService<String, SysFlanceApiMapper, SysFlanceApi> implements SysFlanceApiService {
}
