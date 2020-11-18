package com.flance.components.form.infrastructure.sync.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.flance.components.form.domain.dform.model.po.*;
import com.flance.components.form.domain.dform.model.vo.MainflowFormGroupVo;
import com.flance.components.form.domain.dform.model.vo.MainflowFormTemplateVo;
import com.flance.components.form.domain.dform.model.vo.MainflowFormVo;
import com.flance.components.form.domain.dform.model.vo.ServiceModelBindVo;
import com.flance.components.form.domain.dform.service.*;
import com.flance.components.form.infrastructure.sync.FlanceFormMainflowFormSyncService;
import com.flance.components.form.infrastructure.utils.Constent;
import com.flance.components.form.infrastructure.utils.TableCodeUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 同步用户申报记录表service
 * @author jhf
 */
@Service
public class FlanceFormMainflowFormSyncServiceImpl implements FlanceFormMainflowFormSyncService {

    Logger logger = LoggerFactory.getLogger(FlanceFormMainflowFormSyncService.class);

    @Resource
    private ApplicationContext applicationContext;

    /** 用户申报主表service */
    @Resource
    private FlanceFormBizMainflowService flanceFormBizMainflowService;

    /** 业务申报表单service */
    @Resource
    private FlanceFormBizServiceformService flanceFormBizServiceformService;

    /** 用户业务关联信息service */
    @Resource
    private FlanceFormBizBusinessService flanceFormBizBusinessService;

    /** 字段值service */
    @Resource
    private FlanceFormTmpFieldvalueService flanceFormTmpFieldvalueService;

    /** 业务组service */
    @Resource
    private FlanceFormBizGroupService flanceFormBizGroupService;

    /** 模板表service */
    @Resource
    private FlanceFormTmpTemplateService flanceFormTmpTemplateService;

    /** 字段表service */
    @Resource
    private FlanceFormTmpFieldService flanceFormTmpFieldService;

    /** 字典表service */
    @Resource
    private FlanceFormTmpFielddvalueService flanceFormTmpFielddvalueService;

    /** 字段大值service */
    @Resource
    private FlanceFormTmpFieldbvalueService flanceFormTmpFieldbvalueService;

    /** 申报记录表service */
    @Resource
    private FlanceFormRecMainflowformService flanceFormRecMainflowformService;

    /** 模板字段记录表 */
    @Resource
    private FlanceFormTmpTmpfieldService flanceFormTmpTmpfieldService;

    @Resource
    private FlanceFormSdbFileService flanceFormSdbFileService;

    /**
     * 根据流程主表，同步用户的数据库信息到记录表
     * @param mainflowId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncMainflowFormRec(String mainflowId) {

        FlanceFormBizMainflow flanceFormBizMainflow = flanceFormBizMainflowService.findOne(mainflowId);

        if (null == flanceFormBizMainflow) {
            throw new RuntimeException("找不到流程主表id[" + mainflowId + "]");
        }

        List<FlanceFormBizGroup> bizGroups = flanceFormBizGroupService.findAll(new HashMap<String, Object>(1){{
            put("serviceFormFk", flanceFormBizMainflow.getServiceformPk());
        }});

        List<MainflowFormGroupVo> formGroupModels = Lists.newArrayList();

        bizGroups.forEach(bizGroup -> {
            List<FlanceFormTmpTemplate> flanceFormTmpTemplates = bizGroup.getFlanceFormTmpTemplates();
            MainflowFormGroupVo mainflowFormGroupModel = new MainflowFormGroupVo();
            mainflowFormGroupModel.setGroupId(bizGroup.getId());
            mainflowFormGroupModel.setTemplateVos(packageTemplates(mainflowId, bizGroup.getId(), flanceFormTmpTemplates));
            formGroupModels.add(mainflowFormGroupModel);
        });

        MainflowFormVo mainflowFormModel = new MainflowFormVo();

        mainflowFormModel.setGroupVos(formGroupModels);
        mainflowFormModel.setMainflowFormId(mainflowId);

        FlanceFormRecMainflowform flanceFormRecMainflowform = new FlanceFormRecMainflowform();
        flanceFormRecMainflowform.setMainflowFk(mainflowId);
        flanceFormRecMainflowform.setRecordValue(JSONObject.toJSONString(mainflowFormModel, SerializerFeature.DisableCircularReferenceDetect));

        flanceFormRecMainflowformService.delete(mainflowId);
        flanceFormRecMainflowformService.save(flanceFormRecMainflowform);

    }

    /**
     * 根据domain模型将template重新组装成vo对象
     * @param mainflowId
     * @param tmpTemplates
     * @return
     */
    private List<MainflowFormTemplateVo> packageTemplates(String mainflowId, String groupId , List<FlanceFormTmpTemplate> tmpTemplates) {

        List<MainflowFormTemplateVo> templateModels = Lists.newArrayList();

        tmpTemplates.forEach(tmpTemplate -> {

            List<FlanceFormTmpFieldvalue> tmpFields = flanceFormTmpFieldvalueService.findAllByMainflowIdAndGroupIdAndTmpId(mainflowId, groupId, tmpTemplate.getId());

            // 查询业务记录
            FlanceFormBizBusiness flanceFormBizBusiness = flanceFormBizBusinessService.findOneByProps(new HashMap<String, String>(){{
                put("mainflowFk", mainflowId);
                put("groupFk", groupId);
                put("templateFk", tmpTemplate.getId());
            }});

            // 设置模板动态字段的值
            setFieldValues(tmpFields);

            // 设置模板静态字段的值
            setStaticFields(mainflowId, tmpTemplate, tmpFields);

            // 内存排序
            // tmpFields.sort((x, y) -> (x.getFieldSort() - y.getFieldSort()));

            // 简化写法
            tmpFields.sort(Comparator.comparing(FlanceFormTmpFieldvalue::getFieldSort));

            MainflowFormTemplateVo templateModel = new MainflowFormTemplateVo();
            templateModel.setTemplateId(tmpTemplate.getId());
            templateModel.setFlanceFormTmpFieldvalues(tmpFields);
            templateModel.setCanEdit(flanceFormBizBusiness.getCanEdit());
            templateModel.setRejectMark(flanceFormBizBusiness.getRejectMark());
            templateModels.add(templateModel);
        });

        return templateModels;
    }

    /**
     * 处理字段值
     * 根据字段类型将值取出来( 动静态值处理)
     * 1. 大值处理
     * 2. 字典值处理
     * 3. 普通值处理
     * 4. 文件值处理
     *
     * @param fieldValues
     */
    private void setFieldValues(List<FlanceFormTmpFieldvalue> fieldValues) {

        fieldValues.forEach(fieldValue -> {

            FlanceFormTmpField flanceFormTmpField = flanceFormTmpFieldService.findOne(fieldValue.getFieldFk());

            // 字典类型，如果是字典类型则去字典字段关联表里面查询
            if (!StringUtils.isEmpty(flanceFormTmpField.getDicId())) {
                List<FlanceFormTmpFielddvalue> flanceFormTmpFielddvalues = flanceFormTmpFielddvalueService.findAll(new HashMap<String, Object>(){{
                    put("fieldvalueFk", fieldValue.getId());
                }});
                fieldValue.setFlanceFormTmpFielddvalues(flanceFormTmpFielddvalues);
            }

            // 大值类型，如果是大值类型则去大值字段关联表里面查询
            if (flanceFormTmpField.getIsBigText().equals(Constent.SHORT_TYPE_1)) {
                FlanceFormTmpFieldbvalue flanceFormTmpFieldbvalue = flanceFormTmpFieldbvalueService.findOneByProp("FIELDVALUE_FK", fieldValue.getId());
                fieldValue.setFlanceFormTmpFieldbvalue(flanceFormTmpFieldbvalue);
            }

            // 文件类型
            if (flanceFormTmpField.getFieldType().equals(Constent.SHORT_TYPE_7)) {
                List<FlanceFormSdbFile> flanceFormSdbFiles = flanceFormSdbFileService.findAll(new HashMap<String, Object>(){{
                    put("fieldvalueFk", fieldValue.getId());
                }});
                fieldValue.setFlanceFormSdbFiles(flanceFormSdbFiles);
            }

        });

    }

    /**
     * 设置静态字段的值
     * @param tmpFields
     * @param template
     * @param mainflowId
     */
    private void setStaticFields(String mainflowId, FlanceFormTmpTemplate template, List<FlanceFormTmpFieldvalue> tmpFields) {

        List<FlanceFormTmpField> staticFields = flanceFormTmpFieldService.findAllByTemplateIdAndIsStatic(template.getId(), Constent.SHORT_TYPE_1);

        Map<String, List<Object>> staticObjectMap = Maps.newHashMap();

        // 如果是list单独处理
        staticFields.forEach(staticField -> {
            // 查询处模板字段的关联关系(里面有字段排序等信息)
            FlanceFormTmpTmpfield tmpTmpfield = flanceFormTmpTmpfieldService.findOneByProps(new HashMap<String, String>(){{
                put("fieldFk", staticField.getId());
                put("templateFk", template.getId());
            }});
            String tableCode = staticField.getTableCode();

            List resultList = staticObjectMap.get(tableCode);
            // 放入临时缓存,防止多字段重复查询数据库
            if (resultList == null) {
                ServiceModelBindVo serviceModelBind = TableCodeUtil.getServiceModelBind(tableCode);
                Object objService = applicationContext.getBean(serviceModelBind.getServiceName());
                try {
                    Method findAllByMainformFk = objService.getClass().getMethod("findAllByMainformFk", String.class);
                    Object obj = findAllByMainformFk.invoke(objService, mainflowId);
                    if (null != obj) {
                        resultList = (List) findAllByMainformFk.invoke(objService, mainflowId);
                        staticObjectMap.put(tableCode, resultList);
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    throw new RuntimeException("在tableCode下[" + tableCode + "]类[" + objService.getClass() + "]没有方法[findAllByMainformFk]");
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("在tableCode下[" + tableCode + "]类[" + objService.getClass() + "]方法[findAllByMainformFk]调用失败");
                }
            }

            /**
             * 取出静态表的列表(默认都按照列表处理,base_info的list应该只有一条)
             * 因为共属于一个template,所以应该是同一组,故可以使用同一个objGroup
             */
            resultList.forEach(result -> {
                String objGroup = "";
                try {
                    Field idField = result.getClass().getDeclaredField("id");
                    idField.setAccessible(true);
                    objGroup = idField.get(result).toString();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                    throw new RuntimeException("在tableCode下[" + tableCode + "]类[" + result.getClass() + "]没有字段[id]");
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("在tableCode下[" + tableCode + "]类[" + result.getClass() + "]字段[id]取值失败");
                }

                try {
                    Field field = result.getClass().getDeclaredField(staticField.getCode());
                    field.setAccessible(true);
                    Object value = field.get(result);
                    if (null != value) {
                        FlanceFormTmpFieldvalue flanceFormTmpFieldvalue = new FlanceFormTmpFieldvalue();
                        flanceFormTmpFieldvalue.setFieldFk(staticField.getId());
                        flanceFormTmpFieldvalue.setFieldSort(tmpTmpfield.getSort());
                        flanceFormTmpFieldvalue.setCommonValue(value.toString());
                        if (StringUtils.isEmpty(staticField.getDicId())) {
                            List<FlanceFormTmpFielddvalue> flanceFormTmpFielddvalues = new ArrayList<>();
                            FlanceFormTmpFielddvalue flanceFormTmpFielddvalue = new FlanceFormTmpFielddvalue();
                            flanceFormTmpFielddvalue.setDicFk(flanceFormTmpFieldvalue.getCommonValue());
                            flanceFormTmpFielddvalues.add(flanceFormTmpFielddvalue);
                            flanceFormTmpFieldvalue.setFlanceFormTmpFielddvalues(flanceFormTmpFielddvalues);
                        }
                        flanceFormTmpFieldvalue.setObjGroup(objGroup);
                        tmpFields.add(flanceFormTmpFieldvalue);
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                    throw new RuntimeException("在tableCode下[" + tableCode + "]类[" + result.getClass() + "]没有字段[" + staticField.getCode() + "]");
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("在tableCode下[" + tableCode + "]类[" + result.getClass() + "]字段[" + staticField.getCode() + "]取值失败");
                }

            });


        });

    }

}
