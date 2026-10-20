package com.utility;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 四则运算工具类（JDK 8）
 */
public final class CalculateUtil {

    private CalculateUtil() {
    }

    /**
     * 运算类型
     */
    public enum OperateType {
        /** 加 */
        ADD,
        /** 减 */
        SUBTRACT,
        /** 乘 */
        MULTIPLY,
        /** 除 */
        DIVIDE
    }

    /**
     * 对两个数进行指定四则运算
     *
     * @param num1        第一个数
     * @param num2        第二个数
     * @param operateType 操作类型：加、减、乘、除
     * @return 运算结果；除法默认保留 10 位小数，四舍五入
     * @throws IllegalArgumentException 参数为空、操作类型不支持、或除数为 0
     */
    public static BigDecimal calculate(Number num1, Number num2, OperateType operateType) {
        if (num1 == null || num2 == null) {
            throw new IllegalArgumentException("参与运算的数不能为空");
        }
        if (operateType == null) {
            throw new IllegalArgumentException("操作类型不能为空");
        }

        BigDecimal a = toBigDecimal(num1);
        BigDecimal b = toBigDecimal(num2);

        switch (operateType) {
            case ADD:
                return a.add(b);
            case SUBTRACT:
                return a.subtract(b);
            case MULTIPLY:
                return a.multiply(b);
            case DIVIDE:
                if (b.compareTo(BigDecimal.ZERO) == 0) {
                    throw new IllegalArgumentException("除数不能为 0");
                }
                return a.divide(b, 10, RoundingMode.HALF_UP);
            default:
                throw new IllegalArgumentException("不支持的操作类型: " + operateType);
        }
    }

    /**
     * 对两个数进行指定四则运算（除法可指定精度与舍入方式）
     *
     * @param num1         第一个数
     * @param num2         第二个数
     * @param operateType  操作类型
     * @param scale        除法保留小数位数（仅除法生效）
     * @param roundingMode 除法舍入方式（仅除法生效）
     * @return 运算结果
     */
    public static BigDecimal calculate(Number num1, Number num2, OperateType operateType,
                                       int scale, RoundingMode roundingMode) {
        if (operateType != OperateType.DIVIDE) {
            return calculate(num1, num2, operateType);
        }
        if (num1 == null || num2 == null) {
            throw new IllegalArgumentException("参与运算的数不能为空");
        }
        if (roundingMode == null) {
            throw new IllegalArgumentException("舍入方式不能为空");
        }
        if (scale < 0) {
            throw new IllegalArgumentException("小数位数不能为负数");
        }

        BigDecimal a = toBigDecimal(num1);
        BigDecimal b = toBigDecimal(num2);
        if (b.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("除数不能为 0");
        }
        return a.divide(b, scale, roundingMode);
    }

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
}
