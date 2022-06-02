package com.flance.tx.core.annotation;

import com.flance.tx.core.enums.Propagation;

import java.lang.annotation.*;

/**
 * 全局事务注解
 * @author jhf
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
@Inherited
public @interface FlanceGlobalTransactional {

    /**
     * 全局事务名称
     */
    String name() default "";

    /**
     * 传播行为
     */
    Propagation propagation() default Propagation.REQUIRED;

    /**
     * 是否加全局锁
     */
    boolean globalLock() default true;

    /**
     * 指定回滚异常
     */
    Class<? extends Throwable>[] rollbackFor() default {};

    /**
     * 指定不会滚异常
     */
    Class<? extends Throwable>[] noRollbackFor() default {};



}
