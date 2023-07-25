package com.flance.web.utils.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.stereotype.Component;

import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Data
@Component
@ConfigurationProperties(prefix = "flance.apidoc.config")
public class Knife4jConfig {

    private String servletContext = "/";

    private String groupName = "默认分组";

    private String title = "默认服务";

    private String desc = "接口文档";

    private String version = "1.0";

    private String scanPackage = "com.flance.*";

    private String contactName = "";

    private String contactUrl = "";

    private String contactEmail = "";

    private String termsOfServiceUrl = "";

}
