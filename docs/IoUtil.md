# IoUtil

IO 流工具类。

**包名：** `com.utility.IoUtil`

## 常用方法

| 方法 | 说明 |
| --- | --- |
| `closeQuietly` | 安静关闭 |
| `readBytes` | 读取为字节数组 |
| `readString` | 读取为字符串 |
| `copy` | 流拷贝 |
| `toReader` | InputStream → Reader |

```java
String text = IoUtil.readString(inputStream);
IoUtil.closeQuietly(inputStream);
```

> `readBytes` / `readString` 会关闭传入的输入流。
