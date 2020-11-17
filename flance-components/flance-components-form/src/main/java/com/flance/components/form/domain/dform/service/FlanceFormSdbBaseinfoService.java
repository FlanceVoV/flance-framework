package com.flance.components.form.domain.dform.service;

import com.flance.components.form.domain.dform.model.po.FlanceFormSdbBaseinfo;
import com.flance.jdbc.jpa.web.service.IBaseWebDomainService;

import java.util.List;

public interface FlanceFormSdbBaseinfoService extends IBaseWebDomainService<FlanceFormSdbBaseinfo, FlanceFormSdbBaseinfo, String> {

    List<FlanceFormSdbBaseinfo> findAllByMainformFk(String mainformFk);

}
