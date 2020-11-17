package com.flance.components.form.infrastructure.sync;

/**
 * 同步用户申报记录表service
 * @author jhf
 */
public interface FlanceFormMainflowFormSyncService {

    /**
     * 根据流程id初始化用户申报记录表
    * @param mainflowId
     */
    void syncMainflowFormRec(String mainflowId);

}
