package com.flance.components.logs.domain.log.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 操作日志，针对（增、删、改）
 * @author jhf
 */
@Data
@Entity
@Table(name = "F_COMP_LOG_OPERATE_LOG")
public class OperateLog {

    @Id
    private Long id;

    /**
     * 操作类型，1.新增，2.修改，3.删除
     */
    @Column(nullable = false)
    private Integer type;

    /**
     * 操作端应用标识
     */
    @Column(nullable = false)
    private String appId;

    /**
     * 操作请求参数
     */
    private String requestParams;

    /**
     * 请求接口
     */
    @Column(nullable = false)
    private String requestUrl;

    /**
     * 操作人用户中心标识
     */
    @Column(nullable = false)
    private String userId;

    /**
     * 数据恢复状态. 1.未恢复，2.已恢复
     */
    @Column(nullable = false)
    private Integer recoverStatus;

    /**
     * 操作日期
     */
    @Column(nullable = false)
    private Date createDate;

    /**
     * 操作数据内容
     */
    @Lob
    private String content;

}
