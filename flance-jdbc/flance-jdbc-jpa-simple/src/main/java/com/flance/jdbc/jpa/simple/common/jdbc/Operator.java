package com.flance.jdbc.jpa.simple.common.jdbc;

/**
 * 数据库操作符
 * @author jhf
 */
public enum Operator {

    AND,
    OR,
    EQ,
    NE,
    LIKE,
    LLIKE,
    RLIKE,
    GT,
    LT,
    GTE,
    LTE,
    IN,
    BETWEEN,
    OREQ,
    ORNE,
    ORLIKE,
    ORGT,
    ORLT,
    ORGTE,
    ORLTE,
    ORIN,
    ORBETWEEN,
    NULL,
    NOTNULL,
    ORNULL,
    ORNOTNULL,
    GROUPBY;

    private Operator() {
    }

}
