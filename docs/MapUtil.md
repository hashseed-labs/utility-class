# MapUtil

Map 工具类。

**包名：** `com.utility.MapUtil`

## 常用方法

| 方法 | 说明 |
| --- | --- |
| `isEmpty` / `isNotEmpty` | 判空 |
| `get(map, key, defaultValue)` | 取值带默认 |
| `getStr` | 取字符串 |
| `of(...)` | 快速创建 Map |
| `copy` | 浅拷贝 |
| `emptyIfNull` | null 转空 Map |

```java
Map<String, Object> map = MapUtil.of("id", 1, "name", "Tom");
String name = MapUtil.getStr(map, "name", "-");
```
