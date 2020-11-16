package com.flance.components.fastdfs.domain.file.model.dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * fastdfs组件 文件对象dto
 * @author jhf
 */
@Data
public class FastDfsFileDto {

    @Id
    private Long id;

    /** 文件名 **/
    private String fileName;

    /**
     * fastdfs文件组名
     */
    private String groupName;

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

    /** 文件所属系统id **/
    private String appId;

    /** 是否公开授权 **/
    private Boolean isOpen;

    /** 创建日期 **/
    private Date createDate;
}
