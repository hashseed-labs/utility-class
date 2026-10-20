# RandomUtil

随机数 / 随机串工具类。

**包名：** `com.utility.RandomUtil`

## 常用方法

| 方法 | 说明 |
| --- | --- |
| `nextInt` / `nextLong` / `nextBoolean` | 随机数 |
| `randomNumbers(len)` | 随机数字串 |
| `randomLetters(len)` | 随机字母串 |
| `randomString(len)` | 随机字母数字串 |
| `randomEle(list)` | 列表随机元素 |

```java
RandomUtil.nextInt(1, 100);
RandomUtil.randomNumbers(6);   // 验证码
RandomUtil.randomString(16);   // 随机口令
```
