//package com.flance.web.utils.config;
//
//import com.google.common.collect.Maps;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.env.EnvironmentPostProcessor;
//import org.springframework.core.env.ConfigurableEnvironment;
//import org.springframework.core.env.MapPropertySource;
//
//import java.util.Map;
//
//@Slf4j
//public class Knife4jConfigAccept  implements EnvironmentPostProcessor {
//
//    @Override
//    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
//        log.info("修改配置，兼容api doc");
//        String name = "Config resource 'class path resource [bootstrap.yml]' via location 'optional:classpath:/'";
//        if (null == environment.getPropertySources().get(name)) {
//            return;
//        }
//        MapPropertySource propertySource = (MapPropertySource) environment.getPropertySources().get(name);
//        Map<String, Object> source = propertySource.getSource();
//        Map<String, Object> map = Maps.newConcurrentMap();
//        source.forEach(map::put);
//        // 将配置的端口号修改为 8022
//        map.put("spring.mvc.pathmatch.matching-strategy", "ant_path_matcher");
//        environment.getPropertySources().replace(name, new MapPropertySource(name, map));
//    }
//}
