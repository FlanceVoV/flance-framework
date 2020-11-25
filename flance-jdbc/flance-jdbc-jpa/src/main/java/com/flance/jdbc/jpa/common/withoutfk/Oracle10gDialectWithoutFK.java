package com.flance.jdbc.jpa.common.withoutfk;

import org.hibernate.dialect.Oracle10gDialect;

/**
 * oracle方言屏蔽外键
 * @author jhf
 */
public class Oracle10gDialectWithoutFK extends Oracle10gDialect {

    @Override
    public String getAddForeignKeyConstraintString(String constraintName, String[] foreignKey, String referencedTable, String[] primaryKey, boolean referencesPrimaryKey) {
        // 通过修改外键列的默认值，而不是添加外键，避免生成外键
        return " modify "+ foreignKey[0] +" default 0 " ;
    }

}
