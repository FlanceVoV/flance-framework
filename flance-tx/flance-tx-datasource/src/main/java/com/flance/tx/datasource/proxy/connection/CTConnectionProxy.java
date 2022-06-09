package com.flance.tx.datasource.proxy.connection;

import com.flance.tx.datasource.proxy.datasource.DataSourceProxy;
import com.flance.tx.datasource.proxy.statement.CTPreparedStatementProxy;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * CT模式 连接代理
 * @author jhf
 */
@Slf4j
public class CTConnectionProxy extends ConnectionProxy {

    public CTConnectionProxy(DataSourceProxy dataSourceProxy, Connection targetConnection) {
        super(dataSourceProxy, targetConnection);
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        log.info("开启CT prepareStatement 代理");
        return new CTPreparedStatementProxy(this, targetConnection.prepareStatement(sql), sql);
    }
}
