package com.flance.components.fastdfs.domain.file.model.domain;

import lombok.Data;

import javax.persistence.Id;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * fastdfs组件 文件对象do
 * @author jhf
 */
@Data
public class FastDfsFileDo {

    @Id
    private Long id;

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

    /** 文件所属系统id **/
    private String appId;

    /** 是否公开授权 **/
    private Boolean isOpen;

    /** 创建日期 **/
    private Date createDate;

    /**
     * 校验md5
     * @param md5
     * @return
     */
    public Boolean checkMd5(String md5) {
        return md5.equals(this.fileMd5);
    }

    /**
     * 文件大小转换
     */
    public void convertSize() {
        DecimalFormat df = new DecimalFormat("#.00");
        if (this.fileSize == 0) {
            this.fileSizeView = "0";
        }
        if (this.fileSize < 1024) {
            this.fileSizeView = df.format((double) this.fileSize) + "B";
        } else if (this.fileSize < 1048576) {
            this.fileSizeView = df.format((double) this.fileSize / 1024) + "KB";
        } else if (this.fileSize < 1073741824) {
            this.fileSizeView = df.format((double) this.fileSize / 1048576) + "MB";
        } else {
            this.fileSizeView = df.format((double) this.fileSize / 1073741824) + "GB";
        }
    }
}
