package com.flance.tx.config.tx;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启数据源代理
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EnableFlanceTxRegistrar.class)
@Documented
public @interface EnableFlanceTx {

    String[] excludes() default {};

    boolean enableDataSourceProxy() default true;

}
