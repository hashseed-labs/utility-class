# AssertUtil

断言工具类，条件不满足抛 `IllegalArgumentException`。

**包名：** `com.utility.AssertUtil`

## 常用方法

| 方法 | 说明 |
| --- | --- |
| `notNull` | 非 null |
| `isTrue` / `isFalse` | 布尔断言 |
| `notBlank` | 非空白字符串 |
| `notEmpty` | 集合 / Map / 数组非空 |
| `equals` | 相等断言 |

```java
AssertUtil.notBlank(username, "用户名不能为空");
AssertUtil.isTrue(age >= 18, "年龄必须≥18");
```
