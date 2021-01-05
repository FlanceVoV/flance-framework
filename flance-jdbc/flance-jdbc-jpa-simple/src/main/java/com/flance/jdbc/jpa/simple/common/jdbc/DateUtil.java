package com.flance.jdbc.jpa.simple.common.jdbc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static Date parse(String dateStr, String formater) {
        if (dateStr != null && !"".equals(dateStr.trim())) {
            SimpleDateFormat format = new SimpleDateFormat(formater, Locale.ENGLISH);
            try {
                return format.parse(dateStr);
            } catch (ParseException var4) {
                var4.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }
}
