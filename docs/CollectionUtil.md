# CollectionUtil

集合运算与排序工具类。

**包名：** `com.utility.CollectionUtil`

---

## 功能简介

提供交集、并集、补集（相对补集 / 差集），以及按字段排序。

支持三种匹配方式：

1. 元素本身 `equals`
2. 按单个字段（key）相等
3. 自定义 `BiPredicate`（多字段、大于 / 小于 / 包含等）

所有运算**不修改入参**，返回新集合。

---

## 方法一览

### 基于 equals

| 方法 | 说明 | 返回 |
| --- | --- | --- |
| `intersection(a, b)` | 交集 | `Set` |
| `union(a, b)` | 并集 | `Set` |
| `complement(a, b)` | 补集：属于 a 不属于 b | `Set` |

### 按单个字段

| 方法 | 说明 | 返回 |
| --- | --- | --- |
| `intersection(a, b, keyExtractor)` | 按字段取交集 | `List` |
| `union(a, b, keyExtractor)` | 按字段取并集（同 key 优先保留 a） | `List` |
| `complement(a, b, keyExtractor)` | 按字段取补集 | `List` |

### 自定义匹配器

| 方法 | 说明 | 返回 |
| --- | --- | --- |
| `intersection(a, b, matcher)` | a 中至少匹配到一个 b 的元素 | `List` |
| `union(a, b, matcher)` | a 全部 + b 中与 a 都不匹配的 | `List` |
| `complement(a, b, matcher)` | a 中与 b 全都不匹配的 | `List` |

### 排序

| 方法 | 说明 |
| --- | --- |
| `sortAsc(source, keyExtractor)` | 按字段正序 |
| `sortDesc(source, keyExtractor)` | 按字段倒序 |

---

## 使用示例

### 1. 基础交并补

```java
import com.utility.CollectionUtil;
import java.util.Arrays;
import java.util.List;

List<Integer> a = Arrays.asList(1, 2, 3);
List<Integer> b = Arrays.asList(2, 3, 4);

CollectionUtil.intersection(a, b); // [2, 3]
CollectionUtil.union(a, b);        // [1, 2, 3, 4]
CollectionUtil.complement(a, b);   // [1]
```

### 2. 按对象字段

```java
List<User> both = CollectionUtil.intersection(listA, listB, User::getId);
List<User> all = CollectionUtil.union(listA, listB, User::getId);
List<User> onlyA = CollectionUtil.complement(listA, listB, User::getId);
```

### 3. 多字段 / 非等于条件

```java
List<User> hit = CollectionUtil.intersection(listA, listB,
    (x, y) -> Objects.equals(x.getDept(), y.getDept())
            && x.getAge() >= y.getAge());
```

### 4. 排序

```java
List<User> asc = CollectionUtil.sortAsc(users, User::getAge);
List<User> desc = CollectionUtil.sortDesc(users, User::getCreateTime);
```

---

## 注意事项

- 集合参数、字段提取器、匹配器不能为 `null`
- 按字段 / 自定义匹配时，优先保留第一个集合中的元素
- 排序时字段为 `null` 的元素排在最后；元素本身为 `null` 会跳过
