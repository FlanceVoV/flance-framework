package com.flance.web.nacos;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class NacosController {

    @NacosInjected
    private NamingService namingService;

    @Resource
    private NacosConfigs nacosConfigs;

    public void offline() {
        try {
            namingService.deregisterInstance(nacosConfigs.getApplicationName(),
                    nacosConfigs.getIp(),
                    nacosConfigs.getPort());
            System.exit(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String nacosServerStatus() {

        return namingService.getServerStatus();
    }

    public List<Instance> getAllInstance() {
        try {
            return namingService.getAllInstances(nacosConfigs.getApplicationName());
        } catch (Exception e) {
            e.printStackTrace();
            throw new FlanceNacosException();
        }
    }


}
