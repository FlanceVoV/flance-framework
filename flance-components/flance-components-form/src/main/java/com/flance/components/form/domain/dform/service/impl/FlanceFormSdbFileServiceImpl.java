package com.flance.components.form.domain.dform.service.impl;

import com.flance.components.form.domain.dform.model.po.FlanceFormSdbFile;
import com.flance.components.form.domain.dform.service.FlanceFormSdbFileService;
import com.flance.jdbc.jpa.web.service.BaseWebDomainService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class FlanceFormSdbFileServiceImpl extends BaseWebDomainService<FlanceFormSdbFile, FlanceFormSdbFile, FlanceFormSdbFile, FlanceFormSdbFile, String> implements FlanceFormSdbFileService {

    @Override
    public List<FlanceFormSdbFile> findAllByMainformFk(String mainformFk) {
        return super.findAll(new HashMap<String, Object>(){{
            put("mainformFk", mainformFk);
        }});
    }
}
