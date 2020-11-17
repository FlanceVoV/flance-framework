package com.flance.components.form.domain.dform.model.po;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "F_COMP_FORM_TMP_FIELD")
public class FlanceFormTmpField {

    /**
    * 主键
    */
    @Id
    private String id;

    /**
    * 字段名
    */
    private String name;

    /**
    * 字段标识
    */
    private String code;

    /**
    * 字段类型
    1.文本输入框
    2.密码输入框
    3.日期输入框
    4.整数输入框
    5.浮点输入框
    6.文本域
    7.附件上传
    8.单选框
    9.复选框
    10.下拉框
    */
    private Short fieldType;

    /**
    * 1.静态字段（指数据库表中的字段），静态字段不会存储在fieldValue表里
    * 2.动态字段
    */
    private Short isstatic;

    /**
    * 1.是大文本形式，需要用CLOB存储，即存储在BIG_VALUE_ID字段中(对应的表FIELDBVALUE)
    * 2.非大文本形式，需要用VARCHAR2存储，即存储在COMMON_VALUE字段中
    */
    private Short isBigText;

    /**
    * 关联的字典（父节点），允许为空
    * 针对：单选框、复选框、下拉菜单等字典信息
    */
    private String dicId;

    /**
    * 表名
    */
    private String tableCode;

    /**
     * 字段值
     */
    @OneToOne
    @JoinColumn(name="FIELD_FK",referencedColumnName="ID")
    @NotFound(action = NotFoundAction.IGNORE)
    private FlanceFormTmpFieldvalue flanceFormTmpFieldvalue;

    private String createBy;

    private Date createDate;

    private Short deleted;
}