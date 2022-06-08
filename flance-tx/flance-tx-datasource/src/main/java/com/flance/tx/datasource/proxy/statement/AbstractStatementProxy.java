package com.flance.tx.datasource.proxy.statement;

import com.flance.tx.datasource.proxy.connection.AbstractConnectionProxy;
import com.flance.tx.datasource.proxy.connection.ConnectionProxy;

import java.sql.*;

/**
 * 预处理Statement代理
 * @author jhf
 */
public abstract class AbstractStatementProxy<T extends Statement> implements Statement {

    /**
     * 连接代理
     */
    protected AbstractConnectionProxy connectionProxy;

    /**
     * 目标statement
     */
    protected T targetStatement;

    /**
     * 执行sql
     */
    protected String execSql;

    public AbstractStatementProxy(ConnectionProxy connectionProxy, T targetStatement) throws SQLException {
        this(connectionProxy, targetStatement, null);
    }

    public AbstractStatementProxy(AbstractConnectionProxy connectionProxy, T targetStatement, String execSql) {
        this.connectionProxy = connectionProxy;
        this.targetStatement = targetStatement;
        this.execSql = execSql;
    }

    public AbstractConnectionProxy getConnectionProxy() {
        return connectionProxy;
    }

    public String getExecSql() {
        return execSql;
    }


    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        return targetStatement.executeQuery(sql);
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        return targetStatement.executeUpdate(sql);
    }

    @Override
    public void close() throws SQLException {
        targetStatement.close();
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return targetStatement.getMaxFieldSize();
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        targetStatement.setMaxFieldSize(max);
    }

    @Override
    public int getMaxRows() throws SQLException {
        return targetStatement.getMaxRows();
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        targetStatement.setMaxRows(max);
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        targetStatement.setEscapeProcessing(enable);
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return targetStatement.getQueryTimeout();
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        targetStatement.setQueryTimeout(seconds);
    }

    @Override
    public void cancel() throws SQLException {
        targetStatement.cancel();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return targetStatement.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        targetStatement.clearWarnings();
    }

    @Override
    public void setCursorName(String name) throws SQLException {
        targetStatement.setCursorName(name);
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        return targetStatement.execute(sql);
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return targetStatement.getResultSet();
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return targetStatement.getUpdateCount();
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return targetStatement.getMoreResults();
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        targetStatement.setFetchDirection(direction);
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return targetStatement.getFetchDirection();
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        targetStatement.setFetchSize(rows);
    }

    @Override
    public int getFetchSize() throws SQLException {
        return targetStatement.getFetchSize();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return targetStatement.getResultSetConcurrency();
    }

    @Override
    public int getResultSetType() throws SQLException {
        return targetStatement.getResultSetType();
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        targetStatement.addBatch(sql);
    }

    @Override
    public void clearBatch() throws SQLException {
        targetStatement.clearBatch();
    }

    @Override
    public int[] executeBatch() throws SQLException {
        return targetStatement.executeBatch();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return targetStatement.getConnection();
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return targetStatement.getMoreResults(current);
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return targetStatement.getGeneratedKeys();
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return targetStatement.executeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return targetStatement.executeUpdate(sql, columnIndexes);
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return targetStatement.executeUpdate(sql, columnNames);
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return targetStatement.execute(sql, autoGeneratedKeys);
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return targetStatement.execute(sql, columnIndexes);
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return targetStatement.execute(sql, columnNames);
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return targetStatement.getResultSetHoldability();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return targetStatement.isClosed();
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        targetStatement.setPoolable(poolable);
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return targetStatement.isPoolable();
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        targetStatement.closeOnCompletion();
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return targetStatement.isCloseOnCompletion();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return targetStatement.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return targetStatement.isWrapperFor(iface);
    }
}
