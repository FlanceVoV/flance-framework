package com.flance.tx.datasource.proxy.datasource;

import com.flance.tx.datasource.proxy.connection.CTConnectionProxy;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * CT 模式数据源代理
 * @author jhf
 */
@Slf4j
public class CTDataSourceProxy extends DataSourceProxy {

    public CTDataSourceProxy(DataSource targetDataSource) {
        super(targetDataSource);
    }

    @Override
    public Connection getConnection() throws SQLException {
        log.info("开启CT模式连接代理");
        return new CTConnectionProxy(this, targetDataSource.getConnection());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        log.info("开启CT模式连接代理");
        return new CTConnectionProxy(this, targetDataSource.getConnection());
    }
}
