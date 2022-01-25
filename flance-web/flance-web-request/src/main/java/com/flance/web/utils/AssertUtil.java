package com.flance.web.utils;

import java.util.Collection;

public class AssertUtil {

    public static void notNull(Object object, AssertException exception) {
        if (null == object) {
            throw exception;
        }
    }

    public static void notEmpty(String object, AssertException exception) {
        if (null == object || object.equals("")) {
            throw exception;
        }
    }

    public static void notEmpty(Collection<?> collection, AssertException exception) {
        if (null == collection || collection.size() == 0) {
            throw exception;
        }
    }

    public static void mastEquals(Object origin, Object target, AssertException exception) {
        if (!origin.equals(target)) {
            throw exception;
        }
    }

    public static void throwError(AssertException exception) {
        throw exception;
    }

}
