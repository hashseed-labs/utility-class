# ArrayUtil

数组工具类。

**包名：** `com.utility.ArrayUtil`

## 常用方法

| 方法 | 说明 |
| --- | --- |
| `isEmpty` / `isNotEmpty` | 判空 |
| `contains` | 是否包含 |
| `get` | 安全按下标取值 |
| `toList` | 转可变 List |
| `concat` | 拼接数组 |
| `join` | 拼接为字符串 |
| `reverse` | 反转（返回新数组） |
| `distinct` | 去重 |

```java
ArrayUtil.contains(new String[]{"a", "b"}, "a"); // true
ArrayUtil.join(new Integer[]{1, 2, 3}, ",");     // "1,2,3"
```
