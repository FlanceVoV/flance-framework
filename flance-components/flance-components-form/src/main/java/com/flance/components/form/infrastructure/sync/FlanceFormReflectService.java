package com.flance.components.form.infrastructure.sync;


import com.flance.components.form.domain.dform.model.po.FlanceFormBizGroup;
import com.flance.components.form.domain.dform.model.po.FlanceFormTmpField;
import com.flance.components.form.domain.dform.model.po.FlanceFormTmpTemplate;

import java.util.List;
import java.util.Map;

/**
 * 按组提交用户数据
 * @author jhf
 */
public interface FlanceFormReflectService {

    /**
     * 主方法
     * @param mainformId
     * @param groupId
     * @param tmpTemplate
     */
    void invoke(String mainformId, String groupId, FlanceFormTmpTemplate tmpTemplate);

    /**
     * 设置静态字段值
     * @param flanceFormTmpField
     * @param objectMap
     */
    void staticField(FlanceFormTmpField flanceFormTmpField, Map<String, List<Map<String, Object>>> objectMap);

    /**
     * 设置非静态字段值
     * @param tmpField
     * @param isList
     * @param objGroup
     */
    void unStaticField(FlanceFormTmpField tmpField, Short isList, String objGroup);

    /**
     * 保存静态字段
     * @param mainformId
     * @param objectMap
     */
    void staticFieldSave(String mainformId, Map<String, List<Map<String, Object>>> objectMap);

    /**
     * 按组保存数据
     * @param mainFlowId
     * @param flanceFormBizGroup
     */
    void saveByGroup(String mainFlowId, FlanceFormBizGroup flanceFormBizGroup);

}
