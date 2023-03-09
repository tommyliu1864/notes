# ContextLoaderListener

Spring提供了监听器ContextLoaderListener，实现ServletContextListener接口，可监听 ServletContext的状态，在web服务器的启动，读取Spring的配置文件，创建Spring的IOC容器。web 应用中必须在web.xml中配置

```xml
<listener>
  <!--
		配置Spring的监听器，在服务器启动时加载Spring的配置文件
  	Spring配置文件默认位置和名称:/WEB-INF/applicationContext.xml
  	可通过上下文参数自定义Spring配置文件的位置和名称
  -->
  <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>

<!--自定义Spring配置文件的位置和名称-->
<context-param>
  <param-name>contextConfigLocation</param-name>
  <param-value>classpath:spring.xml</param-value>
</context-param>
```

# 准备工作

## 创建Maven Module

## 导入依赖

```xml
<dependencies>
  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>${spring.version}</version>
  </dependency>
  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-beans</artifactId>
    <version>${spring.version}</version>
  </dependency>
  <!--springmvc-->
  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-web</artifactId>
    <version>${spring.version}</version>
  </dependency>

  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>${spring.version}</version>
  </dependency>
  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
    <version>${spring.version}</version>
  </dependency>
  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aspects</artifactId>
    <version>${spring.version}</version>
  </dependency>
  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>${spring.version}</version>
  </dependency>
  <!-- Mybatis核心 -->
  <dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.7</version>
  </dependency>
  <!--mybatis和spring的整合包-->
  <dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis-spring</artifactId>
    <version>2.0.6</version>
  </dependency>
  <!-- 连接池 -->
  <dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.0.9</version>
  </dependency>
  <!-- junit测试 -->
  <dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
    <scope>test</scope>
  </dependency>
  <!-- MySQL驱动 -->
  <dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.32</version>
  </dependency>
  <!-- log4j日志 -->
  <dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
  </dependency>
  <dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
    <version>5.2.0</version>
  </dependency>
  <!-- 日志 -->
  <dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.2.3</version>
  </dependency>
  <!-- ServletAPI -->
  <dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>3.1.0</version>
    <scope>provided</scope>
  </dependency>
  <dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.12.1</version>
  </dependency>
  <dependency>
    <groupId>commons-fileupload</groupId>
    <artifactId>commons-fileupload</artifactId>
    <version>1.3.1</version>
  </dependency>
  <!-- Spring5和Thymeleaf整合包 -->
  <dependency>
    <groupId>org.thymeleaf</groupId>
    <artifactId>thymeleaf-spring5</artifactId>
    <version>3.0.12.RELEASE</version>
  </dependency>
</dependencies>

<properties>
  <spring.version>5.3.1</spring.version>
  <maven.compiler.source>11</maven.compiler.source>
  <maven.compiler.target>11</maven.compiler.target>
  <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```

## 创建表

```sql
CREATE TABLE `t_emp` (
  `emp_id` int(11) NOT NULL AUTO_INCREMENT,
  `emp_name` varchar(20) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `gender` char(1) DEFAULT NULL
  PRIMARY KEY (`emp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
```

## 配置web.xml

```xml
<!--配置springMVC的编码过滤器-->
<filter>
  <filter-name>CharacterEncodingFilter</filter-name>
  <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
  <init-param>
    <param-name>encoding</param-name>
    <param-value>UTF-8</param-value>
  </init-param>
  <init-param>
    <param-name>forceEncoding</param-name>
    <param-value>true</param-value>
  </init-param>
</filter>
<filter-mapping>
  <filter-name>CharacterEncodingFilter</filter-name>
  <url-pattern>/*</url-pattern>
</filter-mapping>

<!-- 配置处理请求方式PUT和DELETE的过滤器 -->
<filter>
  <filter-name>HiddenHttpMethodFilter</filter-name>
  <filter-class>org.springframework.web.filter.HiddenHttpMethodFilter
  </filter-class>
</filter>
<filter-mapping>
  <filter-name>HiddenHttpMethodFilter</filter-name>
  <url-pattern>/*</url-pattern>
</filter-mapping>

<!-- 配置SpringMVC的前端控制器，对浏览器发送的请求统一进行处理 -->
<servlet>
  <servlet-name>DispatcherServlet</servlet-name>
  <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
  <!-- 通过初始化参数指定SpringMVC配置文件的位置和名称 -->
  <init-param>
    <!-- contextConfigLocation为固定值 -->
    <param-name>contextConfigLocation</param-name>
    <!-- 使用classpath:表示从类路径查找配置文件，例如maven工程中的src/main/resources -->
    <param-value>classpath:springMVC.xml</param-value>
  </init-param>
  <!--
    作为框架的核心组件，在启动过程中有大量的初始化操作要做
    而这些操作放在第一次请求时才执行会严重影响访问速度
    因此需要通过此标签将启动控制DispatcherServlet的初始化时间提前到服务器启动时
   -->
  <load-on-startup>1</load-on-startup>
</servlet>

<servlet-mapping>
  <servlet-name>DispatcherServlet</servlet-name>
  <!--
    设置springMVC的核心控制器所能处理的请求的请求路径
    /所匹配的请求可以是/login或.html或.js或.css方式的请求路径
    但是/不能匹配.jsp请求路径的请求
  -->
  <url-pattern>/</url-pattern>
</servlet-mapping>

<listener>
  <!--
    配置Spring的监听器，在服务器启动时加载Spring的配置文件
    Spring配置文件默认位置和名称:/WEB-INF/applicationContext.xml
    可通过上下文参数自定义Spring配置文件的位置和名称
  -->
  <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>

<!--自定义Spring配置文件的位置和名称-->
<context-param>
  <param-name>contextConfigLocation</param-name>
  <param-value>classpath:spring.xml</param-value>
</context-param>
```

## 创建SpringMVC的配置文件并配置

```xml
<!--扫描组件-->
<context:component-scan base-package="com.example.ssm.controller"/>
<!-- 配置Thymeleaf视图解析器 -->
<bean id="viewResolver" class="org.thymeleaf.spring5.view.ThymeleafViewResolver">
  <property name="order" value="1"/>
  <property name="characterEncoding" value="UTF-8"/>
  <property name="templateEngine">
    <bean class="org.thymeleaf.spring5.SpringTemplateEngine">
      <property name="templateResolver">
        <bean class="org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver">
          <!-- 视图前缀 -->
          <property name="prefix" value="/WEB-INF/templates/"/>
          <!-- 视图后缀 -->
          <property name="suffix" value=".html"/>
          <property name="templateMode" value="HTML5"/>
          <property name="characterEncoding" value="UTF-8"/>
        </bean>
      </property>
    </bean>
  </property>
</bean>
  <!--
      处理静态资源，例如html、js、css、jpg
      若只设置该标签，则只能访问静态资源，其他请求则无法访问
      此时必须设置<mvc:annotation-driven/>解决问题
  -->
<mvc:default-servlet-handler/>
<mvc:annotation-driven/>

<!-- 配置访问首页的视图控制 -->
<mvc:view-controller path="/" view-name="index"/>
```

## 搭建MyBatis环境

### 创建属性文件jdbc.properties

```properties
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/ebook?serverTimezone=UTC
jdbc.username=root
jdbc.password=root123$
```

### 创建MyBatis的核心配置文件mybatis-config.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>

    <settings>
        <!--将下划线映射为驼峰-->
        <setting name="mapUnderscoreToCamelCase" value="true"/>
    </settings>

    <!--设置分页插件-->
    <plugins>
        <plugin interceptor="com.github.pagehelper.PageInterceptor"></plugin>
    </plugins>

</configuration>
```

### 创建Mapper接口和映射文件

```java
public interface EmployeeMapper {
    List<Employee> getEmployeeList();
}
```

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.example.ssm.mapper.EmployeeMapper" >
  <select id="getEmployeeList" resultType="Employee">
      select *
      from t_emp
  </select>
</mapper>
```

### 创建日志文件log4j.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE log4j:configuration SYSTEM "log4j.dtd">
<log4j:configuration xmlns:log4j="http://jakarta.apache.org/log4j/">
    <appender name="STDOUT" class="org.apache.log4j.ConsoleAppender">
        <param name="Encoding" value="UTF-8"/>
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern" value="%-5p %d{MM-dd HH:mm:ss,SSS}
%m  (%F:%L) \n"/>
        </layout>
    </appender>
    <logger name="java.sql">
        <level value="debug"/>
    </logger>
    <logger name="org.apache.ibatis">
        <level value="info"/>
    </logger>
    <root>
        <level value="debug"/>
        <appender-ref ref="STDOUT"/>
    </root>
</log4j:configuration>
```

## 创建Spring的配置文件并配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context" xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd">

    <!--扫描组件-->
    <context:component-scan base-package="com.example.ssm">
        <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
    </context:component-scan>

    <!-- 引入jdbc.properties -->
    <context:property-placeholder location="classpath:jdbc.properties"/>

    <!-- 配置Druid数据源 -->
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="${jdbc.driver}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>

    <!-- 事务管理 -->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>
    <!--
        开启事务的注解驱动
        通过注解@Transactional所标识的方法或标识的类中所有的方法，都会被事务管理器管理事务
    -->
    <tx:annotation-driven transaction-manager="transactionManager"/>

    <!-- 配置用于创建SqlSessionFactory的工厂bean -->
    <bean class="org.mybatis.spring.SqlSessionFactoryBean">
        <!-- 设置MyBatis配置文件的路径(可以不设置) -->
        <property name="configLocation" value="classpath:mybatis-config.xml"/>
        <!-- 设置数据源 -->
        <property name="dataSource" ref="dataSource"/>
        <!-- 设置类型别名所对应的包 -->
        <property name="typeAliasesPackage" value="com.example.ssm.pojo"/>
        <!--
            设置映射文件的路径
            若映射文件所在路径和mapper接口所在路径一致，则不需要设置
        -->
        <!--<property name="mapperLocations" value="classpath:mapper/*.xml" />-->
    </bean>

    <!--
        配置mapper接口的扫描配置
        由mybatis-spring提供，可以将指定包下所有的mapper接口创建动态代理
        并将这些动态代理作为IOC容器的bean管理
    -->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.example.ssm.mapper"/>
    </bean>

</beans>
```

## 测试功能

### 创建组件

```java
public class Employee {

    private Integer empId;

    private String empName;

    private Integer age;

    private String gender;

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName == null ? null : empName.trim();
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    public Employee(){}

    public Employee(Integer empId, String empName, Integer age, String gender) {
        this.empId = empId;
        this.empName = empName;
        this.age = age;
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Emp{" +
                "empId=" + empId +
                ", empName='" + empName + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                '}';
    }
}
```

创建控制层组件EmployeeController

```java
@Controller
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employee/page/{pageNum}")
    public String getEmployeeList(@PathVariable("pageNum") Integer pageNum, Model model) {
        PageInfo<Employee> page = employeeService.getEmployeeList(pageNum);
        model.addAttribute("page", page);
        return "employee_list";
    }
}
```

创建接口EmployeeService

```java
public interface EmployeeService {

    PageInfo<Employee> getEmployeeList(Integer pageNum);

}
```

创建实现类EmployeeServiceImpl

```java
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public PageInfo<Employee> getEmployeeList(Integer pageNum) {
        PageHelper.startPage(pageNum, 4);
        List<Employee> list = employeeMapper.getEmployeeList();
        PageInfo<Employee> page = new PageInfo<>(list, 5);
        return page;
    }
}
```

### 创建页面

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Employee Info</title>
</head>
<body>
<table>
    <tr>
        <th colspan="5">Employee Info</th>
    </tr>
    <tr>
        <th>emp_id</th>
        <th>emp_name</th>
        <th>age</th>
        <th>gender</th>
        <th>options</th>
    </tr>
    <tr th:each="employee : ${page.list}">
        <td th:text="${employee.empId}"></td>
        <td th:text="${employee.empName}"></td>
        <td th:text="${employee.age}"></td>
        <td th:text="${employee.gender}"></td>
        <td>
            <a href="">delete</a>
            <a href="">update</a>
        </td>
    </tr>
    <tr>
        <td colspan="5">
            <span th:if="${page.hasPreviousPage}">
                <a th:href="@{/employee/page/1}">首页</a>
                <a th:href="@{'/employee/page/' + ${page.prePage}}">上一页</a>
            </span>

            <span th:each="num: ${page.navigatepageNums}">
                <a th:if="${page.pageNum==num}" th:href="@{'/employee/page/' + ${num}}" th:text="'['+${num}+']'"
                   style="color: red;"></a>
                <a th:if="${page.pageNum!=num}" th:href="@{'/employee/page/' + ${num}}" th:text="${num}"></a>
            </span>

            <span th:if="${page.hasNextPage}">
                <a th:href="@{'/employee/page/1' + ${page.nextPage}}">下一页</a>
                <a th:href="@{'/employee/page/' + ${page.pages}}">末页</a>
            </span>
        </td>
    </tr>
</table>
</body>
</html>
```

### 访问测试分页功能

http://localhost:8080/ssm/employee/page/1
