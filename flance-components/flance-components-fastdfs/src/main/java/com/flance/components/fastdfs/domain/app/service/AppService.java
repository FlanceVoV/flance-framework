package com.flance.components.fastdfs.domain.app.service;

import com.flance.components.fastdfs.domain.app.model.dto.AppClientDto;
import com.flance.components.fastdfs.domain.app.model.po.AppClient;
import com.flance.jdbc.jpa.web.service.IBaseWebDomainService;

public interface AppService extends IBaseWebDomainService<AppClient, AppClientDto, Long> {
}
