package com.flance.components.oss.domain.oss.service.impl;

import com.flance.components.oss.domain.oss.entity.OssServer;
import com.flance.components.oss.domain.oss.mapper.OssServerMapper;
import com.flance.components.oss.domain.oss.service.OssServerService;
import com.flance.jdbc.mybatis.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class OssServerServiceImpl extends BaseService<String, OssServerMapper, OssServer> implements OssServerService {

}
