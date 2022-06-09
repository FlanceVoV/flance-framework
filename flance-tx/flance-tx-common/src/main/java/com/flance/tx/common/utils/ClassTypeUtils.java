package com.flance.tx.common.utils;

import java.util.Date;

public class ClassTypeUtils {

    public static boolean isBaseType(Object target) {

        if (target instanceof Integer) {
            return true;
        } else if (target instanceof String) {
            return true;
        } else if (target instanceof Double) {
            return true;
        } else if (target instanceof Float) {
            return true;
        } else if (target instanceof Long) {
            return true;
        } else if (target instanceof Boolean) {
            return true;
        } else if (target instanceof Date) {
            return true;
        } else {
            return false;
        }
    }

}
