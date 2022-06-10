package com.flance.tx.common.utils;

import com.flance.tx.common.TxConstants;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * The type String utils.
 *
 * @author slievrly
 * @author Geng Zhang
 */
public class StringUtils {

    private StringUtils() {
    }

    public static final String EMPTY = "";

    public static boolean isNullOrEmpty(String str) {
        return (str == null) || (str.isEmpty());
    }

    public static boolean isBlank(String str) {
        int length;

        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean equals(String a, String b) {
        if (a == null) {
            return b == null;
        }
        return a.equals(b);
    }

    public static boolean equalsIgnoreCase(String a, String b) {
        if (a == null) {
            return b == null;
        }
        return a.equalsIgnoreCase(b);
    }

    public static String inputStream2String(InputStream is) {
        if (is == null) {
            return null;
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int i;
            while ((i = is.read()) != -1) {
                baos.write(i);
            }
            return baos.toString(TxConstants.DEFAULT_CHARSET_NAME);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] inputStream2Bytes(InputStream is) {
        if (is == null) {
            return null;
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int i;
            while ((i = is.read()) != -1) {
                baos.write(i);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toString(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj.getClass().isPrimitive()) {
            return String.valueOf(obj);
        }
        if (obj instanceof String) {
            return (String)obj;
        }
        if (obj instanceof Number || obj instanceof Character || obj instanceof Boolean) {
            return String.valueOf(obj);
        }
        if (obj instanceof Date) {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(obj);
        }
        if (obj instanceof Collection) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            if (!((Collection)obj).isEmpty()) {
                for (Object o : (Collection)obj) {
                    sb.append(toString(o)).append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append("]");
            return sb.toString();
        }
        if (obj instanceof Map) {
            Map<Object, Object> map = (Map)obj;
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            if (!map.isEmpty()) {
                map.forEach((key, value) -> {
                    sb.append(toString(key)).append("->")
                        .append(toString(value)).append(",");
                });
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append("}");
            return sb.toString();
        }
        StringBuilder sb = new StringBuilder();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            sb.append(field.getName());
            sb.append("=");
            try {
                Object f = field.get(obj);
                if (f.getClass() == obj.getClass()) {
                    sb.append(f.toString());
                } else {
                    sb.append(toString(f));
                }
            } catch (Exception e) {
            }
            sb.append(";");
        }
        return sb.toString();
    }

    public static String trimToNull(final String str) {
        final String ts = trim(str);
        return isEmpty(ts) ? null : ts;
    }

    public static String trim(final String str) {
        return str == null ? null : str.trim();
    }


    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }


    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
