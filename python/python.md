# 基础语法

## 注释

python中单行注释采用 **#** 开头。

```python
# 第一个注释
print ("Hello, Python!")  # 第二个注释
```

python 中多行注释使用三个单引号 **'''** 或三个双引号 **"""**。

```python
'''
这是多行注释，使用单引号。
这是多行注释，使用单引号。
这是多行注释，使用单引号。
'''

"""
这是多行注释，使用双引号。
这是多行注释，使用双引号。
这是多行注释，使用双引号。
"""
```

## 变量

- 赋值

Python 中的变量赋值不需要类型声明。每个变量在内存中创建，都包括变量的标识，名称和数据这些信息。每个变量在使用前都必须赋值，变量赋值以后该变量才会被创建。等号 **=** 用来给变量赋值。等号 **=** 运算符左边是一个变量名，等号 **=** 运算符右边是存储在变量中的值。例如：

```python
counter = 100 # 赋值整型变量
miles = 1000.0 # 浮点型
name = "John" # 字符串
```

- 多个变量赋值

Python允许你同时为多个变量赋值。例如：

```python
a = b = c = 1
```

以上实例，创建一个整型对象，值为1，三个变量被分配到相同的内存空间上。

您也可以为多个对象指定多个变量。例如：

```python
a, b, c = 1, 2, "john"
```

## 数据类型

常见的几种数据类型，字符串、int、float

```python
# 字符串
print(type("abc"))
# int
print(type(12))
# float
print(type(12.89))
```

执行结果

```
<class 'str'>
<class 'int'>
<class 'float'>
```

- 类型转换

```python
# 字符串转为int
int_num = int("12")
print(int_num)

# 字符串转为float
float_num = float("12.89")
print(float_num)
```

## 算术运算符

以下假设变量 **a=10**，变量 **b=21**：

| 运算符 | 描述                                            | 实例                 |
| :----- | :---------------------------------------------- | :------------------- |
| +      | 加 - 两个对象相加                               | a + b 输出结果 31    |
| -      | 减 - 得到负数或是一个数减去另一个数             | a - b 输出结果 -11   |
| *      | 乘 - 两个数相乘或是返回一个被重复若干次的字符串 | a * b 输出结果 210   |
| /      | 除 - x 除以 y                                   | b / a 输出结果 2.1   |
| %      | 取模 - 返回除法的余数                           | b % a 输出结果 1     |
| **     | 幂 - 返回x的y次幂                               | a**b 为10的21次方    |
| //     | 取整除 - 往小的方向取整数                       | 9//2 = 4  -9//2 = -5 |

## 布尔类型

布尔类型即 True 或 False。在 Python 中，True 和 False 都是关键字，表示布尔值。

布尔类型可以用来控制程序的流程，比如判断某个条件是否成立，或者在某个条件满足时执行某段代码。

```python
a = True
b = False
print(a)
print(b)
print(type(a))

# 比较运算符
print(2 > 3)
```

输出：

```
True
False
<class 'bool'>
False
```

# 字符串

## 定义

```python
# 单引号定义
name = 'abc'
# 双引号定义
name = "efg"
# 三引号定义
name = """
abc
hij
"""
```

## 字符串拼接

```python
name = "张三"
print("姓名：" + name)

age = 18
# 报错，无法和非字符串类型进行拼接
print("年龄：" + age)
```

## 字符串格式化

Python 支持格式化字符串的输出 。尽管这样可能会用到非常复杂的表达式，但最基本的用法是将一个值插入到一个有字符串格式符 %s 的字符串中。

```python
print("我叫%s，今年%d岁" % ('小明', 18))
```

### f-string

f-string 是 python3.6 之后版本添加的，称之为字面量格式化字符串，是新的格式化字符串的语法。

```python
name = "小明"
age = 18
print(f"我叫{name}，今年{age}岁")
```

### 数字精度控制

我们可以使用辅助符号 ***m.n***来控制数据的宽度和精度

- m，控制宽度，要求是数字，设置的宽度小于数字自身，不生效

- .n，控制小数点的精度，要求是数字，会进行小数的四舍五入

示例：

- %5d：表示将整数的宽度控制在5位，如数字11，被设置为5d，就会变成，【空格】【空格】【空格】11，用三个空格补足宽度。
- %5.2f：表示将宽度控制为5，将小数点精度设置为2，小数点和小数部分也算入宽度计算。如，对11.345设置了%7.2f后，结果是：【空格】【空格】11.35。2个空格补足宽度，小数部分限制2位精度后，四舍五入为 .35。

```python
num1 = 11
num2 = 11.345

print("%5d" % num1)
print("%7.2f" % num2)
```

输出：

```
   11
  11.35
```

# 条件控制

Python 条件语句是通过一条或多条语句的执行结果（True 或者 False）来决定执行的代码块。

```python
age = int(input("请输入你家狗狗的年龄："))
print("")
if age <= 0:
    print("你是在逗我吧!")
elif age == 1:
    print("相当于 14 岁的人。")
elif age == 2:
    print("相当于 22 岁的人。")
elif age > 2:
    human = 22 + (age - 2) * 5
    print("对应人类年龄: ", human)

# 退出提示
input("点击 enter 键退出")
```

# 循环语句

## while

以下实例使用了 while 来计算 1 到 100 的总和：

```python
n = 100
 
sum = 0
counter = 1
while counter <= n:
    sum = sum + counter
    counter += 1
 
print("1 到 %d 之和为: %d" % (n,sum))
```

执行结果如下：

```
1 到 100 之和为: 5050
```

## for

for 循环可以遍历任何可迭代对象，如一个列表或者一个字符串。

- 输出字符串列表中的每个元素：

```python
sites = ["Baidu", "Google","Runoob","Taobao"]
for site in sites:
    print(site)
```

输出结果：

```
Baidu
Google
Runoob
Taobao
```

- 也可用于打印字符串中的每个字符：

```python
word = 'runoob'

for letter in word:
    print(letter)
```

以上代码执行输出结果为：

```
r
u
n
o
o
b
```

- 整数范围值可以配合 range() 函数使用：

```python
#  1 到 5 的所有数字：
for number in range(1, 6):
    print(number)
```

以上代码执行输出结果为：

```
1
2
3
4
5
```

## break

**break** 语句可以跳出 for 和 while 的循环体。如果你从 for 或 while 循环中终止，任何对应的循环 else 块将不执行。

```python
n = 5
while n > 0:
    n -= 1
    if n == 2:
        break
    print(n)
print('循环结束。')
```

输出结果为：

```
4
3
循环结束。
```

## continue

**continue** 语句被用来告诉 Python 跳过当前循环块中的剩余语句，然后继续进行下一轮循环。

```python
n = 5
while n > 0:
    n -= 1
    if n == 2:
        continue
    print(n)
print('循环结束。')
```

输出结果为：

```
4
3
1
0
循环结束。
```

