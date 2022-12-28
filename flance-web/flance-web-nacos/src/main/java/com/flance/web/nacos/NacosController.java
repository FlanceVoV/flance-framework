package com.flance.web.nacos;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class NacosController {

    @Resource
    private NamingService namingService;

    @Resource
    private NacosConfigs nacosConfigs;

    public void offline() {
        try {
            namingService.shutDown();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("flance-nacos namingService error [{}]", e.getMessage());
        }
    }

    public void shutdown() {
        log.warn("flance-nacos namingService shutdown");
        System.exit(0);
    }

    public String nacosServerStatus() {
        return namingService.getServerStatus();
    }

    public List<Instance> getAllInstance() {
        try {
            return namingService.getAllInstances(nacosConfigs.getApplicationName());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("flance-nacos namingService error [{}]", e.getMessage());
            return Lists.newArrayList();
        }
    }


}
