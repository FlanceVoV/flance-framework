package com.flance.components.form.infrastructure.sync.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.flance.components.form.domain.dform.model.po.*;
import com.flance.components.form.domain.dform.model.vo.ServiceFormFieldVo;
import com.flance.components.form.domain.dform.model.vo.ServiceFormGroupVo;
import com.flance.components.form.domain.dform.model.vo.ServiceFormTemplateVo;
import com.flance.components.form.domain.dform.model.vo.ServiceFormVo;
import com.flance.components.form.domain.dform.service.*;
import com.flance.components.form.infrastructure.sync.FlanceFormServiceFormSyncService;
import com.google.common.collect.Lists;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * 同步业务申报表service
 * @author jhf
 */
@Service
public class FlanceFormServiceFormSyncServiceImpl implements FlanceFormServiceFormSyncService {

    /** 业务申报表单service */
    @Resource
    private FlanceFormBizServiceformService flanceFormBizServiceformService;

    /** 业务组service */
    @Resource
    private FlanceFormBizGroupService flanceFormBizGroupService;

    /** 模板表service */
    @Resource
    private FlanceFormTmpTemplateService flanceFormTmpTemplateService;

    /** 字段表service */
    @Resource
    private FlanceFormTmpFieldService flanceFormTmpFieldService;

    /** 业务模板记录表service */
    @Resource
    private FlanceFormRecServiceformService flanceFormRecServiceformService;

    /** 模板字段记录表 */
    @Resource
    private FlanceFormTmpTmpfieldService flanceFormTmpTmpfieldService;

    /** 字典表service */
    @Resource
    private FlanceFormTmpDicService flanceFormTmpDicServicel;

    /**
     * 根据serviceFormId生成业务表记录
     * @param serviceFormId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncServiceFormRec(String serviceFormId) {

        FlanceFormBizServiceform flanceFormBizServiceform = flanceFormBizServiceformService.findOne(serviceFormId);

        if (null == flanceFormBizServiceform) {
            throw new RuntimeException("找不到业务表id[" + serviceFormId + "]");
        }

        // 查询所有业务组列表，重新组装到vo对象
        List<FlanceFormBizGroup> flanceFormBizGroups = flanceFormBizGroupService.findAll(new HashMap<String, Object>(){{
            put("SERVICE_FORM_FK", serviceFormId);
        }});
        List<ServiceFormGroupVo> groupModels = Lists.newArrayList();
        flanceFormBizGroups.forEach(bizGroup -> {

            List<FlanceFormTmpTemplate> tmpTemplates = flanceFormTmpTemplateService.findAllByGroupId(bizGroup.getId());

            ServiceFormGroupVo groupModel = new ServiceFormGroupVo();
            groupModel.setGroupId(bizGroup.getId());
            groupModel.setGroupName(bizGroup.getGroupName());
            groupModel.setStep(bizGroup.getStep());
            groupModel.setStepIndex(bizGroup.getStepIndex());
            groupModel.setServiceFormTemplateVos(packageTemplates(tmpTemplates));
            groupModels.add(groupModel);
        });

        // 重建审批表
        ServiceFormVo serviceFormModel = new ServiceFormVo();
        serviceFormModel.setServiceFormId(serviceFormId);
        serviceFormModel.setServiceFormGroupVos(groupModels);

        String jsonFormObj = JSONObject.toJSONString(serviceFormModel, SerializerFeature.DisableCircularReferenceDetect);
        FlanceFormRecServiceform flanceFormRecServiceform = new FlanceFormRecServiceform();
        flanceFormRecServiceform.setRecordValue(jsonFormObj);
        flanceFormRecServiceform.setServiceFormFk(serviceFormId);

        flanceFormRecServiceformService.deleteByProperty("SERVICE_FORM_FK", serviceFormId);
        flanceFormRecServiceformService.save(flanceFormRecServiceform);
    }

    /**
     * 根据domain模型将template重新组装成vo对象
     * @param tmpTemplates
     * @return
     */
    private List<ServiceFormTemplateVo> packageTemplates(List<FlanceFormTmpTemplate> tmpTemplates) {
        List<ServiceFormTemplateVo> templateModels = Lists.newArrayList();
        tmpTemplates.forEach(tmpTemplate -> {

            List<FlanceFormTmpField> tmpFields = flanceFormTmpFieldService.findAllByTemplateId(tmpTemplate.getId());

            ServiceFormTemplateVo templateModel = new ServiceFormTemplateVo();
            templateModel.setTemplateId(tmpTemplate.getId());
            templateModel.setCode(tmpTemplate.getCode());
            templateModel.setIsList(tmpTemplate.getIsList());
            templateModel.setLimit(tmpTemplate.getLimit());
            templateModel.setTemplateName(tmpTemplate.getName());
            templateModel.setRemark(tmpTemplate.getRemark());
            templateModel.setServiceFormFieldVos(packageFields(tmpTemplate.getId(), tmpFields));

            templateModels.add(templateModel);
        });

        return templateModels;
    }

    /**
     * 根据domain模型将tmpField重新组装成vo对象
     * 8.单选框
     * 9.复选框
     * 10.下拉框-非级联模式-一次性全查模式
     * 11.下拉框-非级联模式-ajax动态加载模式
     * 12.下拉框-级联模式-一次性全查模式
     * 13.下拉框-级联模式-ajax动态加载模式
     * @param tmpFields
     * @return
     */
    private List<ServiceFormFieldVo> packageFields(String tempId, List<FlanceFormTmpField> tmpFields) {
        List<ServiceFormFieldVo> fieldModels = Lists.newArrayList();

        tmpFields.forEach(tmpField -> {
            FlanceFormTmpTmpfield flanceFormTmpTmpfield = flanceFormTmpTmpfieldService.findOneByProps(new HashMap<String, String>(){{
                put("FIELD_FK", tmpField.getId());
                put("TEMPLATE_FK", tempId);
            }});
            ServiceFormFieldVo serviceFormFieldModel = new ServiceFormFieldVo();
            serviceFormFieldModel.setFieldId(tmpField.getId());
            serviceFormFieldModel.setCode(tmpField.getCode());
            serviceFormFieldModel.setFieldType(tmpField.getFieldType());
            serviceFormFieldModel.setFieldName(tmpField.getName());
            serviceFormFieldModel.setIsBigText(tmpField.getIsBigText());
            serviceFormFieldModel.setIsstatic(tmpField.getIsstatic());
            serviceFormFieldModel.setTableCode(tmpField.getTableCode());
            serviceFormFieldModel.setFlanceFormTmpTmpfield(flanceFormTmpTmpfield);

            FlanceFormTmpDic flanceFormTmpDic = new FlanceFormTmpDic();

            /**
             * 8、9、10、12：是一次性将字典全查出来的模式
             * 11、13：是返回根节点字典，通过ajax调用接口查询的模式
             */
            switch (tmpField.getFieldType()) {

                case 8:
                case 10:
                case 12:
                case 9:
                    flanceFormTmpDic.setParentId(tmpField.getDicId());
// TODO                    flanceFormTmpDic.setChildren(flanceFormTmpDicServicel.queryAllByCode(flanceFormTmpDic));
                    serviceFormFieldModel.setFlanceFormTmpDic(flanceFormTmpDic);
                    break;
                case 11:
                case 13:
                    flanceFormTmpDic.setId(tmpField.getDicId());
                    serviceFormFieldModel.setFlanceFormTmpDic(flanceFormTmpDic);
                    break;
                default:
                    break;
            }

            fieldModels.add(serviceFormFieldModel);
        });

        return fieldModels;
    }

}
