package com.flance.tx.datasource.sqlexe;


import java.util.List;
import java.util.Map;

public interface SqlExec {

    List<Object> doSelectBase(String sql, Map<Integer, Object> params);

    List<Map> doSelect(String sql, Map<Integer, Object> params);

    int doUpdate(String sql, Map<Integer, Object> params);

    int doDelete(String sql, Map<Integer, Object> params);

    int doInsert(String sql, Map<Integer, Object> params);

}
