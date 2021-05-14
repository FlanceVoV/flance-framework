package com.flance.jdbc.jpa.simple.components.sys.service.impl;


import com.flance.jdbc.jpa.simple.components.sys.entity.SysDictionary;
import com.flance.jdbc.jpa.simple.components.sys.repository.SysDictionaryDao;
import com.flance.jdbc.jpa.simple.components.sys.service.SysDictionaryService;
import com.flance.jdbc.jpa.simple.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 业务字典实现
 * @author jhf
 */
@Service
public class SysDictionaryServiceImpl extends BaseService<SysDictionary, Long> implements SysDictionaryService {

    SysDictionaryDao sysDictionaryDao;

    @Autowired
    public void setSysDictionaryDao(SysDictionaryDao sysDictionaryDao) {
        this.sysDictionaryDao = sysDictionaryDao;
        super.setBaseDao(sysDictionaryDao);
    }

}
