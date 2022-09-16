package com.flance.web.nacos;

import com.alibaba.cloud.nacos.registry.NacosRegistration;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;


/**
 * 应用nacos配置
 * @author jhf
 */
@Data
@Component
@DependsOn(value = {"nacosAutoServiceRegistration"})
public class NacosConfigs {

    private static final String SPRING_CONFIG_EX_CONFIGS = "spring.cloud.nacos.config.extension-configs";

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.cloud.nacos.discovery.namespace}")
    private String nacosDiscoveryNamespace;

    @Value("${spring.cloud.nacos.discovery.server-addr}")
    private String nacosDiscoveryServerAddress;

    @Value("${spring.cloud.nacos.discovery.group}")
    private String nacosDiscoveryGroup;

    @Value("${spring.cloud.nacos.config.namespace}")
    private String nacosConfigNamespace;

    @Value("${spring.cloud.nacos.config.server-addr}")
    private String nacosConfigServerAddress;

    @Value("${server.servlet.context-path:/}")
    private String contextPath;

    @Value("${spring.cloud.inetutils.preferred-networks:0}")
    private String preferredNetworks;

    @Value("${server.port:-1}")
    private Integer port;

    private List<ExConfig> extensionConfigs;

    private String ip;



    private Float weight;

    private String clusterName;

    @Resource
    private NacosRegistration nacosRegistration;


    @Data
    static class ExConfig {
        @JsonProperty("data-id")
        private String dataId;

        private String group;

        @JsonProperty("file-extension")
        private String fileExtension;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("配置文件：").append(dataId).append("|");
            sb.append("配置文件组：").append(group).append("|");
            sb.append("文件类型：").append(fileExtension).append("|");
            return sb.toString();
        }

    }

    @PostConstruct
    public void post() {
        YamlUtil.load("application.yml", "bootstrap.yml");
        Object object = YamlUtil.getValueByKey(SPRING_CONFIG_EX_CONFIGS);

        this.ip = nacosRegistration.getHost();
        this.port = nacosRegistration.getPort();
        this.weight = nacosRegistration.getRegisterWeight();
        this.clusterName = nacosRegistration.getCluster();

        if (object instanceof List<?> list) {
            this.extensionConfigs = Lists.newArrayList();
            list.forEach(obj -> {
                ObjectMapper objectMapper = new ObjectMapper();
                ExConfig exConfig = objectMapper.convertValue(obj, ExConfig.class);
                this.extensionConfigs.add(exConfig);
            });
        }
        YamlUtil.remove();
    }

    public Integer getPort() {
        if (port < 0) {
            port = this.nacosRegistration.getPort();
        }
        return port;
    }
}
