# TreeUtil

树形结构组装工具类：扁平列表 → 树，树 → 扁平列表。

**包名：** `com.utility.TreeUtil`

---

## 常用方法

| 方法 | 说明 |
| --- | --- |
| `buildTree(...)` | 按 id / parentId 组装树 |
| `buildTree(..., comparator)` | 组装并对每层排序 |
| `sortTree` | 递归排序已有树 |
| `flatten` | 树转扁平列表（前序） |

---

## 使用示例

假设节点：

```java
public class Menu {
    private Long id;
    private Long parentId;
    private String name;
    private Integer sort;
    private List<Menu> children;
    // getter / setter
}
```

```java
import com.utility.TreeUtil;
import java.util.Comparator;
import java.util.List;

List<Menu> roots = TreeUtil.buildTree(
        menus,
        Menu::getId,
        Menu::getParentId,
        Menu::setChildren,
        0L  // 根节点 parentId
);

// 带排序
List<Menu> sortedRoots = TreeUtil.buildTree(
        menus,
        Menu::getId,
        Menu::getParentId,
        Menu::setChildren,
        Menu::getChildren,
        0L,
        Comparator.comparing(Menu::getSort, Comparator.nullsLast(Integer::compareTo))
);

List<Menu> flat = TreeUtil.flatten(roots, Menu::getChildren);
```

## 说明

- `rootParentId` 常见为 `null` 或 `0`
- 找不到父节点的数据会作为根节点返回
- 空列表返回空 List，不抛异常
