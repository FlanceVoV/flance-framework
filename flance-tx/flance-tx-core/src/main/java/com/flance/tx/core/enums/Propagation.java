package com.flance.tx.core.enums;

/**
 * 事务传播
 * @author jhf
 */
public enum Propagation {

    /**
     * 如果存在一个事务，则支持当前事务。如果没有事务则开启一个新的事务。
     */
    REQUIRED(0),
    /**
     * 如果存在一个事务，支持当前事务。如果没有事务，则非事务的执行。
     */
    SUPPORTS(1),
    /**
     * 如果已经存在一个事务，支持当前事务。如果没有一个活动的事务，则抛出异常。
     */
    MANDATORY(2),
    /**
     * 它会开启一个新的事务。如果一个事务已经存在，则先将这个存在的事务挂起。
     */
    REQUIRES_NEW(3),
    /**
     * 总是非事务地执行，并挂起任何存在的事务。
     */
    NOT_SUPPORTED(4),
    /**
     * 总是非事务地执行，如果存在一个活动事务，则抛出异常。
     */
    NEVER(5),
    /**
     * 如果一个活动的事务存在，则运行在一个嵌套的事务中。
     * 如果没有活动事务, 则按TransactionDefinition.PROPAGATION_REQUIRED 属性执行。
     */
    NESTED(6);

    private final int value;

    private Propagation(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
