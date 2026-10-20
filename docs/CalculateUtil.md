# CalculateUtil

四则运算工具类。

**包名：** `com.utility.CalculateUtil`

---

## 功能简介

对两个数执行加、减、乘、除，结果类型为 `BigDecimal`，避免 `double` 精度问题。

---

## 运算类型

| 枚举 | 含义 |
| --- | --- |
| `OperateType.ADD` | 加 |
| `OperateType.SUBTRACT` | 减 |
| `OperateType.MULTIPLY` | 乘 |
| `OperateType.DIVIDE` | 除 |

---

## 方法一览

| 方法 | 说明 |
| --- | --- |
| `calculate(num1, num2, operateType)` | 执行四则运算；除法默认保留 10 位小数，四舍五入 |
| `calculate(num1, num2, operateType, scale, roundingMode)` | 除法时可指定精度与舍入方式；非除法时忽略后两参 |

---

## 使用示例

```java
import com.utility.CalculateUtil;
import com.utility.CalculateUtil.OperateType;
import java.math.BigDecimal;
import java.math.RoundingMode;

BigDecimal sum = CalculateUtil.calculate(10, 3, OperateType.ADD);
// 13

BigDecimal diff = CalculateUtil.calculate(10, 3, OperateType.SUBTRACT);
// 7

BigDecimal product = CalculateUtil.calculate(10, 3, OperateType.MULTIPLY);
// 30

BigDecimal quotient = CalculateUtil.calculate(10, 3, OperateType.DIVIDE);
// 3.3333333333

BigDecimal q2 = CalculateUtil.calculate(10, 3, OperateType.DIVIDE, 2, RoundingMode.HALF_UP);
// 3.33
```

---

## 注意事项

- `num1`、`num2`、`operateType` 不能为 `null`
- 除数为 `0` 时抛出异常
- 支持 `Integer` / `Long` / `Double` / `BigDecimal` 等 `Number` 类型
