package com.flance.components.form.domain.dform.service.impl;

import com.flance.components.form.domain.dform.model.po.FlanceFormTmpFieldvalue;
import com.flance.components.form.domain.dform.repository.FlanceFormTmpFieldvalueDao;
import com.flance.components.form.domain.dform.service.FlanceFormTmpFieldvalueService;
import com.flance.jdbc.jpa.web.service.BaseWebDomainService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlanceFormTmpFieldvalueServiceImpl extends BaseWebDomainService<FlanceFormTmpFieldvalue, FlanceFormTmpFieldvalue, FlanceFormTmpFieldvalue, FlanceFormTmpFieldvalue, String> implements FlanceFormTmpFieldvalueService {

    private FlanceFormTmpFieldvalueDao flanceFormTmpFieldvalueDao;



    @Override
    public List<FlanceFormTmpFieldvalue> findAllByMainflowIdAndGroupIdAndTmpId(String mainflowId, String groupId, String templateId) {
        return flanceFormTmpFieldvalueDao.findAllByMainflowIdAndGroupIdAndTmpId(mainflowId, groupId, templateId);
    }
}
