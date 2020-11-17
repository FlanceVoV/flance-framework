package com.flance.components.form.domain.dform.model.po;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 静态表，附件信息
 * @author jhf
 */
@Data
@Entity
@Table(name = "F_COMP_FORM_SDB_FILE")
public class FlanceFormSdbFile {

    @Id
    private String id;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * url路劲
     */
    private String urlPath;

    /**
     * 真实名
     */
    private String realName;

    /**
     * 物理路径
     */
    private String physicalPath;

    /**
     * 附件类型
     */
    private String type;

    /**
     * 附件后缀名
     */
    private String ext;

    /**
     * 附件大小
     */
    private String size;

    /**
     * 附件md5
     */
    private String md5;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 是否删除
     */
    private Short deleted;

    /**
     * 属性表外键
     */
    private String fieldvalueFk;
}