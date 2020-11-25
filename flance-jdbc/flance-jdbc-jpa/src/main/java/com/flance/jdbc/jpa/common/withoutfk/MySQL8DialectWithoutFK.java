package com.flance.jdbc.jpa.common.withoutfk;

import org.hibernate.dialect.MySQL8Dialect;

/**
 * mysql方言屏蔽外键
 * @author jhf
 */
public class MySQL8DialectWithoutFK extends MySQL8Dialect {

    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }

    @Override
    public String getAddForeignKeyConstraintString(String constraintName, String[] foreignKey, String referencedTable, String[] primaryKey, boolean referencesPrimaryKey) {
        return " alter ".concat(foreignKey[0]).concat(" set default 0 ");
    }
}
