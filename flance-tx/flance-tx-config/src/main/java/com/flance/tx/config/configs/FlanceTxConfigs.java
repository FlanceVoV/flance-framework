package com.flance.tx.config.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static com.flance.tx.common.TxConstants.*;


/**
 * 分布式事务控制-配置映射
 * @author jhf
 */
@Data
@Component
@ConfigurationProperties(prefix = FLANCE_TX_PREFIX)
public class FlanceTxConfigs {

    /**
     * 是否启用，默认：是
     */
    private boolean enabled = true;

    /**
     * 启用数据源代理
     */
    private boolean enableDatasourceProxy = true;

    /**
     * 事务服务组
     */
    private String txServiceGroup;

    /**
     * 服务模式
     */
    private String serverModule;

    /**
     * 事务服务ip（负载均衡地址）
     */
    private String txServerIp;

    /**
     * 事务服务端口号（负载均衡地址）
     */
    private int txServerPort;

    /**
     * 事务服务连接重试次数，当设置小于等于0时表示不进行重试
     */
    private int txServerReTryTimes;

}
