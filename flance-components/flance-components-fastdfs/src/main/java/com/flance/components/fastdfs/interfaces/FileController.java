package com.flance.components.fastdfs.interfaces;

import com.flance.components.fastdfs.domain.app.model.domain.AppClientDo;
import com.flance.components.fastdfs.domain.file.model.domain.FastDfsFileDo;
import com.flance.components.fastdfs.domain.file.model.dto.FastDfsFileDto;
import com.flance.components.fastdfs.domain.file.parser.FastDfsFileParser;
import com.flance.components.fastdfs.domain.file.service.FastDfsFileService;
import com.flance.components.fastdfs.infrastructure.utils.FastDfsClient;
import com.flance.web.common.request.WebResponse;
import com.flance.web.common.utils.ResponseBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * fastdfs文件服务接口
 * @author jhf
 */
@RestController
@RequestMapping("/api/fast_dfs")
public class FileController {

    @Resource
    FastDfsClient fastDfsClient;

    @Resource
    FastDfsFileService fastDfsFileService;

    @Resource
    FastDfsFileParser fastDfsFileParser;

    @PostMapping("/upload/{appId}")
    public WebResponse upload(@RequestParam("file") MultipartFile file, @PathVariable String appId) {
        FastDfsFileDto fastDfsFileDto = fastDfsClient.uploadFile(file, null);
        FastDfsFileDo fastDfsFileDo = fastDfsFileParser.parseDto2Do(fastDfsFileDto);
        fastDfsFileDo.convertSize();
        fastDfsFileDto = fastDfsFileParser.parseDo2Dto(fastDfsFileDo);
        fastDfsFileDo.setAppId(appId);
        fastDfsFileService.save(fastDfsFileDto);
        return ResponseBuilder.getSuccess(WebResponse.builder().singleResult(fastDfsFileDto).build());
    }

    @GetMapping("/download/{fileId}")
    public WebResponse download(HttpServletResponse response, @PathVariable("fileId") Long fileId) {
        FastDfsFileDto fastDfsFileDto = fastDfsFileService.findOne(fileId);
        AppClientDo appClientDo = new AppClientDo();
        appClientDo.setAppAuthUrl(fastDfsFileDto.getAppId());
        appClientDo.hasFilePermission(null, fileId);
        fastDfsClient.downloadFile(fastDfsFileDto, null, response);
        return ResponseBuilder.getSuccess(WebResponse.builder().build());
    }

}
