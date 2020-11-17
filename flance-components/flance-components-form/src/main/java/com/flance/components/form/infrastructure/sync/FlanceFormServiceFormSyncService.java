package com.flance.components.form.infrastructure.sync;

/**
 * 根据业务表单id生成业务表单记录
 * @author jhf
 */
public interface FlanceFormServiceFormSyncService {

    /**
     * 根据业务表单id生成业务表单记录
     * @param serviceFormId
     */
    void syncServiceFormRec(String serviceFormId);

}
