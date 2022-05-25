package com.flance.components.oss.domain.file.service.impl;

import com.flance.components.oss.domain.file.entity.OssFile;
import com.flance.components.oss.domain.file.mapper.OssFileMapper;
import com.flance.components.oss.domain.file.service.OssFileService;
import com.flance.jdbc.mybatis.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class OssFileServiceImpl extends BaseService<String, OssFileMapper, OssFile> implements OssFileService {
}
