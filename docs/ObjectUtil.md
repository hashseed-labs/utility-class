# ObjectUtil

对象空安全处理工具类。

**包名：** `com.utility.ObjectUtil`

## 常用方法

| 方法 | 说明 |
| --- | --- |
| `isNull` / `isNotNull` | 判空 |
| `hasNull` / `isAllNull` | 多对象判空 |
| `equals` | 空安全 equals |
| `firstNonNull` | 取第一个非 null |
| `defaultIfNull` | null 时返回默认值 |
| `toString` | 空安全转字符串 |

```java
ObjectUtil.defaultIfNull(name, "未知");
ObjectUtil.firstNonNull(null, null, "A"); // "A"
```
