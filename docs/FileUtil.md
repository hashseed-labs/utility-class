# FileUtil

文件读写工具类。

**包名：** `com.utility.FileUtil`

## 常用方法

| 方法 | 说明 |
| --- | --- |
| `exists` / `isFile` / `isDirectory` | 判断 |
| `getName` / `getExtension` | 文件名 / 扩展名 |
| `readString` / `readBytes` | 读取 |
| `writeString` / `writeBytes` | 写入（自动建目录） |
| `copyFile` | 复制文件 |
| `delete` | 删除 |

```java
FileUtil.writeString("D:/tmp/a.txt", "hello");
String text = FileUtil.readString("D:/tmp/a.txt");
String ext = FileUtil.getExtension("a.txt"); // "txt"
```
