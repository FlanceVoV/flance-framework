package com.flance.components.form.domain.dform.service.impl;

import com.flance.components.form.domain.dform.model.po.FlanceFormSdbFile;
import com.flance.components.form.domain.dform.parser.FlanceFormSdbFileParser;
import com.flance.components.form.domain.dform.repository.FlanceFormSdbFileDao;
import com.flance.components.form.domain.dform.service.FlanceFormSdbFileService;
import com.flance.jdbc.jpa.web.service.BaseWebDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class FlanceFormSdbFileServiceImpl extends BaseWebDomainService<FlanceFormSdbFile, FlanceFormSdbFile, FlanceFormSdbFile, FlanceFormSdbFile, String> implements FlanceFormSdbFileService {

    private FlanceFormSdbFileDao flanceFormSdbFileDao;

    private FlanceFormSdbFileParser flanceFormSdbFileParser;

    @Autowired
    public void setFlanceFormSdbFileDao(FlanceFormSdbFileDao flanceFormSdbFileDao) {
        this.flanceFormSdbFileDao = flanceFormSdbFileDao;
        super.setBaseDao(flanceFormSdbFileDao);
    }

    @Autowired
    public void setFlanceFormSdbFileParser(FlanceFormSdbFileParser flanceFormSdbFileParser) {
        this.flanceFormSdbFileParser = flanceFormSdbFileParser;
        super.setBaseParser(flanceFormSdbFileParser);
    }

    @Override
    public List<FlanceFormSdbFile> findAllByMainformFk(String mainformFk) {
        return super.findAll(new HashMap<String, Object>(){{
            put("mainformFk", mainformFk);
        }});
    }
}
