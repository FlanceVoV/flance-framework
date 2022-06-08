package com.flance.tx.datasource.proxy.statement;

import com.flance.tx.datasource.proxy.connection.AbstractConnectionProxy;
import com.flance.tx.datasource.proxy.connection.ConnectionProxy;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PreparedStatementProxy extends AbstractPreparedStatementProxy implements PreparedStatement{

    public PreparedStatementProxy(AbstractConnectionProxy connectionProxy, PreparedStatement targetStatement, String execSql) throws SQLException {
        super(connectionProxy, targetStatement, execSql);
    }

    public PreparedStatementProxy(ConnectionProxy connectionProxy, PreparedStatement targetStatement) throws SQLException {
        super(connectionProxy, targetStatement);
    }
}
