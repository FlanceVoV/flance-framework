package com.flance.tx.core.annotation;

import java.lang.annotation.*;

/**
 * 全局锁注解
 * @author jhf
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
@Inherited
public @interface FlanceGlobalLock {
}
