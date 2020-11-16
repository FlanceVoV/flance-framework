package com.flance.components.fastdfs.domain.app.service.impl;

import com.flance.components.fastdfs.domain.app.model.domain.AppClientDo;
import com.flance.components.fastdfs.domain.app.model.dto.AppClientDto;
import com.flance.components.fastdfs.domain.app.model.po.AppClient;
import com.flance.components.fastdfs.domain.app.model.vo.AppClientVo;
import com.flance.components.fastdfs.domain.app.service.AppService;
import com.flance.jdbc.jpa.web.service.BaseWebDomainService;
import org.springframework.stereotype.Service;

/**
 * app Service
 * @author jhf
 */
@Service
public class AppServiceImpl extends BaseWebDomainService<AppClient, AppClientDto, AppClientVo, AppClientDo, Long> implements AppService {


}
