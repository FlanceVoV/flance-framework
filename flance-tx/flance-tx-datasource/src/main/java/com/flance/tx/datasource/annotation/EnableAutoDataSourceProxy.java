package com.flance.tx.datasource.annotation;

import com.flance.tx.datasource.proxy.datasource.AutoDataSourceProxyRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启数据源代理
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(AutoDataSourceProxyRegistrar.class)
@Documented
public @interface EnableAutoDataSourceProxy {

    String[] excludes() default {};

}
