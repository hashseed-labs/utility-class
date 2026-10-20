package com.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * 日期时间工具类（JDK 8）
 * <p>
 * 支持时间格式化，以及字符串与日期互转。
 */
public final class DateUtil {

    /** 默认日期时间格式：yyyy-MM-dd HH:mm:ss */
    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";

    /** 默认日期格式：yyyy-MM-dd */
    public static final String PATTERN_DATE = "yyyy-MM-dd";

    /** 默认时间格式：HH:mm:ss */
    public static final String PATTERN_TIME = "HH:mm:ss";

    /** 紧凑日期时间格式：yyyyMMddHHmmss */
    public static final String PATTERN_DATETIME_COMPACT = "yyyyMMddHHmmss";

    /** 紧凑日期格式：yyyyMMdd */
    public static final String PATTERN_DATE_COMPACT = "yyyyMMdd";

    private DateUtil() {
    }

    // -------------------- 格式化 --------------------

    /**
     * 将 {@link Date} 格式化为字符串（默认 yyyy-MM-dd HH:mm:ss）
     */
    public static String format(Date date) {
        return format(date, PATTERN_DATETIME);
    }

    /**
     * 将 {@link Date} 按指定格式格式化为字符串
     *
     * @param date    日期
     * @param pattern 格式，例如 yyyy-MM-dd HH:mm:ss
     * @return 格式化后的字符串；date 为 null 时返回 null
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        checkPattern(pattern);
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 将 {@link LocalDateTime} 格式化为字符串（默认 yyyy-MM-dd HH:mm:ss）
     */
    public static String format(LocalDateTime dateTime) {
        return format(dateTime, PATTERN_DATETIME);
    }

    /**
     * 将 {@link LocalDateTime} 按指定格式格式化为字符串
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        checkPattern(pattern);
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 将 {@link LocalDate} 格式化为字符串（默认 yyyy-MM-dd）
     */
    public static String format(LocalDate date) {
        return format(date, PATTERN_DATE);
    }

    /**
     * 将 {@link LocalDate} 按指定格式格式化为字符串
     */
    public static String format(LocalDate date, String pattern) {
        if (date == null) {
            return null;
        }
        checkPattern(pattern);
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    // -------------------- 字符串转日期 --------------------

    /**
     * 字符串转 {@link Date}（默认按 yyyy-MM-dd HH:mm:ss 解析）
     */
    public static Date parseToDate(String text) {
        return parseToDate(text, PATTERN_DATETIME);
    }

    /**
     * 字符串按指定格式转 {@link Date}
     *
     * @param text    时间字符串
     * @param pattern 格式，例如 yyyy-MM-dd HH:mm:ss
     * @return 解析后的 Date；text 为空时返回 null
     * @throws IllegalArgumentException 格式不匹配或解析失败
     */
    public static Date parseToDate(String text, String pattern) {
        if (isBlank(text)) {
            return null;
        }
        checkPattern(pattern);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            sdf.setLenient(false);
            return sdf.parse(text.trim());
        } catch (ParseException e) {
            throw new IllegalArgumentException(
                    "时间字符串解析失败, text=" + text + ", pattern=" + pattern, e);
        }
    }

    /**
     * 字符串转 {@link LocalDateTime}（默认按 yyyy-MM-dd HH:mm:ss 解析）
     */
    public static LocalDateTime parseToLocalDateTime(String text) {
        return parseToLocalDateTime(text, PATTERN_DATETIME);
    }

    /**
     * 字符串按指定格式转 {@link LocalDateTime}
     */
    public static LocalDateTime parseToLocalDateTime(String text, String pattern) {
        if (isBlank(text)) {
            return null;
        }
        checkPattern(pattern);
        try {
            return LocalDateTime.parse(text.trim(), DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "时间字符串解析失败, text=" + text + ", pattern=" + pattern, e);
        }
    }

    /**
     * 字符串转 {@link LocalDate}（默认按 yyyy-MM-dd 解析）
     */
    public static LocalDate parseToLocalDate(String text) {
        return parseToLocalDate(text, PATTERN_DATE);
    }

    /**
     * 字符串按指定格式转 {@link LocalDate}
     */
    public static LocalDate parseToLocalDate(String text, String pattern) {
        if (isBlank(text)) {
            return null;
        }
        checkPattern(pattern);
        try {
            return LocalDate.parse(text.trim(), DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "时间字符串解析失败, text=" + text + ", pattern=" + pattern, e);
        }
    }

    // -------------------- 比较 --------------------

    /**
     * 判断两个 {@link Date} 是否相等（精确到毫秒）
     * <p>
     * 两者都为 null 视为相等；仅一方为 null 视为不相等。
     */
    public static boolean isEqual(Date date1, Date date2) {
        if (date1 == date2) {
            return true;
        }
        if (date1 == null || date2 == null) {
            return false;
        }
        return date1.getTime() == date2.getTime();
    }

    /**
     * 判断两个 {@link LocalDateTime} 是否相等（精确到纳秒）
     */
    public static boolean isEqual(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == dateTime2) {
            return true;
        }
        if (dateTime1 == null || dateTime2 == null) {
            return false;
        }
        return dateTime1.equals(dateTime2);
    }

    /**
     * 判断两个 {@link LocalDate} 是否相等
     */
    public static boolean isEqual(LocalDate date1, LocalDate date2) {
        if (date1 == date2) {
            return true;
        }
        if (date1 == null || date2 == null) {
            return false;
        }
        return date1.equals(date2);
    }

    /**
     * 判断两个 {@link Date} 是否为同一天（忽略时分秒，按系统默认时区）
     * <p>
     * 两者都为 null 视为同一天；仅一方为 null 视为不是同一天。
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == date2) {
            return true;
        }
        if (date1 == null || date2 == null) {
            return false;
        }
        LocalDate d1 = toLocalDateTime(date1).toLocalDate();
        LocalDate d2 = toLocalDateTime(date2).toLocalDate();
        return d1.equals(d2);
    }

    /**
     * 判断两个 {@link LocalDateTime} 是否为同一天（忽略时分秒）
     */
    public static boolean isSameDay(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == dateTime2) {
            return true;
        }
        if (dateTime1 == null || dateTime2 == null) {
            return false;
        }
        return dateTime1.toLocalDate().equals(dateTime2.toLocalDate());
    }

    /**
     * 判断两个 {@link LocalDate} 是否为同一天
     */
    public static boolean isSameDay(LocalDate date1, LocalDate date2) {
        return isEqual(date1, date2);
    }

    // -------------------- Date 与 LocalDateTime 互转 --------------------

    /**
     * {@link Date} 转 {@link LocalDateTime}（系统默认时区）
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * {@link LocalDateTime} 转 {@link Date}（系统默认时区）
     */
    public static Date toDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * {@link LocalDate} 转 {@link Date}（当天 00:00:00，系统默认时区）
     */
    public static Date toDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return toDate(date.atStartOfDay());
    }

    private static void checkPattern(String pattern) {
        if (isBlank(pattern)) {
            throw new IllegalArgumentException("时间格式不能为空");
        }
    }

    private static boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }
}
