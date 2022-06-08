package com.flance.tx.datasource.proxy.datasource;

import com.flance.tx.datasource.proxy.connection.ConnectionProxy;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceProxy extends AbstractDataSourceProxy {

    private String jdbcUrl;

    private String dbType;

    public DataSourceProxy() {
    }

    public DataSourceProxy(DataSource targetDataSource) {
        this.targetDataSource = targetDataSource;
        init(targetDataSource);
    }

    @Override
    public ConnectionProxy getConnection() throws SQLException {
        Connection targetConnection = targetDataSource.getConnection();
        return  new ConnectionProxy(this, targetConnection);
    }

    @Override
    public ConnectionProxy getConnection(String username, String password) throws SQLException {
        Connection targetConnection = targetDataSource.getConnection(username, password);
        return new ConnectionProxy(this, targetConnection);
    }

    public String getDbType() {
        return dbType;
    }

    private void init(DataSource targetDataSource) {
        try (Connection connection = targetDataSource.getConnection()) {
            this.jdbcUrl = connection.getMetaData().getURL();
            this.dbType = getDbTypeByUrl();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("can not init dataSource", e);
        }
    }

    private String getDbTypeByUrl() {
        if (this.jdbcUrl.startsWith("jdbc:mysql:")) {
            return "mysql";
        }
        return null;
    }
}
