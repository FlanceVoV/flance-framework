package com.flance.components.fastdfs.domain.file.parser;

import com.flance.components.fastdfs.domain.file.model.domain.FastDfsFileDo;
import com.flance.components.fastdfs.domain.file.model.dto.FastDfsFileDto;
import com.flance.components.fastdfs.domain.file.model.po.FastDfsFile;
import com.flance.components.fastdfs.domain.file.model.vo.FastDfsFileVo;
import com.flance.jdbc.jpa.parser.BaseParser;
import org.springframework.stereotype.Component;

@Component
public class FastDfsFileParser extends BaseParser<FastDfsFile, FastDfsFileDto, FastDfsFileVo, FastDfsFileDo> {

}
