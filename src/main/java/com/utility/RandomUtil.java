package com.utility;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机数 / 随机串工具类（JDK 8）
 */
public final class RandomUtil {

    private static final String DIGITS = "0123456789";
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LETTERS_DIGITS = LETTERS + DIGITS;

    private RandomUtil() {
    }

    /**
     * 随机 int：[0, bound)
     */
    public static int nextInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }

    /**
     * 随机 int：[origin, bound)
     */
    public static int nextInt(int origin, int bound) {
        return ThreadLocalRandom.current().nextInt(origin, bound);
    }

    /**
     * 随机 long：[origin, bound)
     */
    public static long nextLong(long origin, long bound) {
        return ThreadLocalRandom.current().nextLong(origin, bound);
    }

    /**
     * 随机 boolean
     */
    public static boolean nextBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    /**
     * 随机数字串
     */
    public static String randomNumbers(int length) {
        return randomString(DIGITS, length);
    }

    /**
     * 随机字母串
     */
    public static String randomLetters(int length) {
        return randomString(LETTERS, length);
    }

    /**
     * 随机字母数字串
     */
    public static String randomString(int length) {
        return randomString(LETTERS_DIGITS, length);
    }

    /**
     * 从给定字符集生成随机串
     */
    public static String randomString(String chars, int length) {
        if (StringUtil.isEmpty(chars)) {
            throw new IllegalArgumentException("字符集不能为空");
        }
        if (length <= 0) {
            return StringUtil.EMPTY;
        }
        Random random = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * 从列表中随机取一个元素；空列表返回 null
     */
    public static <T> T randomEle(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(nextInt(list.size()));
    }
}
