package com.flance.components.form.infrastructure.sync.impl;

import com.alibaba.fastjson.JSONObject;
import com.flance.components.form.domain.dform.model.po.*;
import com.flance.components.form.domain.dform.model.vo.ServiceModelBindVo;
import com.flance.components.form.domain.dform.service.*;
import com.flance.components.form.infrastructure.sync.FlanceFormReflectService;
import com.flance.components.form.infrastructure.utils.Constent;
import com.flance.components.form.infrastructure.utils.TableCodeUtil;
import com.google.common.collect.Maps;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 保存或修改用户申报记录service
 * @author jhf
 */
@Service
public class FlanceFormReflectServiceImpl implements FlanceFormReflectService {

    @Resource
    private ApplicationContext applicationContext;

    /** 业务关联service */
    @Resource
    private FlanceFormBizBusinessService flanceFormBizBusinessService;

    /** 字段service */
    @Resource
    private FlanceFormTmpFieldService flanceFormTmpFieldService;

    /** 模板service */
    @Resource
    private FlanceFormTmpTemplateService flanceFormTmpTemplateService;

    /** 字段值service */
    @Resource
    private FlanceFormTmpFieldvalueService flanceFormTmpFieldvalueService;

    /** 字段大值service */
    @Resource
    private FlanceFormTmpFieldbvalueService flanceFormTmpFieldbvalueService;

    /** 字段字典值service */
    @Resource
    private FlanceFormTmpFielddvalueService flanceFormTmpFielddvalueService;

    @Resource
    private FlanceFormSdbFileService flanceFormSdbFileService;

    @Resource
    private FlanceFormTmpDicService flanceFormTmpDicService;

    /**
     * 按组提交数据
     * @param mainFlowId
     * @param flanceFormBizGroup
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveByGroup(String mainFlowId, FlanceFormBizGroup flanceFormBizGroup) {
        // 获取模板
        List<FlanceFormTmpTemplate> templates = flanceFormBizGroup.getFlanceFormTmpTemplates();
        templates.forEach(template ->invoke(mainFlowId, flanceFormBizGroup.getId(), template));
    }

    /**
     * 反射提交模板数据
     * 包括动态、非动态字段的数据
     * @param mainformId
     * @param tmpTemplate
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void invoke(String mainformId, String groupId, FlanceFormTmpTemplate tmpTemplate) {

        Map<String, List<Map<String, Object>>> objectMap = Maps.newHashMap();

        // 查询初始化的业务信息
        FlanceFormBizBusiness flanceFormBizBusiness = flanceFormBizBusinessService.findOneByProps(new HashMap<String, String>(3){{
            put("MAINFORM_FK", mainformId);
            put("GROUP_FK", groupId);
            put("TEMPLATE_FK", tmpTemplate.getId());
        }});

        if (null == flanceFormBizBusiness) {
            throw new RuntimeException("业务查询失败，原因找不到用户的业务初始化信息，流程主表id[" + mainformId + "]组id[" + groupId + "]模板id[" + tmpTemplate.getId() + "]");
        }

        if (flanceFormBizBusiness.getCanEdit() != 1) {
            throw new RuntimeException("业务编辑失败，原因该模板不允许编辑，流程主表id[" + mainformId + "]组id[" + groupId + "]模板id[" + tmpTemplate.getId() + "]");
        }

        List<FlanceFormTmpField> fields = tmpTemplate.getFlanceFormTmpFields();

        // 查询模板，并赋值
        FlanceFormTmpTemplate flanceFormTmpTemplate = flanceFormTmpTemplateService.findOne(tmpTemplate.getId());
        flanceFormTmpTemplate.setFlanceFormTmpFields(tmpTemplate.getFlanceFormTmpFields());

        String objGroup = UUID.randomUUID().toString();
        Map<String, String> tempMap = Maps.newHashMap();

        for (FlanceFormTmpField tmpField: fields) {

            if (null == tmpField || StringUtils.isEmpty(tmpField.getId())) {
                throw new RuntimeException("动态字段处理失败，模板[" + flanceFormTmpTemplate.getName() + "]下对象为空或[tmpField.id]为空");
            }
            tmpField.getFlanceFormTmpFieldvalue().setBusinessFk(flanceFormBizBusiness.getId());

            // 查询字段，并赋值
            FlanceFormTmpField flanceFormTmpField = flanceFormTmpFieldService.findOne(tmpField.getId());
            flanceFormTmpField.setFlanceFormTmpFieldvalue(tmpField.getFlanceFormTmpFieldvalue());

            if (null != tempMap.get(tmpField.getId())) {
                tempMap.clear();
                objGroup = UUID.randomUUID().toString();
            }

            tempMap.put(tmpField.getId(), "");

            /**
             * 针对静态字段单独处理
             * 将所有的静态表的字段提取出来，封装成一个map对象，并通过反射生成真实对象，通过反射调研service的insert、update方法
             */
            if (flanceFormTmpField.getIsstatic() == 1) {
                staticField(flanceFormTmpField, objectMap);
            } else {
                unStaticField(flanceFormTmpField, flanceFormTmpTemplate.getIsList(), objGroup);
            }
        }

        staticFieldSave(mainformId, objectMap);

    }

    /**
     * 静态字段存储
     * @param mainformId
     * @param objectMap
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void staticFieldSave(String mainformId, Map<String, List<Map<String, Object>>> objectMap) {
        // 拿到objectMap，反射生成对象存储到静态表里面
        for (String tableCode : objectMap.keySet()) {
            ServiceModelBindVo serviceModelBind = TableCodeUtil.getServiceModelBind(tableCode);
            List<Map<String, Object>> objList = objectMap.get(tableCode);

            String arrStr = JSONObject.toJSONString(objList);

            List list = null;

            try {
                list = JSONObject.parseArray(arrStr, Class.forName(serviceModelBind.getModelName()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException("在tableCode下[" + tableCode + "]找不到类[" + serviceModelBind.getModelName() + "]");
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("在tableCode下[" + tableCode + "]类[" + serviceModelBind.getModelName() + "]生成对象失败");
            }

            list.forEach(flanceFormSdb -> {

                // 获取id用以判断保存或修改
                Field idField = null;
                Object id = null;
                try {
                    idField = flanceFormSdb.getClass().getDeclaredField("id");
                    idField.setAccessible(true);
                    id = idField.get(flanceFormSdb);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                    throw new RuntimeException("在tableCode下[" + tableCode + "]类[" + serviceModelBind.getModelName() + "]下找不到字段[id]");
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("在tableCode下[" + tableCode + "]类[" + serviceModelBind.getModelName() + "]字段[id]获取失败");
                }

                Object objService = applicationContext.getBean(serviceModelBind.getServiceName());

                try {
                    Field mainformFk = flanceFormSdb.getClass().getDeclaredField("mainformFk");
                    mainformFk.setAccessible(true);
                    mainformFk.set(flanceFormSdb, mainformId);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                    throw new RuntimeException("在tableCode下[" + tableCode + "]类[" + serviceModelBind.getModelName() + "]下找不到字段[mainformFk]");
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("在tableCode下[" + tableCode + "]类[" + serviceModelBind.getModelName() + "]字段[mainFlowFK]设置失败");
                }

                // 反射获取方法并调用
                Method method = null;
                try {
                    if (StringUtils.isEmpty(id)) {
                        method = objService.getClass().getMethod("insert", flanceFormSdb.getClass());
                    } else {
                        method = objService.getClass().getMethod("updateByPrimaryKeySelective", flanceFormSdb.getClass());
                    }
                    method.invoke(objService, flanceFormSdb);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    throw new RuntimeException("在tableCode下[" + tableCode + "]类[" + serviceModelBind.getModelName() + "]下找不到方法[" + method.getName() + "]");
                }  catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("在tableCode下[" + tableCode + "]类[" + serviceModelBind.getModelName() + "]执行方法失败[" + method.getName() + "]");
                }

            });

        }
    }

    /**
     * 静态字段设置
     * @param flanceFormTmpField
     * @param objectMap
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void staticField(FlanceFormTmpField flanceFormTmpField, Map<String, List<Map<String, Object>>> objectMap) {

        // 构建对象map
        List<Map<String, Object>> objList = objectMap.get(flanceFormTmpField.getTableCode());

        /**
         * 如果没有这个对象的字段集合，则新增一个；否则在已有的对象字段集合中添加一个字段及值
         * 要求list里的对象及字段必须按照顺序提交进来
         */
        Map<String, Object> obj = Maps.newHashMap();
        if (null == objList) {
            objList = new ArrayList<>();
            objList.add(obj);
        } else {
            obj = objList.get(objList.size() - 1);
        }
        if (obj.get(flanceFormTmpField.getCode()) != null) {
            obj = Maps.newHashMap();
            objList.add(obj);
        }

        if (null != flanceFormTmpField.getFlanceFormTmpFieldvalue().getFlanceFormTmpFielddvalues() &&
                flanceFormTmpField.getFlanceFormTmpFieldvalue().getFlanceFormTmpFielddvalues().size() > 0) {
            String dicFk = flanceFormTmpField.getFlanceFormTmpFieldvalue().getFlanceFormTmpFielddvalues().get(0).getDicFk();
            flanceFormTmpField.getFlanceFormTmpFieldvalue().setCommonValue(dicFk);
        }

        obj.put(flanceFormTmpField.getCode(), flanceFormTmpField.getFlanceFormTmpFieldvalue().getCommonValue());
        objectMap.put(flanceFormTmpField.getTableCode(), objList);

    }

    /**
     * 非静态字段设置及存储
     * @param flanceFormTmpField
     * @param isList
     * @param objGroup
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unStaticField(FlanceFormTmpField flanceFormTmpField, Short isList, String objGroup) {

        // 非静态字段直接存储
        FlanceFormTmpFieldvalue flanceFormTmpFieldvalue = flanceFormTmpField.getFlanceFormTmpFieldvalue();
        // 判断字段外键是否为空
        if (StringUtils.isEmpty(flanceFormTmpFieldvalue.getFieldFk())) {
            throw new RuntimeException("fieldFk为空，字段name[" + flanceFormTmpField.getName() + "]，字段code[" + flanceFormTmpField.getCode() + "]");
        }

        // 如果是list则进行分组设置
        if (isList == 1) {
            flanceFormTmpFieldvalue.setObjGroup(objGroup);
        }

        /**
         * 1. 先保存fieldValue表
         * 2. 再判断关联保存fieldbValue表或者fielddValue表
         * * 可以优化批量插入，批量更新
         */
        try {
            if (StringUtils.isEmpty(flanceFormTmpFieldvalue.getId())) {
                flanceFormTmpFieldvalueService.save(flanceFormTmpFieldvalue);
            } else {
                flanceFormTmpFieldvalueService.updateNotNull(flanceFormTmpFieldvalue, flanceFormTmpFieldvalue.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存或修改动态表出错，字段name[" + flanceFormTmpField.getName() + "]，字段code[" + flanceFormTmpField.getCode() + "]");
        }

        /**
         * 1. isBigText(1.表示该字段用大值blob存储，2.表示该字段正常存储)
         * 2. dicId(不为空表示该字段用关联字典表存储，空表示正常字典)
         * * 可以优化批量插入，批量更新
         */
        if (flanceFormTmpField.getIsBigText() == 1) {
            FlanceFormTmpFieldbvalue flanceFormTmpFieldbvalue = flanceFormTmpFieldvalue.getFlanceFormTmpFieldbvalue();
            flanceFormTmpFieldbvalue.setFieldvalueFk(flanceFormTmpFieldvalue.getId());
            flanceFormTmpFieldbvalueService.deleteByProperty("FIELDVALUE_FK", flanceFormTmpFieldvalue.getId());
            flanceFormTmpFieldbvalueService.save(flanceFormTmpFieldbvalue);
        } else if(!StringUtils.isEmpty(flanceFormTmpField.getDicId())) {
            List<FlanceFormTmpFielddvalue> fielddvalues = flanceFormTmpFieldvalue.getFlanceFormTmpFielddvalues();
            flanceFormTmpFielddvalueService.deleteByProperty("FIELDVALUE_FK", flanceFormTmpFieldvalue.getId());
            fielddvalues.forEach(flanceFormTmpFielddvalue -> {
                flanceFormTmpFielddvalue.setFieldvalueFk(flanceFormTmpFieldvalue.getId());
                flanceFormTmpFielddvalueService.save(flanceFormTmpFielddvalue);
            });
        }


        /**
         * 附件上传
         */
        if (flanceFormTmpField.getFieldType().equals(Constent.SHORT_TYPE_7)) {
            String id = flanceFormTmpFieldvalue.getId();
            // 获得文件对象
            List<FlanceFormSdbFile> flanceFormSdbFiles = flanceFormTmpFieldvalue.getFlanceFormSdbFiles();
            flanceFormSdbFiles.forEach(file -> {
                if (StringUtils.isEmpty(file.getId())) {
                    file.setFieldvalueFk(id);
                    flanceFormSdbFileService.save(file);
                }
            });
        }
    }
}
