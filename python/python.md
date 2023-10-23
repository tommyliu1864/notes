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

# 函数

函数是组织好的，可重复使用的，用来实现单一，或相关联功能的代码段。

```python
def add(a, b):
    result = a + b
    return result


r = add(10, 20)
print(f"结果：{r}")
```

## 函数返回值None类型

None作为一个特殊的字面量，用于表示：空、无意义，有非常多的应用场景。

- 用在函数无返回值上
- 用在if判断上
  - 在if判断中，None等同于False
  - 一般用于在函数中主动返回None，配合if判断做相关处理

- 用于声明无内容的变量上
  - 定义变量，但暂时不需要变量有具体值，可以用None来代替，`name = None`

```python
def check_age(age):
    if age > 18:
        return "SUCCESS"
    else:
        return None

result = check_age(16)

# None类型
print(type(result))
# None用于if判断
if not result:
    # 进入if表示result是None值，也就是False
    print("未成年，不可以进入")
```

输出结果：

```
<class 'NoneType'>
未成年，不可以进入
```

## 函数的文档说明

```python
def add(x, y):
    """
    对两个数进行相加操作
    :param x: 相加的第一个数
    :param y: 相加的第二个数
    :return: 两数相加的结果
    """
    result = x + y
    return result


print(f'结果：{add(10, 5)}')
```

当把光标移动到函数上：

<img src="./images/函数的文档说明.png" alt="函数的文档说明" style="zoom:67%;" />

## global

- 不实用global关键字的情况下，试图访问全局变量：

```python
number = 100

def func_a():
    # 这里声明的变量仍然为局部变量
    number = 200

func_a()

print(number)
```

输出结果：

```
100
```



- 使用global 关键字，在函数内声明变量为全局变量

```python
number = 100

def func_a():
    # 设置在函数内定义的变量为全局变量
    global number
    number = 200

func_a()

print(number)
```

输出结果：

```
200
```

## 函数的多返回值

```python
# 一次性返回多个值
def mutil_result():
    return 1, "Hello", True


x, y, z = mutil_result()
print(x, y, z)
```

## 函数参数

### 关键字参数

```python
def user_info(name, age, gender):
    print(f"姓名是：{name}，年龄是：{age}，性别是：{gender}")

# 位置参数，默认使用形式
user_info('小明', 20, '男')

# 关键字参数
user_info(name='小王', age=11, gender='女')
user_info(name='小王', gender='女', age=11)
```

### 缺损参数

```python
# 缺损参数
def user_info(name, age, gender='男'):
    print(f"姓名是：{name}，年龄是：{age}，性别是：{gender}")

user_info('小明', 20)
user_info('小红', 20, gender='女')
```

### 不定长参数

不定长参数：不定长参数也叫可变参数，用于不确定调用的时候会传递多少个参数（不传参也可以）的场景。

作用：当调用函数时不确定参数个数时，可以使用不定长参数。

不定长参数的类型：

1）位置传递

2）关键字传递

#### 位置传递

传进的所有参数都会被args变量收集，它会根据传进参数的位置合并为一个元组，args是元组类型

```python
def user_info(*args):
    print(type(args))
    print(args)

user_info('TOM', 18)
```

输出结果：

```
<class 'tuple'>
('TOM', 18)
```

#### 关键字传递

参数是“键=值”形式的情况下，所有的“键=值”都会被kwargs接受，同时会根据“键=值”组成字典

```python
def user_info(**kwargs):
    print(type(kwargs))
    print(kwargs)

user_info(name='TOM', age=18, id=100)
```

输出结果：

```
<class 'dict'>
{'name': 'TOM', 'age': 18, 'id': 100}
```

## 函数作为参数传递

这是一种计算逻辑的传递，而非数据的传递。

```python
def algorithm(compute):
    result = compute(1, 2)
    print(result)
    
def add(x, y):
    return x + y

algorithm(add)
```

## lambda 匿名函数

无名称的匿名函数，只能临时使用一次。

```python
def algorithm(compute):
    result = compute(1, 2)
    print(result)

algorithm(lambda x, y: x + y)
```

# 列表

## 定义

```python
my_list = ["apple", 100, True]
print(my_list)
print(type(my_list))
```

输出结果：

```
['apple', 100, True]
<class 'list'>
```

## 下标索引

```python
my_list = ["apple", 100, True]

# 从前往后取
print(my_list[0])
print(my_list[1])
print(my_list[2])

# 从后往前取
print(my_list[-1])
print(my_list[-2])
print(my_list[-3])
```

输出结果：

```
apple
100
True
True
100
apple
```

## 查找

```python
my_list = ["java", "python", "golang"]
# 查找某元素在列表内的下标索引
print(my_list.index("python"))
```

输出结果：

```
1
```

如果查找的元素不存在，会报错：

```
ValueError: 'kotlin' is not in list
```

## 修改

```python
my_list = ["java", "python", "golang"]
# 修改指定索引位置的元素
my_list[0] = "c++"
print(my_list)
```

输出结果：

```
['c++', 'python', 'golang']
```

## 插入

```python
my_list = ["java", "python", "golang"]
# 在指定下标位置插入新元素
my_list.insert(1, "kotlin")
print(my_list)
```

输出结果：

```
['java', 'kotlin', 'python', 'golang']
```

## 追加

```python
my_list = ["java", "python", "golang"]
# 在列表尾部追加单个新元素
my_list.append("c++")
print(my_list)
# 在列表尾部追加一批新元素
my_list.extend(["c", "javascript"])
print(my_list)
```

输出结果：

```
['java', 'python', 'golang', 'c++']
['java', 'python', 'golang', 'c++', 'c', 'javascript']
```

## 删除

```python
my_list = ["java", "python", "golang"]
print(f'删除前的列表：{my_list}')
# 删除指定索引位置元素
del my_list[2]
print(f'删除后的列表：{my_list}')

# 删除指定索引位置元素，并返回
element = my_list.pop(1)
print(f'删除后的列表：{my_list}')
print(f'被删除的元素：{element}')
```

输出结果：

```
删除前的列表：['java', 'python', 'golang']
删除后的列表：['java', 'python']
删除后的列表：['java']
被删除的元素：python
```

- 删除某元素在列表中的第一个匹配项

```python
my_list = ["java", "python", "golang", "python", "golang"]
print(f'删除前的列表：{my_list}')
my_list.remove("python")
print(f'删除后的列表：{my_list}')
```

输出结果：

```
删除前的列表：['java', 'python', 'golang', 'python', 'golang']
删除后的列表：['java', 'golang', 'python', 'golang']
```

## 清空

```python
my_list = ["java", "python", "golang"]
my_list.clear()
print(my_list)
```

输出结果：

```
[]
```

## 统计

- 统计某个元素在列表中的个数

```python
my_list = ["java", "python", "golang", "python", "golang"]
print(f'统计个数：{my_list.count("python")}')
```

输出结果：

```
统计个数：2
```

- 列表的长度

```python
my_list = ["java", "python", "golang", "python", "golang"]
print(f'列表的长度：{len(my_list)}')
```

输出结果：

```
列表的长度：5
```

## 遍历

- while 循环

```python
my_list = ["java", "python", "golang"]
index = 0
while index < len(my_list):
    element = my_list[index]
    print(element)
    index += 1
```

输出结果：

```
java
python
golang
```

- for 循环

```python
my_list = ["java", "python", "golang"]
for element in my_list:
    print(element)
```

输出结果：

```
java
python
golang
```

# 元组

Python中的元组（tuple）和列表（list）是两种不同的数据结构，它们在很多方面都有区别，主要包括以下几个方面：

1. 可变性：
   - 列表是可变的，可以随意添加、删除或修改其中的元素。
   - 元组是不可变的，一旦创建，就无法添加、删除或修改其中的元素。元组的元素是固定的。
2. 语法表示：
   - 列表使用方括号 [] 表示，例如：`my_list = [1, 2, 3]`。
   - 元组使用圆括号 () 表示，例如：`my_tuple = (1, 2, 3)`。
3. 性能：
   - 由于元组是不可变的，通常比列表更快，特别是在访问元素时。
   - 列表的可变性会导致在大规模元素添加/删除时产生更多的开销。
4. 适用场景：
   - 如果需要存储一组元素，但这些元素在整个程序执行期间不应该被改变，元组是更好的选择。
   - 如果需要一个容器，其中元素可以动态添加、删除或修改，列表是更合适的选项。
5. 内置方法：
   - 列表具有许多内置方法，如`append()`、`insert()`、`remove()`等，用于在列表上执行各种操作。
   - 元组由于不可变性，只支持有限的方法，如`count()`和`index()`。

```python
# 定义元组
t1 = ('abc', 88, True)
print(type(t1))
print(t1)

# 定义单个元素的元组
# 注意，必须带有逗号，否则不是元组类型
t2 = ("hello", )
print(t2)
```

输出结果：

```
<class 'tuple'>
('abc', 88, True)
88
('hello',)
```

# 字符串常用操作

## 通过下标索引取值

```python
my_str = "People don't need freedom"
# 通过下标索引取值
print(f'获取指定索引位置字符：{my_str[3]}')
print(f'获取指定索引位置字符：{my_str[-3]}')
```

输出结果：

```
获取指定索引位置字符：p
获取指定索引位置字符：d
```

## 获取字符串索引位置

```python
my_str = "People don't need freedom"
# 获取字符串索引位置
print(f'获取指定字符串的索引位置：{my_str.index("p")}')
```

输出结果：

```
获取指定字符串的索引位置：3
```

## 替换

```python
my_str = "People don't need freedom"
new_str = my_str.replace("need", "ask")
print(f'字符串被替换后，返回新的字符串：{new_str}')
```

输出结果：

```
字符串被替换后，返回新的字符串：People don't ask freedom
```

## 分割

```python
str_list = new_str.split(" ")
print(f'分割后返回的字符串列表：{str_list}')
```

输出结果：

```
分割后返回的字符串列表：['People', "don't", 'ask', 'freedom']
```

## strip

- 不传入参数

```python
my_str = " People don't need freedom "
# 去除前后空格
print(f'strip不传入参数，去除前后空格：{my_str.strip()}')
```

输出结果：

```
strip不传入参数，去除前后空格：People don't need freedom
```

- 传入参数

```python
my_str = "45People don't need freedom654"
print(f'strip传入参数：{my_str.strip("45")}')
```

输出结果：

```
strip传入参数：People don't need freedom6
```

## 统计个数

```python
# 统计字符串中某字符出现的次数
my_str = "People don't need freedom"
print(f'ee出现的次数：{my_str.count("ee")}')
# 字符串的长度
print(f'字符串的长度：{len(my_str)}')
```

输出结果：

```
ee出现的次数：2
字符串的长度：25
```

## 遍历

```python
# 遍历字符串
my_str = "People don't need freedom"
# while 循环遍历
index = 0
while index < len(my_str):
    print(my_str[index])
    index += 1

# for 循环遍历
for c in my_str:
    print(c)
```

# 序列

序列是指：内容连续、有序，可使用下标索引的一类数据容器

**列表、元组、字符串，均可以视为序列。**

**序列的常用操作 - 切片**

序列支持切片，即：列表、元组、字符串，均支持进行切片操作。

切片：从一个序列中，取出一个字序列。

语法：序列[起始下标:结束下标:步长]

表示从序列中，从指定位置开始，依次取出元素，到指定位置结束，得到一个新序列：

- 起始下标表示从何处开始，可以留空，留空视作从头开始
- 结束下标（不含）表示何处结束，可以留空，留空视作截取到结尾
- 步长表示依次取元素的间隔
  - 步长1表示，一个个取元素
  - 步长2表示，每次跳过一个元素取
  - 步长N表示，每次跳过N-1个元素取
  - 步长为负数表示，反向取（注意，起始下标和结束下标也要反向标记）

```python
# 对list进行切片，从1开始，4结束，步长1
my_list = [0, 1, 2, 3, 4, 5, 6]
print(f'结果1:{my_list[1:4]}')

# 对tuple进行切片，从头开始，到最后结束，步长1
my_tuple = (0, 1, 2, 3, 4, 5, 6)
print(f'结果2:{my_tuple[:]}')

# 对str进行切片，从头开始，到最后结束，步长-1
my_str = "0123456789"
# 9876543210，等同于将字符串反转了
print(f'结果3:{my_str[::-1]}')

# 对列表进行切片，从3开始，到1结束，步长-1
my_list = [0, 1, 2, 3, 4, 5, 6]
print(f'结果4:{my_list[3:1:-1]}')

# 对元组进行切片，从头开始，到尾结束，步长-2
my_tuple = (0, 1, 2, 3, 4, 5, 6)
print(f'结果5:{my_tuple[::-2]}')
```

输出结果：

```
结果1:[1, 2, 3]
结果2:(0, 1, 2, 3, 4, 5, 6)
结果3:9876543210
结果4:[3, 2]
结果5:(6, 4, 2, 0)
```

# 集合

集合（set）是一个无序的不重复元素序列。集合中的元素不会重复，并且可以进行交集、并集、差集等常见的集合操作。

可以使用大括号 **{ }** 创建集合，元素之间用逗号 **,** 分隔， 或者也可以使用 **set()** 函数创建集合。

```python
# 定义集合
my_set = {"Toyota", "Volkswagen", "Ford", "Honda", "Chevrolet", "Toyota", "Volkswagen"}
print(f'集合类型：{type(my_set)}')
print(f'集合内容：{my_set}')

# 添加新元素
my_set.add("Ford")
print(f'集合添加元素后：{my_set}')

# 移除元素
my_set.remove("Honda")
print(f'移除元素后：{my_set}')

# 随机取出一个元素
print(f'随机取出一个元素：{my_set.pop()}')
print(f'随机取出一个元素之后的集合：{my_set}')

# 清空集合
my_set.clear()
print(f'集合被清空之后：{my_set}')
```

输出结果：

```
集合类型：<class 'set'>
集合内容：{'Chevrolet', 'Volkswagen', 'Ford', 'Toyota', 'Honda'}
集合添加元素后：{'Chevrolet', 'Volkswagen', 'Ford', 'Toyota', 'Honda'}
移除元素后：{'Chevrolet', 'Volkswagen', 'Ford', 'Toyota'}
随机取出一个元素：Chevrolet
随机取出一个元素之后的集合：{'Volkswagen', 'Ford', 'Toyota'}
集合被清空之后：set()
```

- 取两个集合的差集（集合1有而集合2没有的）

  结果：得到一个新集合，集合1和集合2不变

```python
set1 = {1, 2, 3}
set2 = {1, 5, 6}
set3 = set1.difference(set2)
print(set3)
print(set1)
print(set2)
```

输出结果：

```	
{2, 3}
{1, 2, 3}
{1, 5, 6}
```

- 消除两个集合的差集（对比集合1和集合2，在集合1内，删除和集合2相同的元素）

  结果：集合1被修改，集合2不变

```python
set1 = {1, 2, 3}
set2 = {1, 5, 6}
set1.difference_update(set2)
print(set1)
print(set2)
```

输出结果：

```
{2, 3}
{1, 5, 6}
```

- 两个集合合并（将集合1和集合2组合成新集合）

  结果：得到新集合，集合1和集合2不变

```python
set1 = {1, 2, 3}
set2 = {1, 5, 6}
set3 = set1.union(set2)
print(set3)
print(set1)
print(set2)
```

输出结果：

```
{1, 2, 3, 5, 6}
{1, 2, 3}
{1, 5, 6}
```

# 容器的比较

## 比较

<img src="./images/容器比较.png" alt="容器比较" style="zoom:67%;" />

## 通用功能

<img src="./images/容器的通用功能.png" alt="容器的通用功能" style="zoom:67%;" />

# 文件

## 读取

- open 

打开文件

```python
open(name, mode, encoding)
```

name：要打开的目标文件名

mode：设置要打开文件的模式（访问模式）：只读、写入、追加等。

encoding：编码格式（推荐使用UTF-8）

- close 

关闭文件

- read() 方法

```python
f.read(num)
```

num 表示要从文件中读取的数据长度（单位是字节），如果没有传入num，那么就表示读取文件中所有的数据。

- readlines() 方法

按照行的方式把整个文件中的内容进行一次性读取，并且返回的是一个列表，其中每一行的数据为一个元素。

- readline() 方法

一次性读取一行内容

```python
# 打开文件
f = open('/Users/chenliang/test.txt', 'r', encoding="UTF-8")
print(f'读取10个字节：{f.read(10)}')
print(f'读取所有内容：{f.read()}')

# 一次性读取所有行
lines = f.readlines()
print(type(lines))
print(lines)

# 单行读取
line1 = f.readline()
print(f'第一行数据：{line1}')
line2 = f.readline()
print(f'第二行数据：{line2}')
line3 = f.readline()
print(f'第三行数据：{line3}')

# for 循环读取文件行
for line in f:
    print(line)

# 关闭文件
f.close()
```

- with 语法操作文件

```python
with open('/Users/chenliang/test.txt', 'r', encoding="UTF-8") as f:
    for line in f:
        print(line)
```

## 写出

```python
# 新建文件，或者覆盖
# f = open('/Users/chenliang/test.txt', 'w', encoding="UTF-8")
# 追加
f = open('/Users/chenliang/test.txt', 'a', encoding="UTF-8")
f.write("中国")
# 刷新缓存，写入磁盘
# f.flush()
f.close()
```

# 异常

## 基本捕获语法

```python
try:
    f = open('D:/a.txt', 'r', encoding='UTF-8')
except:
    print('出现异常了，因为文件不存在')
```

## 捕获指定异常

```python
try:
    1 / 0
except ZeroDivisionError as e:
    print('出现了除以零异常')
    print(e)
```

## 捕获多个异常

```python
try:
    f = open('D:/a.txt', 'r', encoding='UTF-8')
    1 / 0
except (FileNotFoundError, ZeroDivisionError) as e:
    print(e)
```

## 捕获所有异常

```python
try:
    f = open('D:/a.txt', 'r', encoding='UTF-8')
except Exception as e:
    print(e)
```

## else finally

```python
try:
    f = open('/Users/chenliang/a.txt', 'r', encoding='UTF-8')
except Exception as e:
    f = open('/Users/chenliang/a.txt', 'w', encoding='UTF-8')
    print('没有文件，并新建了一个文件')
else:
    print('已经有文件了，没有出现异常')
finally:
    f.close()
    print('始终都要关闭文件')
```

## 异常的传递性

```python
def func1():
    1 / 0

def func2():
    func1()

def func3():
    # 在func1中发生的异常传递到了func3中，并被捕获
    try:
        func2()
    except ZeroDivisionError as e:
        print('除以零异常')

func3()
```

# 模块

Python 模块（Module），是一个Python文件，以.py结尾。模块能定义函数、类和变量，模块里也能包含可执行的代码。

模块的作用：python中有很多各种不同的模块，每一个模块都可以帮助我们快速的实现一些功能，比如实现和时间相关的功能就可以使用time模块。我们可以认为一个模块就是一个工具包，每一个工具包中都有各种不同的工具供我们使用，进而实现各种不同的功能。

## 模块的导入方式

模块在使用前需要先导入，导入的语法如下：

[from 模块名] import [模块 ｜ 类 ｜ 变量 ｜ 函数 ｜ *] [as 别名]

常用的组合形式如：

- import 模块名

  ```python
  import time
  time.sleep(5)
  ```

- from 模块名 import 类、变量、方法等

  ```python
  from time import sleep
  sleep(5)
  ```

- from 模块名 import *

  ```python
  from time import *
  sleep(5)
  ```

- import 模块名 as 别名

  ```python
  import time as t
  t.sleep(5)
  ```

- from 模块名 import 功能名 as 别名

  ```python
  from time import sleep as s
  s(5)
  ```

## 自定义模块导入

- 导入模块

<img src="./images/制作自定义模块.png" alt="制作自定义模块" style="zoom:67%;" />

- 导入不同模块的同名功能时，当调用同名功能的时候，调用到的是后面导入的模块功能

<img src="./images/不同模块的同名功能.png" alt="不同模块的同名功能" style="zoom:67%;" />

## `__main__`

`my_module1.py`

```python
def test(a, b):
    print(a + b)

# 只有在运行当前文件时，才会执行test函数，导入文件时，不会执行
if __name__ == '__main__':
    test(5,55)
```

`test_my_module.py`

```python
import my_module1
```

## `__all__`

如果一个模块文件中`__all__`变量，当使用`from xxx import *`导入时，只能导入这个列表中的元素

<img src="./images/__all__.png" alt="__all__" style="zoom: 50%;" />

# 包

从物理上看，包就是一个文件夹，在该文件夹下包含了一个`__init__.py`文件，该文件夹可用于包含多个模块文件

从逻辑上看，包的本质依然是模块

<img src="./images/包.png" alt="包" style="zoom:67%;" />

包的作用：当我们的模块文件越来越多时，包可以帮助我们管理这些模块，包的作用就是包含多个模块，但包的本质依然是模块

三种不同的导入方式：

```python
# 第一种方式导入
import my_package.my_module1
import my_package.my_module2

my_package.my_module1.info_print1()
my_package.my_module2.info_print2()

# 第二种方式导入
from my_package import my_module1
from my_package import my_module2

my_module1.info_print1()
my_module2.info_print2()

# 第三种方式导入
from my_package.my_module1 import info_print1
from my_package.my_module2 import info_print2

info_print1()
info_print2()
```

- 通过`__all__`变量，控制`import *`

在`__init__.py`中设置`__all__`变量

```python
__all__=['my_module1']
```

表示`import*` 时，只能导入`my_module1`

```python
from my_package import *
my_module1.info_print1()
```

- 安装第三方包

```shell
pip install numpy
```

