package com.flance.jdbc.jpa.simple.utils;

import com.google.common.collect.Maps;
import org.springframework.util.AntPathMatcher;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * stream工具类
 * @author jhf
 */
public class StreamUtils {

    /**
     * 根据属性去重
     * @param keyExtractor
     * @param <T>
     * @return
     */
    public static  <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object,Boolean> seen = Maps.newConcurrentMap();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }


}
