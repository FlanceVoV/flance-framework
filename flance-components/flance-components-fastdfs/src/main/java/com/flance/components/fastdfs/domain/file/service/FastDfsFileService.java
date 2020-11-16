package com.flance.components.fastdfs.domain.file.service;

import com.flance.components.fastdfs.domain.file.model.dto.FastDfsFileDto;
import com.flance.components.fastdfs.domain.file.model.po.FastDfsFile;
import com.flance.jdbc.jpa.web.service.IBaseWebDomainService;

public interface FastDfsFileService extends IBaseWebDomainService<FastDfsFile, FastDfsFileDto, Long> {
}
