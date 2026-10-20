# JsonUtil

JSON 序列化 / 反序列化工具类（基于 **Jackson**）。

**包名：** `com.utility.JsonUtil`

---

## 依赖

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.datatype</groupId>
    <artifactId>jackson-datatype-jsr310</artifactId>
</dependency>
```

已支持 `Java 8` 时间类型；反序列化忽略未知字段。

---

## 常用方法

| 方法 | 说明 |
| --- | --- |
| `toJson(obj)` | 对象 → JSON |
| `toPrettyJson(obj)` | 对象 → 格式化 JSON |
| `fromJson(json, Class)` | JSON → 对象 |
| `fromJson(json, TypeReference)` | JSON → 复杂泛型 |
| `toList(json, Class)` | JSON → `List` |
| `toMap(json)` | JSON → `Map` |
| `deepCopy(source, Class)` | 深拷贝 |
| `isJson(text)` | 是否合法 JSON |
| `getMapper()` | 获取全局 `ObjectMapper` |

---

## 使用示例

```java
import com.fasterxml.jackson.core.type.TypeReference;
import com.utility.JsonUtil;
import java.util.List;
import java.util.Map;

String json = JsonUtil.toJson(user);

User user = JsonUtil.fromJson(json, User.class);
List<User> users = JsonUtil.toList(jsonArray, User.class);
Map<String, Object> map = JsonUtil.toMap(json);

List<User> list = JsonUtil.fromJson(jsonArray, new TypeReference<List<User>>() {});
User copy = JsonUtil.deepCopy(user, User.class);
```
