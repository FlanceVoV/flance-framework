package com.flance.components.oss.domain.file.entity;


import com.flance.jdbc.mybatis.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * fastdfs组件 文件对象po
 * @author jhf
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OssFile extends BaseEntity<String> {

    /** oss服务器文件标识 **/
    private String ossFileId;

    /** oss服务提供id **/
    private String ossId;

    /** oss服务提供端 **/
    private String ossCode;

    /** oss服务器名称 **/
    private String ossName;

    /** 是否同步至oss服务 0.未同步 1.已同步 **/
    private Integer isSyncOss;

    /** 本地存储是否已删除 0.未删除 1.已删除 **/
    private Integer isDeletedLocal;

    /** 本地存储删除时间 **/
    private Date localDeletedTime;

    /** oss服务同步时间 **/
    private Date syncTime;

    /** oss服务最后响应内容，记录成功或失败日志 **/
    private String lastSyncLog;

    /** 文件名 **/
    private String fileName;

    /** 真实文件名 **/
    private String fileRealName;

    /** 文件md5 **/
    private String fileMd5;

    /** 文件大小 **/
    private Long fileSize;

    /** 文件大小，展示（KB,MB,GB） **/
    private String fileSizeView;

    /** 文件物理路径 **/
    private String filePath;

    /** 文件访问url **/
    private String fileUrl;

    /** 文件cdn url **/
    private String fileCdnUrl;

    /** 是否公开授权 **/
    private Boolean isOpen;

}
