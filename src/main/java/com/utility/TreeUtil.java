package com.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 树形结构组装工具类（JDK 8）
 * <p>
 * 将扁平列表按 id / parentId 组装为树。
 */
public final class TreeUtil {

    private TreeUtil() {
    }

    /**
     * 组装树
     *
     * @param list             扁平数据
     * @param idGetter         获取节点 id
     * @param parentIdGetter   获取父节点 id
     * @param childrenSetter   设置子节点列表
     * @param rootParentId     根节点的 parentId（常见为 null 或 0）
     * @param <T>              节点类型
     * @param <K>              id 类型
     * @return 根节点列表
     */
    public static <T, K> List<T> buildTree(List<T> list,
                                           Function<? super T, ? extends K> idGetter,
                                           Function<? super T, ? extends K> parentIdGetter,
                                           BiConsumer<T, List<T>> childrenSetter,
                                           K rootParentId) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        if (idGetter == null || parentIdGetter == null || childrenSetter == null) {
            throw new IllegalArgumentException("id/parentId/children 访问器不能为空");
        }

        Map<K, T> nodeMap = new LinkedHashMap<K, T>();
        Map<K, List<T>> childrenMap = new LinkedHashMap<K, List<T>>();

        for (T node : list) {
            if (node == null) {
                continue;
            }
            K id = idGetter.apply(node);
            if (id == null) {
                continue;
            }
            nodeMap.put(id, node);
            childrenMap.put(id, new ArrayList<T>());
        }

        List<T> roots = new ArrayList<T>();
        for (T node : list) {
            if (node == null) {
                continue;
            }
            K id = idGetter.apply(node);
            if (id == null) {
                continue;
            }
            K parentId = parentIdGetter.apply(node);
            if (isRoot(parentId, rootParentId) || !nodeMap.containsKey(parentId)) {
                roots.add(node);
            } else {
                childrenMap.get(parentId).add(node);
            }
        }

        for (Map.Entry<K, T> entry : nodeMap.entrySet()) {
            childrenSetter.accept(entry.getValue(), childrenMap.get(entry.getKey()));
        }
        return roots;
    }

    /**
     * 组装树（根 parentId 默认为 null）
     */
    public static <T, K> List<T> buildTree(List<T> list,
                                           Function<? super T, ? extends K> idGetter,
                                           Function<? super T, ? extends K> parentIdGetter,
                                           BiConsumer<T, List<T>> childrenSetter) {
        return buildTree(list, idGetter, parentIdGetter, childrenSetter, null);
    }

    /**
     * 组装树并对每一层子节点排序
     */
    public static <T, K> List<T> buildTree(List<T> list,
                                           Function<? super T, ? extends K> idGetter,
                                           Function<? super T, ? extends K> parentIdGetter,
                                           BiConsumer<T, List<T>> childrenSetter,
                                           Function<? super T, ? extends List<T>> childrenGetter,
                                           K rootParentId,
                                           Comparator<? super T> comparator) {
        List<T> roots = buildTree(list, idGetter, parentIdGetter, childrenSetter, rootParentId);
        if (comparator != null && childrenGetter != null) {
            sortTree(roots, childrenGetter, comparator);
        }
        return roots;
    }

    /**
     * 递归排序树节点
     */
    public static <T> void sortTree(List<T> nodes,
                                    Function<? super T, ? extends List<T>> childrenGetter,
                                    Comparator<? super T> comparator) {
        if (nodes == null || nodes.isEmpty() || childrenGetter == null || comparator == null) {
            return;
        }
        Collections.sort(nodes, comparator);
        for (T node : nodes) {
            List<T> children = childrenGetter.apply(node);
            if (children != null && !children.isEmpty()) {
                sortTree(children, childrenGetter, comparator);
            }
        }
    }

    /**
     * 树转扁平列表（前序遍历）
     */
    public static <T> List<T> flatten(List<T> roots, Function<? super T, ? extends List<T>> childrenGetter) {
        List<T> result = new ArrayList<T>();
        if (roots == null || roots.isEmpty()) {
            return result;
        }
        if (childrenGetter == null) {
            throw new IllegalArgumentException("childrenGetter 不能为空");
        }
        for (T root : roots) {
            flattenNode(root, childrenGetter, result);
        }
        return result;
    }

    private static <T> void flattenNode(T node,
                                        Function<? super T, ? extends List<T>> childrenGetter,
                                        List<T> result) {
        if (node == null) {
            return;
        }
        result.add(node);
        List<T> children = childrenGetter.apply(node);
        if (children == null || children.isEmpty()) {
            return;
        }
        for (T child : children) {
            flattenNode(child, childrenGetter, result);
        }
    }

    private static <K> boolean isRoot(K parentId, K rootParentId) {
        if (parentId == null) {
            return rootParentId == null;
        }
        return parentId.equals(rootParentId);
    }
}
