package com.flance.tx.common.utils;

import java.math.BigDecimal;
import java.util.Date;

public class ClassTypeUtils {

    public static boolean isBaseType(Object target) {

        if (target instanceof Integer) {
            return true;
        } else if (target.equals(String.class)) {
            return true;
        } else if (target.equals(Double.class)) {
            return true;
        } else if (target.equals(Float.class)) {
            return true;
        } else if (target.equals(Long.class)) {
            return true;
        } else if (target.equals(Boolean.class)) {
            return true;
        } else if (target.equals(Date.class)) {
            return true;
        } else if (target.equals(BigDecimal.class)) {
            return true;
        } else {
            return false;
        }
    }

}
