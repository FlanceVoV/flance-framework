package com.flance.components.fastdfs.domain.app.model.dto;


import lombok.Data;

import java.util.Date;

/**
 * fastdfs组件 服务对象dto
 * @author jhf
 */
@Data
public class AppClientDto {

    private Long id;

    /** app名称 **/
    private String appName;

    /** app标识，唯一索引 **/
    private String appId;

    /** app服务的文件鉴权url **/
    private String appAuthUrl;

    /** 创建日期 **/
    private Date createDate;

}
