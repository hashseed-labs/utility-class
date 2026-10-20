package com.utility;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bean 工具类（JDK 8）
 * <p>
 * 基于 JavaBeans 内省做属性拷贝 / Map 互转，不依赖 Spring。
 */
public final class BeanUtil {

    private BeanUtil() {
    }

    /**
     * 属性拷贝到已有目标对象（同名同类型属性）
     *
     * @param ignoreNull true 时源属性为 null 则跳过
     */
    public static void copyProperties(Object source, Object target, boolean ignoreNull) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("source / target 不能为空");
        }
        try {
            Map<String, PropertyDescriptor> targetPds = toPdMap(target.getClass());
            for (PropertyDescriptor sourcePd : getPropertyDescriptors(source.getClass())) {
                String name = sourcePd.getName();
                if ("class".equals(name)) {
                    continue;
                }
                PropertyDescriptor targetPd = targetPds.get(name);
                if (targetPd == null || targetPd.getWriteMethod() == null || sourcePd.getReadMethod() == null) {
                    continue;
                }
                Method read = sourcePd.getReadMethod();
                Method write = targetPd.getWriteMethod();
                if (!write.getParameterTypes()[0].isAssignableFrom(read.getReturnType())) {
                    continue;
                }
                Object value = read.invoke(source);
                if (ignoreNull && value == null) {
                    continue;
                }
                write.invoke(target, value);
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Bean 属性拷贝失败", e);
        }
    }

    /**
     * 属性拷贝到已有目标对象（不过滤 null）
     */
    public static void copyProperties(Object source, Object target) {
        copyProperties(source, target, false);
    }

    /**
     * 拷贝为新的目标类型实例
     */
    public static <T> T copy(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        if (targetClass == null) {
            throw new IllegalArgumentException("目标类型不能为空");
        }
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            copyProperties(source, target, false);
            return target;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Bean 拷贝失败: " + targetClass.getName(), e);
        }
    }

    /**
     * 批量拷贝
     */
    public static <T> List<T> copyList(List<?> sourceList, Class<T> targetClass) {
        if (sourceList == null || sourceList.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<T>(sourceList.size());
        for (Object source : sourceList) {
            result.add(copy(source, targetClass));
        }
        return result;
    }

    /**
     * Bean 转 Map（属性名 → 属性值）
     */
    public static Map<String, Object> toMap(Object bean) {
        if (bean == null) {
            return Collections.emptyMap();
        }
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            for (PropertyDescriptor pd : getPropertyDescriptors(bean.getClass())) {
                String name = pd.getName();
                if ("class".equals(name) || pd.getReadMethod() == null) {
                    continue;
                }
                result.put(name, pd.getReadMethod().invoke(bean));
            }
            return result;
        } catch (Exception e) {
            throw new IllegalArgumentException("Bean 转 Map 失败", e);
        }
    }

    /**
     * Map 转 Bean
     */
    public static <T> T toBean(Map<String, ?> map, Class<T> targetClass) {
        if (map == null) {
            return null;
        }
        if (targetClass == null) {
            throw new IllegalArgumentException("目标类型不能为空");
        }
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            Map<String, PropertyDescriptor> pdMap = toPdMap(targetClass);
            for (Map.Entry<String, ?> entry : map.entrySet()) {
                PropertyDescriptor pd = pdMap.get(entry.getKey());
                if (pd == null || pd.getWriteMethod() == null) {
                    continue;
                }
                Object value = entry.getValue();
                if (value == null) {
                    pd.getWriteMethod().invoke(target, new Object[]{null});
                    continue;
                }
                Class<?> paramType = pd.getWriteMethod().getParameterTypes()[0];
                if (paramType.isAssignableFrom(value.getClass())) {
                    pd.getWriteMethod().invoke(target, value);
                }
            }
            return target;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Map 转 Bean 失败", e);
        }
    }

    private static Map<String, PropertyDescriptor> toPdMap(Class<?> clazz) throws Exception {
        Map<String, PropertyDescriptor> map = new HashMap<String, PropertyDescriptor>();
        for (PropertyDescriptor pd : getPropertyDescriptors(clazz)) {
            map.put(pd.getName(), pd);
        }
        return map;
    }

    private static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
        return beanInfo.getPropertyDescriptors();
    }
}
