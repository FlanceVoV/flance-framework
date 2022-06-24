package com.flance.tx.datasource.proxy.plugins;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ParameterUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.flance.tx.datasource.sqlexe.CTSqlExec;
import com.flance.tx.datasource.sqlexe.SqlExec;
import com.google.common.collect.Maps;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;

public class CTPaginationInnerInterceptor extends PaginationInnerInterceptor {

    @Override
    public boolean willDoQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        IPage<?> page = ParameterUtils.findPage(parameter).orElse(null);
        if (page == null || page.getSize() < 0 || !page.searchCount()) {
            return true;
        }

        BoundSql countSql;
        MappedStatement countMs = buildCountMappedStatement(ms, page.countId());
        if (countMs != null) {
            countSql = countMs.getBoundSql(parameter);
        } else {
            countMs = buildAutoCountMappedStatement(ms);
            String countSqlStr = autoCountSql(page, boundSql.getSql());
            PluginUtils.MPBoundSql mpBoundSql = PluginUtils.mpBoundSql(boundSql);
            countSql = new BoundSql(countMs.getConfiguration(), countSqlStr, mpBoundSql.parameterMappings(), parameter);
            PluginUtils.setAdditionalParameter(countSql, mpBoundSql.additionalParameters());
        }

        SqlExec sqlExec = new CTSqlExec();

        sqlExec.doSelect(countSql.getSql(), Maps.newHashMap());

        return continuePage(page);
    }

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        super.beforeQuery(executor, ms, parameter, rowBounds, resultHandler, boundSql);
    }
}
