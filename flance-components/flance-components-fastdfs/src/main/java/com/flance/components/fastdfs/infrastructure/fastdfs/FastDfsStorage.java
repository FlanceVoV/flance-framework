package com.flance.components.fastdfs.infrastructure.fastdfs;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 动态数据源注解
 * @author jhf
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FastDfsStorage {

    SingleTracker single() default SingleTracker.SINGLE;

    int clientArgIndex();

}
