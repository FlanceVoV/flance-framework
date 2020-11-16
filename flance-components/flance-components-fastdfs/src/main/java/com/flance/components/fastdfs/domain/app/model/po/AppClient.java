package com.flance.components.fastdfs.domain.app.model.po;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * fastdfs组件 服务对象po
 * @author jhf
 */
@Data
@Entity
@Table(name = "FLANCE_COMPOENTS_FASTDFS_APP")
public class AppClient {

    @Id
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
