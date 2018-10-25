package com.maozhen.sso.common.utils;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @date 2018/7/11 14:05
 */
public class StringUtil {

    public static final String EMPTY = "";
    private static Pattern linePattern = Pattern.compile("_(\\w)");

    /**
     * 判断字符串是否不为空，不为空则返回true
     *
     * @param str 源数据
     * @return Boolean
     */
    public static boolean isNotEmpty(String str) {
        if (null != str && !"".equals(str.trim())) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }


    /**
     * <p>Checks if a CharSequence is whitespace, empty ("") or null.</p>
     */
    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }


    /**
     * 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为empty
     *
     * @param obj
     * @return
     */
    public static boolean isNullOrEmpty(Object obj) {
        if (obj == null)
            return true;

        if (obj instanceof CharSequence)
            return ((CharSequence) obj).length() == 0;

        if (obj instanceof Collection)
            return ((Collection) obj).isEmpty();

        if (obj instanceof Map)
            return ((Map) obj).isEmpty();

        if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            boolean empty = true;
            for (int i = 0; i < object.length; i++) {
                if (!isNullOrEmpty(object[i])) {
                    empty = false;
                    break;
                }
            }
            return empty;
        }
        return false;
    }

    /**
     * 下划线转驼峰
     */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 给定字符串转换成大写
     *
     * @param str
     * @return
     */
    public static String toUpperCaseStr(String str) {
        if (str != null) {
            return str.toUpperCase();
        }
        return str;
    }

    /**
     * 给定字符串转换成小写
     *
     * @param str
     * @return
     */
    public static String toLowerCaseStr(String str) {
        if (str != null) {
            return str.toLowerCase();
        }
        return str;
    }

    /**
     * Object 对象转换成字符串
     *
     * @param obj
     * @return
     */
    public static String toStringByObject(Object obj) {
        return toStringByObject(obj, false, null);
    }

    /**
     * Object 对象转换成字符串，并可以根据参数去掉两端空格
     *
     * @param obj
     * @return
     */
    public static String toStringByObject(Object obj, boolean isqdkg, String datatype) {
        if (obj == null) {
            return "";
        } else {
            if (isqdkg) {
                return obj.toString().trim();
            } else {
                //如果有设置时间格式类型，这转换
                if (StringUtil.hasText(datatype)) {
                    if (obj instanceof Timestamp) {
                        return DateUtil.format((Timestamp) obj, datatype);
                    } else if (obj instanceof Date) {
                        return DateUtil.format((Timestamp) obj, datatype);
                    }
                }
                return obj.toString();
            }
        }
    }

    public static boolean hasText(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        } else {
            int strLen = str.length();
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean hasLength(CharSequence str) {
        return str != null && str.length() > 0;
    }

    public static boolean hasText(String str) {
        return hasText((CharSequence) str);
    }

    public static int parseInt(Object str) {
        return parseInt(str, 0);
    }

    public static int parseInt(Object str, int defaultValue) {
        if (str == null || str.equals("")) {
            return defaultValue;
        }
        String s = str.toString().trim();
        if (!s.matches("-?\\d+")) {
            return defaultValue;
        }
        return Integer.parseInt(s);
    }


    public static String join(Collection list, String sep) {
        return join((Collection) list, sep, (String) null);
    }

    public static String join(Collection list, String sep, String prefix) {
        Object[] array = list == null ? null : list.toArray();
        return join(array, sep, prefix);
    }

    public static String join(Object[] array, String sep, String prefix) {
        if (array == null) {
            return "";
        } else {
            int arraySize = array.length;
            if (arraySize == 0) {
                return "";
            } else {
                if (sep == null) {
                    sep = "";
                }
                if (prefix == null) {
                    prefix = "";
                }
                StringBuilder buf = new StringBuilder(prefix);
                for (int i = 0; i < arraySize; ++i) {
                    if (i > 0) {
                        buf.append(sep);
                    }
                    buf.append(array[i] == null ? "" : array[i]);
                }

                return buf.toString();
            }
        }
    }

    /**
     * 获取一个UUid
     *
     * @param
     * @return uuid 32 位
     */
    public static String createIdbyUUID() {
        return UUID.randomUUID().toString().trim().replaceAll("-", "");// 32位uuid
    }


    /**
     * 获得用户远程地址
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String remoteAddr = request.getHeader("X-Real-IP");
        if (!isBlank(remoteAddr)) {
            remoteAddr = request.getHeader("X-Forwarded-For");
        }
        if (!isBlank(remoteAddr)) {
            remoteAddr = request.getHeader("Proxy-Client-IP");
        }
        if (!isBlank(remoteAddr)) {
            remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
    }


}
