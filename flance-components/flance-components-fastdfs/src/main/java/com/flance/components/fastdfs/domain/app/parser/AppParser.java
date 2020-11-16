package com.flance.components.fastdfs.domain.app.parser;

import com.flance.components.fastdfs.domain.app.model.domain.AppClientDo;
import com.flance.components.fastdfs.domain.app.model.dto.AppClientDto;
import com.flance.components.fastdfs.domain.app.model.po.AppClient;
import com.flance.components.fastdfs.domain.app.model.vo.AppClientVo;
import com.flance.jdbc.jpa.parser.BaseParser;
import org.springframework.stereotype.Component;

@Component
public class AppParser extends BaseParser<AppClient, AppClientDto, AppClientVo, AppClientDo> {
}
