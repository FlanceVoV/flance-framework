package com.flance.components.form.domain.dform.service;

import com.flance.components.form.domain.dform.model.po.FlanceFormSdbFile;
import com.flance.jdbc.jpa.web.service.IBaseWebDomainService;

import java.util.List;

public interface FlanceFormSdbFileService extends IBaseWebDomainService<FlanceFormSdbFile, FlanceFormSdbFile, String> {

    List<FlanceFormSdbFile> findAllByMainformFk(String mainformFk);

}
