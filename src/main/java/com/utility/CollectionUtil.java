package com.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * 集合运算工具类（JDK 8）
 * <p>
 * 三种比较方式：
 * <ul>
 *   <li>元素本身 equals</li>
 *   <li>按单个字段（key）相等</li>
 *   <li>自定义匹配器 {@link BiPredicate}：支持多字段、大于/小于/包含等任意条件</li>
 * </ul>
 * 结果均为新集合，不修改入参。补集为相对补集（差集）。
 * <p>
 * 按字段 / 自定义匹配时：优先保留第一个集合中的元素。
 */
public final class CollectionUtil {

    private CollectionUtil() {
    }

    // -------------------- 基于 equals --------------------

    /**
     * 交集：同时属于 a 和 b 的元素（基于 equals）
     */
    public static <T> Set<T> intersection(Collection<T> a, Collection<T> b) {
        checkNotNull(a, b);
        Set<T> result = new HashSet<T>(a);
        result.retainAll(b);
        return result;
    }

    /**
     * 并集：属于 a 或属于 b 的元素（基于 equals）
     */
    public static <T> Set<T> union(Collection<T> a, Collection<T> b) {
        checkNotNull(a, b);
        Set<T> result = new HashSet<T>(a);
        result.addAll(b);
        return result;
    }

    /**
     * 补集（相对补集 / 差集）：属于 a、不属于 b 的元素（基于 equals）
     */
    public static <T> Set<T> complement(Collection<T> a, Collection<T> b) {
        checkNotNull(a, b);
        Set<T> result = new HashSet<T>(a);
        result.removeAll(b);
        return result;
    }

    // -------------------- 按单个字段相等 --------------------

    /**
     * 按指定字段取交集：a 中字段值出现在 b 中的元素
     */
    public static <T, K> List<T> intersection(Collection<T> a, Collection<T> b,
                                              Function<? super T, ? extends K> keyExtractor) {
        checkArgs(a, b, keyExtractor);
        Set<K> keysInB = collectKeys(b, keyExtractor);
        return filterByKeyPresence(a, keyExtractor, keysInB, true);
    }

    /**
     * 按指定字段取并集：同 key 优先保留 a 中的元素
     */
    public static <T, K> List<T> union(Collection<T> a, Collection<T> b,
                                       Function<? super T, ? extends K> keyExtractor) {
        checkArgs(a, b, keyExtractor);
        Map<K, T> result = new LinkedHashMap<K, T>();
        putAllByKey(result, a, keyExtractor);
        putAllByKey(result, b, keyExtractor);
        return new ArrayList<T>(result.values());
    }

    /**
     * 按指定字段取补集：a 中字段值未出现在 b 中的元素
     */
    public static <T, K> List<T> complement(Collection<T> a, Collection<T> b,
                                            Function<? super T, ? extends K> keyExtractor) {
        checkArgs(a, b, keyExtractor);
        Set<K> keysInB = collectKeys(b, keyExtractor);
        return filterByKeyPresence(a, keyExtractor, keysInB, false);
    }

    // -------------------- 自定义匹配器（多字段 / 非等于） --------------------

    /**
     * 按自定义条件取交集：a 中至少与 b 的某一元素匹配成功的元素。
     * <p>
     * 匹配条件由 {@code matcher} 决定，支持多字段组合，以及等于、大于、小于、包含等任意逻辑。
     * a、b 可以是不同类型。
     *
     * @param a       第一个集合
     * @param b       第二个集合
     * @param matcher 匹配条件，{@code (leftFromA, rightFromB) -> boolean}
     * @return 来自 a 的元素列表（保留 a 中顺序）
     */
    public static <T, U> List<T> intersection(Collection<T> a, Collection<U> b,
                                              BiPredicate<? super T, ? super U> matcher) {
        checkArgs(a, b, matcher);
        List<T> result = new ArrayList<T>();
        for (T left : a) {
            if (left == null) {
                continue;
            }
            if (anyMatch(left, b, matcher)) {
                result.add(left);
            }
        }
        return result;
    }

    /**
     * 按自定义条件取并集：先取 a 的全部元素，再补上 b 中与 a 任一元素都不匹配的元素。
     * <p>
     * 要求 a、b 为同一类型（或 b 可赋给 T），以便合并到同一结果列表。
     *
     * @param a       第一个集合
     * @param b       第二个集合
     * @param matcher 匹配条件，{@code (leftFromA, rightFromB) -> boolean}
     * @return 合并后的元素列表（匹配到的优先保留 a）
     */
    public static <T> List<T> union(Collection<T> a, Collection<? extends T> b,
                                    BiPredicate<? super T, ? super T> matcher) {
        checkArgs(a, b, matcher);
        List<T> result = new ArrayList<T>();
        for (T left : a) {
            if (left != null) {
                result.add(left);
            }
        }
        for (T right : b) {
            if (right == null) {
                continue;
            }
            if (!anyMatch(right, a, reverse(matcher))) {
                result.add(right);
            }
        }
        return result;
    }

    /**
     * 按自定义条件取补集：a 中与 b 的所有元素都不匹配的元素。
     * <p>
     * a、b 可以是不同类型。
     *
     * @param a       被减集合
     * @param b       减去的集合
     * @param matcher 匹配条件，{@code (leftFromA, rightFromB) -> boolean}
     * @return 来自 a 的元素列表（保留 a 中顺序）
     */
    public static <T, U> List<T> complement(Collection<T> a, Collection<U> b,
                                            BiPredicate<? super T, ? super U> matcher) {
        checkArgs(a, b, matcher);
        List<T> result = new ArrayList<T>();
        for (T left : a) {
            if (left == null) {
                continue;
            }
            if (!anyMatch(left, b, matcher)) {
                result.add(left);
            }
        }
        return result;
    }

    
    // -------------------- 按指定字段排序 --------------------

    /**
     * 按指定字段正序排序（升序）
     * <p>
     * 不修改原集合；字段值为 null 的元素排在最后；元素本身为 null 的会被跳过。
     *
     * @param source       原集合
     * @param keyExtractor 字段提取器，例如 {@code User::getAge}
     * @return 排序后的新列表
     */
    public static <T, U extends Comparable<? super U>> List<T> sortAsc(
            Collection<T> source, Function<? super T, ? extends U> keyExtractor) {
        return sortBy(source, keyExtractor, true);
    }

    /**
     * 按指定字段倒序排序（降序）
     * <p>
     * 不修改原集合；字段值为 null 的元素排在最后；元素本身为 null 的会被跳过。
     *
     * @param source       原集合
     * @param keyExtractor 字段提取器，例如 {@code User::getAge}
     * @return 排序后的新列表
     */
    public static <T, U extends Comparable<? super U>> List<T> sortDesc(
            Collection<T> source, Function<? super T, ? extends U> keyExtractor) {
        return sortBy(source, keyExtractor, false);
    }

    private static <T, U extends Comparable<? super U>> List<T> sortBy(
            Collection<T> source, Function<? super T, ? extends U> keyExtractor, boolean ascending) {
        if (source == null) {
            throw new IllegalArgumentException("参与排序的集合不能为空");
        }
        if (keyExtractor == null) {
            throw new IllegalArgumentException("字段提取器不能为空");
        }

        List<T> result = new ArrayList<T>();
        for (T item : source) {
            if (item != null) {
                result.add(item);
            }
        }

        Comparator<U> keyComparator = ascending
                ? Comparator.nullsLast(Comparator.<U>naturalOrder())
                : Comparator.nullsLast(Comparator.<U>reverseOrder());
        Comparator<T> comparator = Comparator.comparing(keyExtractor, keyComparator);
        Collections.sort(result, comparator);
        return result;
    }

    // -------------------- 内部方法 --------------------

    private static <T, U> boolean anyMatch(T left, Collection<U> rights,
                                           BiPredicate<? super T, ? super U> matcher) {
        for (U right : rights) {
            if (right == null) {
                continue;
            }
            if (matcher.test(left, right)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将 (a, b) 匹配器反转为 (b, a)，用于并集中判断 b 的元素是否已在 a 中匹配过。
     */
    private static <T> BiPredicate<T, T> reverse(BiPredicate<? super T, ? super T> matcher) {
        return (left, right) -> matcher.test(right, left);
    }

    private static <T, K> void putAllByKey(Map<K, T> target, Collection<T> source,
                                           Function<? super T, ? extends K> keyExtractor) {
        for (T item : source) {
            if (item == null) {
                continue;
            }
            K key = keyExtractor.apply(item);
            if (key == null || target.containsKey(key)) {
                continue;
            }
            target.put(key, item);
        }
    }

    private static <T, K> Set<K> collectKeys(Collection<T> source,
                                             Function<? super T, ? extends K> keyExtractor) {
        Set<K> keys = new HashSet<K>();
        for (T item : source) {
            if (item == null) {
                continue;
            }
            K key = keyExtractor.apply(item);
            if (key != null) {
                keys.add(key);
            }
        }
        return keys;
    }

    private static <T, K> List<T> filterByKeyPresence(Collection<T> source,
                                                      Function<? super T, ? extends K> keyExtractor,
                                                      Set<K> keys,
                                                      boolean shouldContain) {
        List<T> result = new ArrayList<T>();
        Set<K> seen = new HashSet<K>();
        for (T item : source) {
            if (item == null) {
                continue;
            }
            K key = keyExtractor.apply(item);
            if (key == null || !seen.add(key)) {
                continue;
            }
            if (keys.contains(key) == shouldContain) {
                result.add(item);
            }
        }
        return result;
    }

    private static void checkNotNull(Collection<?> a, Collection<?> b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("参与运算的集合不能为空");
        }
    }

    private static void checkArgs(Collection<?> a, Collection<?> b, Function<?, ?> keyExtractor) {
        checkNotNull(a, b);
        if (keyExtractor == null) {
            throw new IllegalArgumentException("字段提取器不能为空");
        }
    }

    private static void checkArgs(Collection<?> a, Collection<?> b, BiPredicate<?, ?> matcher) {
        checkNotNull(a, b);
        if (matcher == null) {
            throw new IllegalArgumentException("匹配条件不能为空");
        }
    }
}
