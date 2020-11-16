package com.flance.web.auth.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static Date getDay(Integer day) {
        Calendar c = Calendar.getInstance();
        //-1.昨天时间 0.当前时间 1.明天时间 *以此类推
        c.add(Calendar.DATE, day);
        return c.getTime();
    }

}
