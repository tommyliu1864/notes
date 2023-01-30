# MyBatis简介

## MyBatis历史

MyBatis最初是Apache的一个开源项目**iBatis**, 2010年6月这个项目由Apache Software Foundation迁 移到了Google Code。随着开发团队转投Google Code旗下， iBatis3.x正式更名为MyBatis。代码于 2013年11月迁移到Github。 iBatis一词来源于“internet”和“abatis”的组合，是一个基于Java的持久层框架。 iBatis提供的持久层框架 包括SQL Maps和Data Access Objects(DAO)。

## MyBatis特性

1) MyBatis 是支持定制化 SQL、存储过程以及高级映射的优秀的持久层框架
2) MyBatis 避免了几乎所有的 JDBC 代码和手动设置参数以及获取结果集
3) MyBatis可以使用简单的XML或注解用于配置和原始映射，将接口和Java的POJO(Plain Old Java Objects，普通的Java对象)映射成数据库中的记录
4) MyBatis 是一个 半自动的ORM(Object Relation Mapping)框架

## MyBatis下载

MyBatis下载地址:https://github.com/mybatis/mybatis-3

![MyBatis下载1](./images/MyBatis下载1.png)

![MyBatis下载2](./images/MyBatis下载2.png)

## 和其它持久化层技术对比

- JDBC

  - SQL 夹杂在Java代码中耦合度高，导致硬编码内伤 

  - 维护不易且实际开发需求中 SQL 有变化，频繁修改的情况多见 

  - 代码冗长，开发效率低

- Hibernate 和 JPA

  - 操作简便，开发效率高

  - 程序中的长难复杂 SQL 需要绕过框架
  - 内部自动生产的 SQL，不容易做特殊优化 
  - 基于全映射的全自动框架，大量字段的 POJO 进行部分映射时比较困难。 
  - 反射操作太多，导致数据库性能下降

- MyBatis

  - 轻量级，性能出色

  - SQL 和 Java 编码分开，功能边界清晰。Java代码专注业务、SQL语句专注数据

  - 开发效率稍逊于HIbernate，但是完全能够接受

# 搭建MyBatis

## 开发环境

IDE:idea 2019.2 

构建工具:maven 3.5.4 

MySQL版本:MySQL 8 

MyBatis版本:MyBatis 3.5.7

> MySQL不同版本的注意事项
>
> 1、驱动类driver-class-name
>
> MySQL 5版本使用jdbc5驱动，驱动类使用:com.mysql.jdbc.Driver
>
> MySQL 8版本使用jdbc8驱动，驱动类使用:com.mysql.cj.jdbc.Driver
>
> 2、连接地址url
>
> MySQL 5版本的url:
>
> jdbc:mysql://localhost:3306/ssm
>
> MySQL 8版本的url:
>
> jdbc:mysql://localhost:3306/ssm?serverTimezone=UTC
>
> 否则运行测试用例报告如下错误:
>
> java.sql.SQLException: The server time zone value 'ÖÐ1ú±ê×1⁄4Ê±1⁄4ä' is unrecognized or represents more

## 创建maven工程

1. 打包方式:jar 

2. 引入依赖

```xml
<dependencies>
  <!-- Mybatis核心 -->
  <dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.7</version>
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
    <version>8.0.16</version>
  </dependency>
</dependencies>
```

## 创建MyBatis的核心配置文件

> 习惯上命名为mybatis-config.xml，这个文件名仅仅只是建议，并非强制要求。将来整合Spring之后，这个配置文件可以省略，所以大家操作时可以直接复制、粘贴。 核心配置文件主要用于配置连接数据库的环境以及MyBatis的全局配置信息 核心配置文件存放的位置是src/main/resources目录下

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
<!--设置连接数据库的环境--> <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/ssm?
serverTimezone=UTC"/>
                <property name="username" value="root"/>
                <property name="password" value="123456"/>
            </dataSource>
        </environment>
    </environments>
<!--引入映射文件--> <mappers>
        <package name="mappers/UserMapper.xml"/>
    </mappers>
</configuration>
```

## 创建mapper接口

> MyBatis中的mapper接口相当于以前的dao。但是区别在于，mapper仅仅是接口，我们不需要提供实现类。

```java
public interface UserMapper {
    //添加用户信息
    int insertUser();
}
```

## 创建MyBatis的映射文件

相关概念:ORM(Object Relationship Mapping)对象关系映射。

- 对象:Java的实体类对象 
- 关系:关系型数据库 
- 映射:二者之间的对应关系

| Java 概念 | 数据库概念 |
| --------- | ---------- |
| 类        | 表         |
| 属性      | 字段/列    |
| 对象      | 记录/行    |

> 1、映射文件的命名规则:
>
>  表所对应的实体类的类名+Mapper.xml 
>
> 例如:表t_user，映射的实体类为User，所对应的映射文件为UserMapper.xml 
>
> 因此一个映射文件对应一个实体类，对应一张表的操作 
>
> MyBatis映射文件用于编写SQL，访问以及操作表中的数据 
>
> MyBatis映射文件存放的位置是src/main/resources/mappers目录下
>
>  2、 MyBatis中可以面向接口操作数据，要保证两个一致: 
>
> a）mapper接口的全类名和映射文件的命名空间(namespace)保持一致 
>
> b）mapper接口中方法的方法名和映射文件中编写SQL的标签的id属性保持一致

## 通过junit测试功能