package com.flance.components.fastdfs.domain.file.service.impl;

import com.flance.components.fastdfs.domain.file.model.domain.FastDfsFileDo;
import com.flance.components.fastdfs.domain.file.model.dto.FastDfsFileDto;
import com.flance.components.fastdfs.domain.file.model.po.FastDfsFile;
import com.flance.components.fastdfs.domain.file.model.vo.FastDfsFileVo;
import com.flance.components.fastdfs.domain.file.parser.FastDfsFileParser;
import com.flance.components.fastdfs.domain.file.repository.FastDfsFileDao;
import com.flance.components.fastdfs.domain.file.service.FastDfsFileService;
import com.flance.jdbc.jpa.web.service.BaseWebDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FastDfsFileServiceImpl extends BaseWebDomainService<FastDfsFile, FastDfsFileDto, FastDfsFileVo, FastDfsFileDo, Long> implements FastDfsFileService {

    private FastDfsFileParser fastDfsFileParser;

    private FastDfsFileDao fastDfsFileDao;

    @Autowired
    public void setFastDfsFileParser(FastDfsFileParser fastDfsFileParser) {
        this.fastDfsFileParser = fastDfsFileParser;
        super.setBaseParser(fastDfsFileParser);
    }

    @Autowired
    public void setFastDfsFileDao(FastDfsFileDao fastDfsFileDao) {
        this.fastDfsFileDao = fastDfsFileDao;
        super.setBaseDao(fastDfsFileDao);
    }
}
