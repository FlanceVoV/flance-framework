package com.flance.components.oss.interfaces;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.flance.components.oss.domain.oss.entity.OssServer;
import com.flance.components.oss.domain.oss.service.OssServerService;
import com.flance.components.oss.infrastructure.oss.BaseOssService;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("/oss")
public class OssFileController {

    @Resource
    ApplicationContext applicationContext;

    @Resource
    OssServerService ossServerService;

    public String list() {
        return "";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file, @RequestParam String ossCode) {
        LambdaQueryWrapper<OssServer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OssServer::getOssCode, ossCode);
        OssServer ossServer = ossServerService.getOne(queryWrapper);
        BaseOssService baseOssService = applicationContext.getBean(ossServer.getOssServiceBean(), BaseOssService.class);

        return "上传成功！";
    }

    @GetMapping("/download")
    public String download(@RequestParam String fileId) {

        return "";
    }

}
