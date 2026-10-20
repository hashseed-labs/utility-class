# IdUtil

ID 生成工具类。

**包名：** `com.utility.IdUtil`

## 常用方法

| 方法 | 说明 |
| --- | --- |
| `randomUuid()` | 标准 UUID（含横线） |
| `simpleUuid()` | 无横线 UUID |
| `snowflakeId()` | 雪花 long ID |
| `snowflakeIdStr()` | 雪花字符串 ID |
| `createSnowflake(workerId, datacenterId)` | 自定义机器位 |

```java
String uuid = IdUtil.simpleUuid();
long id = IdUtil.snowflakeId();

IdUtil.Snowflake snowflake = IdUtil.createSnowflake(2, 1);
long customId = snowflake.nextId();
```

> 默认雪花使用 `workerId=1, datacenterId=1`。多实例部署请为每台机器分配不同 workerId。
