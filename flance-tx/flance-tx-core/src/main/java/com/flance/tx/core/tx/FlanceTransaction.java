package com.flance.tx.core.tx;

import lombok.Data;

/**
 * 分布式 事务模型封装
 * @author jhf
 */
@Data
public class FlanceTransaction {

    /**
     * 事务组id
     */
    private String txGroupId;

    /**
     * 事务id
     */
    private String txId;

    /**
     * 事务编号
     */
    private Integer txIndex;

    /**
     * 事务应用id
     */
    private String applicationId;

    /**
     * 事务服务id
     */
    private String txServerId;

    /**
     * 事务模型
     */
    private String txModule;

    /**
     * 事务服务ip
     */
    private String txServerIp;

    /**
     * 事务应用ip
     */
    private String applicationIp;

    /**
     * 执行的sql
     */
    private String execSql;

}
