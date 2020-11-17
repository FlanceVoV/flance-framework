package com.flance.components.form.domain.dform.model.vo;

import com.flance.components.form.domain.dform.model.po.*;
import lombok.Data;

/**
 * 业务表单字段
 * @author jhf
 */
@Data
public class ServiceFormFieldVo {

    /**
     * 字段id
     */
    private String fieldId;

    /**
     * 字段中文名称
     */
    private String fieldName;

    /**
     * 字段code码
     */
    private String code;

    /**
     *  1.静态字段（指数据库表中的字段），静态字段不会存储在fieldValue表里
     *  2.动态字段
     */
    private Short isstatic;

    /**
     * 1.是大文本形式，需要用CLOB存储，即存储在BIG_VALUE_ID字段中(对应的表FIELDBVALUE)
     * 2.非大文本形式，需要用VARCHAR2存储，即存储在COMMON_VALUE字段中
     */
    private Short isBigText;

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
     * 表名
     */
    private String tableCode;

    /**
     * 字典对象
     */
    private FlanceFormTmpDic flanceFormTmpDic;

    /**
     * 字段规则
     */
    private FlanceFormTmpTmpfield flanceFormTmpTmpfield;

}
