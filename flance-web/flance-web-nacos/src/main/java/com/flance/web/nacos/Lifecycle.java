package com.flance.web.nacos;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * All Beans lifecycle
 * @author jhf
 */
@Slf4j
@Component
public class Lifecycle implements SmartLifecycle {

    private boolean start = false;

    @Resource
    private NacosConfigs nacosConfigs;

    @Override
    public void start() {
        start = true;
        log.info("flance-nacos加载器-spring容器加载完成，nacos注册成功");
        log.info("flance-nacos加载器-配置表-ip起始[{}]", nacosConfigs.getPreferredNetworks());
        log.info("flance-nacos加载器-配置表-servletPath[{}]", nacosConfigs.getContextPath());

        log.info("flance-nacos加载器-nacos配置-服务名[{}]", nacosConfigs.getApplicationName());
        log.info("flance-nacos加载器-nacos配置-服务注册发现名目空间[{}]", nacosConfigs.getNacosDiscoveryNamespace());
        log.info("flance-nacos加载器-nacos配置-服务注册发现中心地址[{}]", nacosConfigs.getNacosDiscoveryServerAddress());
        log.info("flance-nacos加载器-nacos配置-服务注册发现组[{}]", nacosConfigs.getNacosDiscoveryGroup());
        log.info("flance-nacos加载器-nacos配置-服务配置中心名目空间[{}]", nacosConfigs.getNacosConfigNamespace());
        log.info("flance-nacos加载器-nacos配置-服务配置中心地址[{}]", nacosConfigs.getNacosConfigServerAddress());
        Optional.ofNullable(nacosConfigs.getExtensionConfigs()).orElse(Lists.newArrayList())
                .forEach(exConfig -> log.info("flance-nacos加载器-配置表-加载扩展配置[{}]", exConfig.toString()));

        log.info("flance-nacos加载器-注册表-本机ip[{}]", nacosConfigs.getIp());
        log.info("flance-nacos加载器-注册表-本机port[{}]", nacosConfigs.getPort());
        log.info("flance-nacos加载器-注册表-本机权重[{}]", nacosConfigs.getWeight());
        log.info("flance-nacos加载器-注册表-本服务集群名称[{}]", nacosConfigs.getClusterName());
        
        log.info("flance-nacos加载器-内置api-服务下线-[NacosController.offline]");
        log.info("flance-nacos加载器-内置api-nacos服务状态-[NacosController.nacosServerStatus]");
        log.info("flance-nacos加载器-内置api-client服务状态-[NacosController.]");

    }

    @Override
    public void stop() {
        start = false;
        log.warn("flance-nacos加载器-关闭服务");
    }

    @Override
    public boolean isRunning() {
        return start;
    }

}
