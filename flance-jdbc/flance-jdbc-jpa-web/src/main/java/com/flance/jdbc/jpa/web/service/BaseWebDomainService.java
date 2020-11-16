package com.flance.jdbc.jpa.web.service;

import com.flance.jdbc.jpa.common.Operator;
import com.flance.jdbc.jpa.dao.BaseDao;
import com.flance.jdbc.jpa.page.PageResponse;
import com.flance.jdbc.jpa.parser.BaseParser;
import com.flance.jdbc.jpa.service.BaseDomainService;
import com.flance.jdbc.jpa.service.IBaseDomainService;
import com.flance.web.common.service.IService;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * jpa-web service 整合
 * @author jhf
 */
public abstract class BaseWebDomainService<PO, DTO, VO, DO, ID extends Serializable> extends BaseDomainService<PO, DTO, VO, DO, ID> implements IBaseDomainService<PO, DTO, ID>, IBaseWebDomainService<PO, DTO, ID> {

    private BaseDao<PO, ID> baseDao;

    private BaseParser<PO, DTO, VO, DO> baseParser;

    @Override
    protected void setBaseDao(BaseDao baseDao) {
        super.setBaseDao(baseDao);
        this.baseDao = baseDao;
    }

    @Override
    public void setBaseParser(BaseParser<PO, DTO, VO, DO> baseParser) {
        super.setBaseParser(baseParser);
        this.baseParser = baseParser;
    }

    @Override
    public PageResponse<DTO> findPage(Map<String, Object> searchMap) {
        return super.customSearch(searchMap);
    }

    @Override
    public List<DTO> findAll(Map<String, Object> searchMap) {
        Table<String, Operator, Object> table = HashBasedTable.create();
        searchMap.entrySet().stream().forEach(entry -> table.put(entry.getKey(), Operator.AND, entry.getValue()));
        return super.findAll(table);
    }
}
