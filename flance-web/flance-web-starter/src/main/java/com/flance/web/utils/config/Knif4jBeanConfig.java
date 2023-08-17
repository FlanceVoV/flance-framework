//package com.flance.web.utils.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import jakarta.annotation.Resource;
//
//@Slf4j
//@EnableSwagger2
//@Configuration
//public class Knif4jBeanConfig {
//
//    @Resource
//    Knife4jConfig knife4jConfig;
//
//    @Bean(value = "defaultApi2")
//    public Docket defaultApi2() {
//        log.info("flance-api-doc 加载");
//        if (null == knife4jConfig || null == knife4jConfig.getVersion()) {
//            return null;
//        }
//        log.info("flance-api-doc servletContext：[{}]", knife4jConfig.getServletContext());
//        log.info("flance-api-doc 服务名：[{}]", knife4jConfig.getTitle());
//        log.info("flance-api-doc 服务描述：[{}]", knife4jConfig.getDesc());
//        log.info("flance-api-doc 版本号：[{}]", knife4jConfig.getVersion());
//        log.info("flance-api-doc 扫描路径：[{}]", knife4jConfig.getScanPackage());
//
//        String groupName = knife4jConfig.getGroupName();
//        Docket docket = new Docket(DocumentationType.OAS_30)
//                .apiInfo(new ApiInfoBuilder()
//                        .title(knife4jConfig.getTitle())
//                        .description(knife4jConfig.getDesc())
//                        .termsOfServiceUrl(knife4jConfig.getTermsOfServiceUrl())
//                        .contact(new Contact(knife4jConfig.getContactName(), knife4jConfig.getContactUrl(), knife4jConfig.getContactEmail()))
//                        .version(knife4jConfig.getVersion())
//                        .build())
//                //分组名称
//                .groupName(groupName)
//                .select()
//                //这里指定Controller扫描包路径
//                .apis(RequestHandlerSelectors.basePackage(knife4jConfig.getScanPackage()))
//                .paths(PathSelectors.any())
//                .build();
//        return docket;
//    }
//
//}
