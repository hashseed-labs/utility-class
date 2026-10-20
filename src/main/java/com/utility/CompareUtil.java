package com.utility;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 比较工具类（JDK 8）
 * <p>
 * 支持数值比较，以及日期类型（{@link Date} / {@link LocalDateTime} / {@link LocalDate}）大小比较。
 */
public final class CompareUtil {

    private CompareUtil() {
    }

    // -------------------- 数值比较 --------------------

    /**
     * 比较两个数的大小
     *
     * @param num1 第一个数
     * @param num2 第二个数
     * @return num1 小于 num2 返回负数，相等返回 0，num1 大于 num2 返回正数
     * @throws IllegalArgumentException 任一参数为空
     */
    public static int compare(Number num1, Number num2) {
        if (num1 == null || num2 == null) {
            throw new IllegalArgumentException("参与比较的数不能为空");
        }
        return toBigDecimal(num1).compareTo(toBigDecimal(num2));
    }

    /**
     * 判断 num1 是否大于 num2
     */
    public static boolean gt(Number num1, Number num2) {
        return compare(num1, num2) > 0;
    }

    /**
     * 判断 num1 是否大于等于 num2
     */
    public static boolean ge(Number num1, Number num2) {
        return compare(num1, num2) >= 0;
    }

    /**
     * 判断 num1 是否小于 num2
     */
    public static boolean lt(Number num1, Number num2) {
        return compare(num1, num2) < 0;
    }

    /**
     * 判断 num1 是否小于等于 num2
     */
    public static boolean le(Number num1, Number num2) {
        return compare(num1, num2) <= 0;
    }

    /**
     * 判断两个数是否相等（按数值比较，忽略小数末尾的 0）
     */
    public static boolean eq(Number num1, Number num2) {
        return compare(num1, num2) == 0;
    }

    /**
     * 返回两个数中较大的一个
     */
    public static BigDecimal max(Number num1, Number num2) {
        return compare(num1, num2) >= 0 ? toBigDecimal(num1) : toBigDecimal(num2);
    }

    /**
     * 返回两个数中较小的一个
     */
    public static BigDecimal min(Number num1, Number num2) {
        return compare(num1, num2) <= 0 ? toBigDecimal(num1) : toBigDecimal(num2);
    }

    // -------------------- 日期比较：Date --------------------

    /**
     * 比较两个 {@link Date} 的大小（精确到毫秒）
     *
     * @return date1 早于 date2 返回负数，相等返回 0，date1 晚于 date2 返回正数
     * @throws IllegalArgumentException 任一参数为空
     */
    public static int compare(Date date1, Date date2) {
        checkDateNotNull(date1, date2);
        return Long.compare(date1.getTime(), date2.getTime());
    }

    public static boolean gt(Date date1, Date date2) {
        return compare(date1, date2) > 0;
    }

    public static boolean ge(Date date1, Date date2) {
        return compare(date1, date2) >= 0;
    }

    public static boolean lt(Date date1, Date date2) {
        return compare(date1, date2) < 0;
    }

    public static boolean le(Date date1, Date date2) {
        return compare(date1, date2) <= 0;
    }

    public static boolean eq(Date date1, Date date2) {
        return compare(date1, date2) == 0;
    }

    public static Date max(Date date1, Date date2) {
        return compare(date1, date2) >= 0 ? date1 : date2;
    }

    public static Date min(Date date1, Date date2) {
        return compare(date1, date2) <= 0 ? date1 : date2;
    }

    // -------------------- 日期比较：LocalDateTime --------------------

    /**
     * 比较两个 {@link LocalDateTime} 的大小
     *
     * @return dateTime1 早于 dateTime2 返回负数，相等返回 0，dateTime1 晚于 dateTime2 返回正数
     * @throws IllegalArgumentException 任一参数为空
     */
    public static int compare(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        checkDateNotNull(dateTime1, dateTime2);
        return dateTime1.compareTo(dateTime2);
    }

    public static boolean gt(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return compare(dateTime1, dateTime2) > 0;
    }

    public static boolean ge(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return compare(dateTime1, dateTime2) >= 0;
    }

    public static boolean lt(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return compare(dateTime1, dateTime2) < 0;
    }

    public static boolean le(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return compare(dateTime1, dateTime2) <= 0;
    }

    public static boolean eq(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return compare(dateTime1, dateTime2) == 0;
    }

    public static LocalDateTime max(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return compare(dateTime1, dateTime2) >= 0 ? dateTime1 : dateTime2;
    }

    public static LocalDateTime min(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return compare(dateTime1, dateTime2) <= 0 ? dateTime1 : dateTime2;
    }

    // -------------------- 日期比较：LocalDate --------------------

    /**
     * 比较两个 {@link LocalDate} 的大小
     *
     * @return date1 早于 date2 返回负数，相等返回 0，date1 晚于 date2 返回正数
     * @throws IllegalArgumentException 任一参数为空
     */
    public static int compare(LocalDate date1, LocalDate date2) {
        checkDateNotNull(date1, date2);
        return date1.compareTo(date2);
    }

    public static boolean gt(LocalDate date1, LocalDate date2) {
        return compare(date1, date2) > 0;
    }

    public static boolean ge(LocalDate date1, LocalDate date2) {
        return compare(date1, date2) >= 0;
    }

    public static boolean lt(LocalDate date1, LocalDate date2) {
        return compare(date1, date2) < 0;
    }

    public static boolean le(LocalDate date1, LocalDate date2) {
        return compare(date1, date2) <= 0;
    }

    public static boolean eq(LocalDate date1, LocalDate date2) {
        return compare(date1, date2) == 0;
    }

    public static LocalDate max(LocalDate date1, LocalDate date2) {
        return compare(date1, date2) >= 0 ? date1 : date2;
    }

    public static LocalDate min(LocalDate date1, LocalDate date2) {
        return compare(date1, date2) <= 0 ? date1 : date2;
    }

    // -------------------- 内部方法 --------------------

    private static BigDecimal toBigDecimal(Number number) {
        if (number instanceof BigDecimal) {
            return (BigDecimal) number;
        }
        if (number instanceof Integer || number instanceof Long
                || number instanceof Short || number instanceof Byte) {
            return BigDecimal.valueOf(number.longValue());
        }
        // Double / Float 等：用字符串避免二进制浮点误差
        return new BigDecimal(number.toString());
    }

    private static void checkDateNotNull(Object date1, Object date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("参与比较的日期不能为空");
        }
    }
}
