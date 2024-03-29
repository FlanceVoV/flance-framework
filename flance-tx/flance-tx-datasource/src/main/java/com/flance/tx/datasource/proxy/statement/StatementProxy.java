package com.flance.tx.datasource.proxy.statement;

import com.flance.tx.common.utils.StringUtils;
import com.flance.tx.datasource.proxy.connection.AbstractConnectionProxy;
import com.flance.tx.datasource.proxy.connection.ConnectionProxy;

import java.sql.SQLException;
import java.sql.Statement;

public abstract class StatementProxy<T extends Statement> extends AbstractStatementProxy<T> {

    public StatementProxy(ConnectionProxy connectionProxy, T targetStatement) throws SQLException {
        super(connectionProxy, targetStatement);
    }

    public StatementProxy(AbstractConnectionProxy connectionProxy, T targetStatement, String execSql) {
        super(connectionProxy, targetStatement, execSql);
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        this.execSql = sql;
        return super.executeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        this.execSql = sql;
        return super.executeUpdate(sql, columnIndexes);
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        this.execSql = sql;
        return super.executeUpdate(sql, columnNames);
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        this.execSql = sql;
        return super.execute(sql, autoGeneratedKeys);
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        this.execSql = sql;
        return super.execute(sql, columnIndexes);
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        this.execSql = sql;
        return super.execute(sql, columnNames);
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        if (StringUtils.isNotBlank(execSql)) {
            execSql += "; " + sql;
        } else {
            execSql = sql;
        }
        targetStatement.addBatch(sql);
    }

    @Override
    public int[] executeBatch() throws SQLException {
        return super.executeBatch();
    }
}
