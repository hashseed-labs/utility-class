# PageUtil

分页计算工具类。

**包名：** `com.utility.PageUtil`

---

## 常用方法

| 方法 | 说明 |
| --- | --- |
| `getOffset(pageNum, pageSize)` | 计算 SQL offset（页码从 1 起） |
| `getTotalPages(total, pageSize)` | 总页数 |
| `correctPageNum(...)` | 校正页码到合法范围 |
| `hasPrevious` / `hasNext` | 是否有上 / 下一页 |
| `pageList(list, pageNum, pageSize)` | 内存列表切片 |
| `of(records, total, pageNum, pageSize)` | 构建 `PageResult` |
| `page(all, pageNum, pageSize)` | 内存分页并返回结果 |

---

## 使用示例

```java
import com.utility.PageUtil;

// MyBatis / JDBC
int offset = PageUtil.getOffset(pageNum, pageSize);
// SELECT ... LIMIT #{pageSize} OFFSET #{offset}

PageUtil.PageResult<User> result = PageUtil.of(records, total, pageNum, pageSize);
result.getRecords();
result.getTotal();
result.getTotalPages();
result.isHasNext();

// 纯内存分页
PageUtil.PageResult<User> page = PageUtil.page(allUsers, 2, 10);
```

## 约定

- 页码从 **1** 开始
- `pageNum` / `pageSize` 非法时自动校正为最小值 1
- `PageResult` 字段：`records` / `total` / `pageNum` / `pageSize` / `totalPages`
