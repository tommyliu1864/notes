# Spring简介

## Spring 概述

官网地址：https://spring.io/

Spring 是最受欢迎的企业级 Java 应用程序开发框架，数以百万的来自世界各地的开发人员使用Spring 框架来创建性能好、易于测试、可重用的代码。

Spring 框架是一个开源的 Java 平台，它最初是由 Rod Johnson 编写的，并且于 2003 年 6 月首次在 Apache 2.0 许可下发布。

Spring 是轻量级的框架，其基础版本只有 2 MB 左右的大小。

Spring 框架的核心特性是可以用于开发任何 Java 应用程序，但是在 Java EE 平台上构建 web 应用程序是需要扩展的。 Spring 框架的目标是使 J2EE 开发变得更容易使用，通过启用基于 POJO 编程模型来促进良好的编程实践。

## Spring 家族

项目列表：https://spring.io/projects

## Spring Framework

Spring 基础框架，可以视为 Spring 基础设施，基本上任何其他 Spring 项目都是以 Spring Framework 为基础的。

### Spring Framework 特性

- 非侵入式：使用 Spring Framework 开发应用程序时，Spring 对应用程序本身的结构影响非常小。对领域模型可以做到零污染；对功能性组件也只需要使用几个简单的注解进行标记，完全不会破坏原有结构，反而能将组件结构进一步简化。这就使得基于 Spring Framework 开发应用程序时结构清晰、简洁优雅。
- 控制反转：IOC——Inversion of Control，翻转资源获取方向。把自己创建资源、向环境索取资源变成环境将资源准备好，我们享受资源注入。
- 面向切面编程：AOP——Aspect Oriented Programming，在不修改源代码的基础上增强代码功能。
- 容器：Spring IOC 是一个容器，因为它包含并且管理组件对象的生命周期。组件享受到了容器化的管理，替程序员屏蔽了组件创建过程中的大量细节，极大的降低了使用门槛，大幅度提高了开发效率。
- 组件化：Spring 实现了使用简单的组件配置组合成一个复杂的应用。在 Spring 中可以使用 XML 和 Java 注解组合这些对象。这使得我们可以基于一个个功能明确、边界清晰的组件有条不紊的搭建超大型复杂应用系统。 
- 声明式：很多以前需要编写代码才能实现的功能，现在只需要声明需求即可由框架代为实现。 
- 一站式：在 IOC 和 AOP 的基础上可以整合各种企业应用的开源框架和优秀的第三方类库。而且 Spring 旗下的项目已经覆盖了广泛领域，很多方面的功能性需求可以在 Spring Framework 的基 础上全部使用 Spring 来实现。

### Spring Framework五大功能模块

| **功能模块**            | **功能介绍**                                                |
| ----------------------- | ----------------------------------------------------------- |
| Core Container          | 核心容器，在 Spring 环境下使用任何功能都必须基于 IOC 容器。 |
| AOP&Aspects             | 面向切面编程                                                |
| Testing                 | 提供了对 junit 或 TestNG 测试框架的整合。                   |
| Data Access/Integration | 提供了对数据访问/集成的功能。                               |
| Spring MVC              | 提供了面向Web应用程序的集成功能。                           |

# IOC

## IOC容器

### IOC思想

IOC:Inversion of Control，翻译过来是**反转控制**。

1. 获取资源的传统方式

自己做饭:买菜、洗菜、择菜、改刀、炒菜，全过程参与，费时费力，必须清楚了解资源创建整个过程中的全部细节且熟练掌握。

在应用程序中的组件需要获取资源时，传统的方式是组件主动的从容器中获取所需要的资源，在这样的模式下开发人员往往需要知道在具体容器中特定资源的获取方式，增加了学习成本，同时降低了开发效率。

2. 反转控制方式获取资源

点外卖：下单、等、吃，省时省力，不必关心资源创建过程的所有细节。

反转控制的思想完全颠覆了应用程序组件获取资源的传统方式：反转了资源的获取方向——改由容器主动的将资源推送给需要的组件，开发人员不需要知道容器是如何创建资源对象的，只需要提供接收资源的方式即可，极大的降低了学习成本，提高了开发的效率。这种行为也称为查找的**被动**形式。

3. DI

DI：Dependency Injection，翻译过来是依赖注入。

DI 是 IOC 的另一种表述方式：即组件以一些预先定义好的方式(例如:setter 方法)接受来自于容器的资源注入。相对于IOC而言，这种表述更直接。

所以结论是：IOC 就是一种反转控制的思想， 而DI是对IOC的一种具体实现。

### IOC容器在Spring中的实现

Spring 的 IOC 容器就是 IOC 思想的一个落地的产品实现。IOC 容器中管理的组件也叫做 bean。在创建 bean 之前，首先需要创建 IOC 容器。Spring 提供了 IOC 容器的两种实现方式：

1. BeanFactory

这是 IOC 容器的基本实现，是 Spring 内部使用的接口。面向 Spring 本身，不提供给开发人员使用。

2. ApplicationContext

BeanFactory 的子接口，提供了更多高级特性。面向 Spring 的使用者，几乎所有场合都使用 ApplicationContext 而不是底层的 BeanFactory。

3. ApplicationContext的主要实现类

![ApplicationContext的主要实现类](./images/ApplicationContext的主要实现类.png)

| **类型名**                      | **简介**                                                     |
| ------------------------------- | ------------------------------------------------------------ |
| ClassPathXmlApplicationContext  | 通过读取类路径下的 XML 格式的配置文件创建 IOC 容器 对象      |
| FileSystemXmlApplicationContext | 通过文件系统路径读取 XML 格式的配置文件创建 IOC 容 器对象    |
| ConfigurableApplicationContext  | ApplicationContext 的子接口，包含一些扩展方法 refresh() 和 close() ，让 ApplicationContext 具有启动、 关闭和刷新上下文的能力。 |
| WebApplicationContext           | 专门为 Web 应用准备，基于 Web 环境创建 IOC 容器对 象，并将对象引入存入 ServletContext 域中。 |

## 基于XML管理bean

### 实验一：入门案例

1. 创建Maven Module

2. 引入依赖

```xml
<dependencies>
  <!-- 基于Maven依赖传递性，导入spring-context依赖即可导入当前所需所有jar包 -->
  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>5.3.1</version>
  </dependency>
  <!-- junit测试 -->
  <dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
    <scope>test</scope>
  </dependency>
</dependencies>
```

3. 创建类HelloWorld

```java
public class HelloWorld {
  public void sayHello(){
  	System.out.println("helloworld");
	}
}
```

4. 创建Spring的配置文件

![创建Spring的配置文件](./images/创建Spring的配置文件.png)

5. 在Spring的配置文件中配置bean

```xml
<!-- 
配置HelloWorld所对应的bean，即将HelloWorld的对象交给Spring的IOC容器管理 
通过bean标签配置IOC容器所管理的bean
属性:
id：设置bean的唯一标识 
class：设置bean所对应类型的全类名
-->
<bean id="helloworld" class="com.atguigu.spring.bean.HelloWorld"></bean>
```

6. 创建测试类测试

```java
@Test
public void testHelloWorld(){
  // 获取IOC容器
  ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
  HelloWorld helloWorld = (HelloWorld) applicationContext.getBean("helloWorld");
  helloWorld.sayHello();
}
```

7. 思路

![思路](./images/思路.png)

8. 注意

Spring 底层默认通过反射技术调用组件类的无参构造器来创建组件对象，这一点需要注意。如果在需要无参构造器时，没有无参构造器，则会抛出下面的异常：

```
org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'helloWorld' defined in class path resource [applicationContext.xml]: Instantiation of bean failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [com.example.spring.HelloWorld]: No default constructor found; nested exception is java.lang.NoSuchMethodException: com.example.spring.HelloWorld.<init>()
```

### 实验二：获取bean

**方式一：根据id获取**

由于 id 属性指定了 bean 的唯一标识，所以根据 bean 标签的 id 属性可以精确获取到一个组件对象。 上个实验中我们使用的就是这种方式。

**方式二：根据类型获取**

```java
@Test
public void testHelloWorld(){
  ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
  HelloWorld helloWorld = applicationContext.getBean(HelloWorld.class);
  helloWorld.sayHello();
}
```

**方式三：根据id和类型**

```java
@Test
public void testHelloWorld(){
  ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
  HelloWorld helloWorld = applicationContext.getBean("helloWorld",HelloWorld.class);
  helloWorld.sayHello();
}
```

**注意**

当根据类型获取bean时，要求IOC容器中指定类型的bean有且只能有一个 

当IOC容器中一共配置了两个：

```xml
<bean id="helloWorldA" class="com.example.spring.HelloWorld"/>
<bean id="helloWorldB" class="com.example.spring.HelloWorld"/>
```

根据类型获取时会抛出异常：

```
org.springframework.beans.factory.NoUniqueBeanDefinitionException: No qualifying bean of type 'com.example.spring.HelloWorld' available: expected single matching bean but found 2: helloWorldA,helloWorldB
```

**扩展**

如果组件类实现了接口，根据接口类型可以获取 bean 吗?

可以，前提是bean唯一

如果一个接口有多个实现类，这些实现类都配置了 bean，根据接口类型可以获取 bean 吗?

不行，因为bean不唯一

**结论**

根据类型来获取bean时，在满足bean唯一性的前提下，其实只是看：『对象 instanceof 指定的类 型』的返回结果，只要返回的是true就可以认定为和类型匹配，能够获取到。

### 实验三：依赖注入之setter注入

1. 创建学生类Student

```java
public class Student {

    private Integer id;
    private String name;
    private Integer age;
    private String sex;

    public Student(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                '}';
    }
}
```

2. 配置bean时为属性赋值

```xml
<bean id="studentA" class="com.example.mybatis.pojo.Student">
  <!-- property标签：通过组件类的setXxx()方法给组件对象设置属性 -->
	<!-- name属性：指定属性名(这个属性名是getXxx()、setXxx()方法定义的，和成员变量无关)-->
	<!-- value属性：指定属性值 -->
  <property name="id" value="1" />
  <property name="name" value="张三"/>
  <property name="age" value="18"/>
  <property name="sex" value="男"/>
</bean>
```

3. 测试

```java
@Test
public void testIOCBySet(){
  ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
  Student student = applicationContext.getBean("studentA",Student.class);
  System.out.println(student);
}
```

### 实验四：依赖注入之构造器注入

1. 在Student类中添加有参构造

```java
public Student(Integer id, String name, Integer age, String sex) {
  this.id = id;
  this.name = name;
  this.age = age;
  this.sex = sex;
}
```

2. 配置bean

```xml
<bean id="studentB" class="com.example.mybatis.pojo.Student">
  <constructor-arg name="id" value="1"/>
  <constructor-arg name="name" value="李四"/>
  <constructor-arg name="age" value="20"/>
  <constructor-arg name="sex" value="女"/>
</bean>
```

注意，constructor-arg 标签还有两个属性可以进一步描述构造器参数：

- index属性：指定参数所在位置的索引(从0开始) 
- name属性：指定参数名

3. 测试

```java
@Test
public void testIOCByConstructor(){
  ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
  Student student = applicationContext.getBean("studentB",Student.class);
  System.out.println(student);
}
```

### 实验五：特殊值处理

1. null值

```xml
<property name="name">
    <null />
</property>
```

注意：

```xml
<property name="name" value="null"></property>
```

以上写法，为name所赋的值是字符串null

2. xml实体

```xml
<!-- 小于号在XML文档中用来定义标签的开始，不能随便使用 -->
<!-- 解决方案一：使用XML实体来代替 -->
<property name="expression" value="a &lt; b"/>
```

3. CDATA 节

```xml
<property name="expression">
	<!-- 解决方案二：使用CDATA节 -->
	<!-- CDATA中的C代表Character，是文本、字符的含义，CDATA就表示纯文本数据 -->
  <!-- XML解析器看到CDATA节就知道这里是纯文本，就不会当作XML标签或属性来解析 --> <!-- 所以CDATA节中写什么符号都随意 -->
	<value><![CDATA[a < b]]></value>
</property>
```

### 实验六：为类类型属性赋值

1. 创建学校类School

```java
public class School {
    private Integer schoolId;
    private String schoolName;

    public School(){}

    public School(Integer schoolId, String schoolName) {
        this.schoolId = schoolId;
        this.schoolName = schoolName;
    }
    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    @Override
    public String toString() {
        return "School{" +
                "schoolId=" + schoolId +
                ", schoolName='" + schoolName + '\'' +
                '}';
    }
}
```

2. 修改Student类

在Student类中添加以下代码:

```java
private School school;

public School getSchool() {
  return school;
}

public void setSchool(School school) {
  this.school = school;
}
```

3. 方式一：引用外部已声明的bean

配置School类型的bean：

```xml
<bean id="school" class="com.example.mybatis.pojo.School">
  <property name="schoolId" value="1"/>
  <property name="schoolName" value="哈佛大学"/>
</bean>
```

为Student中的school属性赋值：

```xml
<bean id="studentC" class="com.example.mybatis.pojo.Student">
  <property name="id" value="1"/>
  <property name="name" value="张三"/>
  <property name="age" value="18"/>
  <property name="sex" value="男"/>
  <!-- ref属性：引用IOC容器中某个bean的id，将所对应的bean为属性赋值 -->
  <property name="school" ref="school"/>
</bean>
```

4. 方式二：内部bean

```xml
<bean id="studentD" class="com.example.mybatis.pojo.Student">
  <property name="id" value="1"/>
  <property name="name" value="张三"/>
  <property name="age" value="18"/>
  <property name="sex" value="男"/>
  <property name="school">
    <!-- 在一个bean中再声明一个bean就是内部bean -->
		<!-- 内部bean只能用于给属性赋值，不能在外部通过IOC容器获取，因此可以省略id属性 -->
    <bean class="com.example.mybatis.pojo.School">      
      <property name="schoolId" value="1"/>
      <property name="schoolName" value="哈佛大学"/>
    </bean>
  </property>
</bean>
```

5. 方式三：级联属性赋值

```xml
<bean id="studentE" class="com.example.mybatis.pojo.Student">
  <property name="id" value="1"/>
  <property name="name" value="张三"/>
  <property name="age" value="18"/>
  <property name="sex" value="男"/>
  <!-- 一定先引用某个bean为属性赋值，才可以使用级联方式更新属性 -->
  <property name="school" ref="school"/>
  <property name="school.schoolId" value="2"/>
  <property name="school.schoolName" value="耶鲁大学"/>
</bean>
```

### 实验七：为数组类型属性赋值

1. 修改Student类

在Student类中添加以下代码：

```java
private String[] hobbies;

public String[] getHobbies() {
  return hobbies;
}

public void setHobbies(String[] hobbies) {
  this.hobbies = hobbies;
}
```

2. 配置bean

```xml
<bean id="studentC" class="com.example.mybatis.pojo.Student">
  <property name="id" value="1"/>
  <property name="name" value="张三"/>
  <property name="age" value="18"/>
  <property name="sex" value="男"/>
  <property name="school" ref="school"/>
  <property name="hobbies">
    <array>
      <value>抽烟</value>
      <value>喝酒</value>
      <value>烫头</value>
    </array>
  </property>
</bean>
```

### 实验八：为集合类型属性赋值

1. 为List集合类型属性赋值

在Student类中添加以下代码：

```java
private List<Teacher> teachers;

public List<Teacher> getTeachers() {
  return teachers;
}

public void setTeachers(List<Teacher> teachers) {
  this.teachers = teachers;
}
```

Teacher 类：

```java
public class Teacher {

    private String name;

    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Teacher() {
    }

    public Teacher(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
```

配置bean

```xml
<bean id="teacherA" class="com.example.mybatis.pojo.Teacher">
  <property name="name" value="乔峰"/>
  <property name="age" value="30"/>
</bean>

<bean id="teacherB" class="com.example.mybatis.pojo.Teacher">
  <property name="name" value="段誉"/>
  <property name="age" value="20"/>
</bean>

<bean id="teacherC" class="com.example.mybatis.pojo.Teacher">
  <property name="name" value="张三丰"/>
  <property name="age" value="80"/>
</bean>

<bean id="studentC" class="com.example.mybatis.pojo.Student">
  <property name="id" value="1"/>
  <property name="name" value="张三"/>
  <property name="age" value="18"/>
  <property name="sex" value="男"/>
  <property name="school" ref="school"/>
  <property name="hobbies">
    <array>
      <value>抽烟</value>
      <value>喝酒</value>
      <value>烫头</value>
    </array>
  </property>
  <property name="teachers">
    <list>
      <ref bean="teacherA"/>
      <ref bean="teacherB"/>
      <ref bean="teacherC"/>
    </list>
  </property>
</bean>
```

若为Set集合类型属性赋值，只需要将其中的list标签改为set标签即可。

2. 为Map集合类型属性赋值

在Student类中添加以下代码：

```java
//最喜欢的三个老师
private Map<String, Teacher> favoriteTeachers;

public Map<String, Teacher> getFavoriteTeachers() {
  return favoriteTeachers;
}

public void setFavoriteTeachers(Map<String, Teacher> favoriteTeachers) {
  this.favoriteTeachers = favoriteTeachers;
}
```

配置bean：

```xml
<bean id="studentC" class="com.example.mybatis.pojo.Student">
  <property name="id" value="1"/>
  <property name="name" value="张三"/>
  <property name="age" value="18"/>
  <property name="sex" value="男"/>
  <property name="school" ref="school"/>
  <property name="hobbies">
    <array>
      <value>抽烟</value>
      <value>喝酒</value>
      <value>烫头</value>
    </array>
  </property>
  <property name="favoriteTeachers">
    <map>
      <entry>
        <key>
          <value>top1</value>
        </key>
        <ref bean="teacherA"/>
      </entry>
      <entry>
        <key>
          <value>top2</value>
        </key>
        <ref bean="teacherB"/>
      </entry>
      <entry>
        <key>
          <value>top3</value>
        </key>
        <ref bean="teacherC"/>
      </entry>
    </map>
  </property>
</bean>
```

3. 引用集合类型的bean

```xml
<!--list集合类型的bean-->
<util:list id="teacherList">
  <ref bean="teacherA"/>
  <ref bean="teacherB"/>
  <ref bean="teacherC"/>
</util:list>
<!--map集合类型的bean-->
<util:map id="teacherMap">
  <entry>
    <key>
      <value>top1</value>
    </key>
    <ref bean="teacherA"/>
  </entry>
  <entry>
    <key>
      <value>top2</value>
    </key>
    <ref bean="teacherB"/>
  </entry>
  <entry>
    <key>
      <value>top3</value>
    </key>
    <ref bean="teacherC"/>
  </entry>
</util:map>

<bean id="studentF" class="com.example.mybatis.pojo.Student">
  <property name="id" value="1"/>
  <property name="name" value="张三"/>
  <property name="age" value="18"/>
  <property name="sex" value="男"/>
  <property name="school" ref="school"/>
  <property name="hobbies">
    <array>
      <value>抽烟</value>
      <value>喝酒</value>
      <value>烫头</value>
    </array>
  </property>
  <property name="teachers" ref="teacherList"/>
  <property name="favoriteTeachers" ref="teacherMap"/>
</bean>
```

使用util:list、util:map标签必须引入相应的命名空间，可以通过idea的提示功能选择

### 实验九：p命名空间

引入p命名空间后，可以通过以下方式为bean的各个属性赋值

```xml
<bean id="studentG" class="com.example.mybatis.pojo.Student"
      p:id="2"
      p:name="张三"
      p:age="18"
      p:sex="男"
      p:school-ref="school"
      p:teachers-ref="teacherList"
/>
```

### 实验十：引入外部属性文件

1. 加入依赖

```xml
<!-- MySQL驱动 -->
<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <version>8.0.32</version>
</dependency>
<!-- 数据源 -->
<dependency>
  <groupId>com.alibaba</groupId>
  <artifactId>druid</artifactId>
  <version>1.0.31</version>
</dependency>
```

2. 创建外部属性文件

```properties
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/ssm?serverTimezone=UTC
jdbc.username=root
jdbc.password=root123$
```

![创建外部属性文件](./images/创建外部属性文件.png)

3. 引入属性文件

```xml
<!-- 引入外部属性文件 -->
<context:property-placeholder location="jdbc.properties"/>
```

4. 配置bean

```xml
<!-- 配置数据源 -->
<bean id="druidDataSource" class="com.alibaba.druid.pool.DruidDataSource">
  <property name="url" value="${jdbc.url}"/>
  <property name="driverClassName" value="${jdbc.driver}"/>
  <property name="username" value="${jdbc.username}"/>
  <property name="password" value="${jdbc.password}"/>
</bean>
```

5. 测试

```java
@Test
public void testDatasource() throws SQLException {
  ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-datasource.xml");
  DataSource dataSource = applicationContext.getBean(DataSource.class);
  Connection connection = dataSource.getConnection();
  System.out.println(connection);
}
```

