# BeanUtil

Bean 属性拷贝 / Map 互转工具类（基于 JavaBeans 内省，无 Spring 依赖）。

**包名：** `com.utility.BeanUtil`

---

## 常用方法

| 方法 | 说明 |
| --- | --- |
| `copyProperties(source, target)` | 拷贝到已有对象 |
| `copyProperties(source, target, ignoreNull)` | 可跳过 null 属性 |
| `copy(source, targetClass)` | 拷贝为新实例 |
| `copyList(list, targetClass)` | 批量拷贝 |
| `toMap(bean)` | Bean → Map |
| `toBean(map, Class)` | Map → Bean |

---

## 使用示例

```java
import com.utility.BeanUtil;

UserVO vo = BeanUtil.copy(user, UserVO.class);
BeanUtil.copyProperties(user, vo, true); // 忽略 null

List<UserVO> vos = BeanUtil.copyList(users, UserVO.class);

Map<String, Object> map = BeanUtil.toMap(user);
User user2 = BeanUtil.toBean(map, User.class);
```

## 注意事项

- 仅拷贝**同名且类型兼容**的可读可写属性
- 目标类需有无参构造
- 不支持深拷贝嵌套对象（可用 `JsonUtil.deepCopy`）
