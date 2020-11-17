package com.flance.components.form.domain.dform.model.po;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "F_COMP_FORM_TMP_DIC")
public class FlanceFormTmpDic implements Serializable {
    /**
    * 主键
    */
    @Id
    private String id;

    /**
     * 业务编码
     */
    private String code;

    /**
    * 名称
    */
    private String name;

    /**
    * 默认根节点为0
    */
    private String parentId;

    /**
    * 类型
    */
    private String dicType;

    /**
    * 1是2否可选
    */
    private Short isOpen;

    /**
    * 提示信息
    */
    private String mark;

    /**
    * 层级
    */
    private Short level;

    private String createBy;

    private Date createDate;

    private Short deleted;

    @Transient
    private Integer childNum;

    @Transient
    private Boolean leaf;

    @Transient
    private List<FlanceFormTmpDic> children;

    @Transient
    private FlanceFormTmpDic flanceFormTmpDic;

    public Boolean getLeaf() {
        if (this.childNum == null) {
            return true;
        }else {
            return this.childNum < 1;
        }
    }

    public void setLeaf(Boolean leaf) {
        if (this.childNum == null) {
            this.leaf = true;
        }else {
            this.leaf = this.childNum < 1;
        }
    }
}