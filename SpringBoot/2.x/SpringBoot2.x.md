# 快速上手SpringBoot

SpringBoot技术由Pivotal团队研发制作，功能的话简单概括就是加速Spring程序的开发，这个加速要从如下两个方面来说

- Spring程序初始搭建过程
- Spring程序的开发过程

下面使用SpringBoot技术快速构建一个SpringMVC的程序，通过这个过程体会<font color="#ff0000"><b>简化</b></font>二字的含义

**步骤①**：创建新模块，选择Spring Initializr，并配置模块相关基础信息

<img src=".\images\配置模块相关基础信息.png" alt="配置模块相关基础信息" style="zoom:80%;" />

​	<font color="#ff0000"><b>特别关注</b></font>：第3步点击Next时，Idea需要联网状态才可以进入到后面那一页，如果不能正常联网，就无法正确到达右面那个设置页了，会一直<font color="#ff0000"><b>联网</b></font>转转转

​	<font color="#ff0000"><b>特别关注</b></font>：第5步选择java版本和你计算机上安装的JDK版本匹配即可，但是最低要求为JDK8或以上版本，推荐使用8或11

**步骤②**：选择当前模块需要使用的技术集

<img src=".\images\选择当前模块需要使用的技术集.png" alt="选择当前模块需要使用的技术集" style="zoom:80%;" />

​	按照要求，左侧选择web，然后在中间选择Spring Web即可，选完右侧就出现了新的内容项，这就表示勾选成功了

​	<font color="#ff0000"><b>关注</b></font>：此处选择的SpringBoot的版本使用默认的就可以了，需要说一点，SpringBoot的版本升级速度很快，可能昨天创建工程的时候默认版本是2.5.4，今天再创建工程默认版本就变成2.5.5了，差别不大，无需过于纠结，回头可以到配置文件中修改对应的版本

**步骤③**：开发控制器类

```java
@RestController
@RequestMapping("/books")
public class BookController {
    @GetMapping
    public String getById(){
        System.out.println("springboot is running...");
        return "springboot is running...";
    }
}
```

<font color="#ff0000"><b>关注</b></font>：做到这里SpringBoot程序的最基础的开发已经做完了，现在就可以正常的运行Spring程序了。可能有些小伙伴会有疑惑，Tomcat服务器没有配置，=-Spring也没有配置，什么都没有配置这就能用吗？这就是SpringBoot技术的强大之处。

**步骤④**：运行自动生成的Application类

<img src=".\images\控制台输出信息.png" alt="控制台输出信息" style="zoom:80%;" />

​	使用带main方法的java程序的运行形式来运行程序，运行完毕后，控制台输出上述信息。

​	不难看出，运行的信息中包含了8080的端口，Tomcat这种熟悉的字样，难道这里启动了Tomcat服务器？是的，这里已经启动了。那服务器没有配置，哪里来的呢？后面再说。现在你就可以通过浏览器访问请求的路径，测试功能是否工作正常了

```
访问路径：	http://localhost:8080/books
```

​	是不是感觉很神奇？目前的效果其实依赖的底层逻辑还是很复杂的，但是从开发者角度来看，目前只有两个文件展现到了开发者面前

- pom.xml

​	这是maven的配置文件，描述了当前工程构建时相应的配置信息

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.5.4</version>
    </parent>

    <groupId>com.itheima</groupId>
    <artifactId>springboot_01_01_quickstart</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

配置中有两个信息需要关注，一个是parent，也就是当前工程继承了另外一个工程，干什么用的后面再说，还有依赖坐标，干什么用的后面再说

- Application类

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

​	这个类功能很简单，就一句代码，前面运行程序就是运行的这个类

​	到这里我们可以大胆推测一下，如果上面这两个文件没有的话，SpringBoot肯定没法玩，看来核心就是这两个文件了。由于是制作第一个SpringBoot程序，先不要关注这两个文件的功能，后面详细讲解内部工作流程。

​	通过上面的制作，我们不难发现，SpringBoot程序简直太好写了，几乎什么都没写，功能就有了，这也是SpringBoot技术为什么现在这么火的原因，和Spirng程序相比，SpringBoot程序在开发的过程中各个层面均具有优势

| **类配置文件**         | **Spring**   | **SpringBoot** |
| ---------------------- | ------------ | -------------- |
| pom文件中的坐标        | **手工添加** | **勾选添加**   |
| web3.0配置类           | **手工制作** | **无**         |
| Spring/SpringMVC配置类 | **手工制作** | **无**         |
| 控制器                 | **手工制作** | **手工制作**   |

​	一句话总结一下就是<font color="#ff0000"><b>能少写就少写</b></font>，<font color="#ff0000"><b>能不写就不写</b></font>，这就是SpringBoot技术给我们带来的好处，行了，现在你就可以动手做一做SpringBoot程序了，看看效果如何，是否真的帮助你简化开发了

# SpringBoot简介

​	入门案例做完了，这个时候回忆一下咱们之前说的SpringBoot的功能是什么还记得吗？加速Spring程序的开发，现在是否深有体会？再来看SpringBoot技术的设计初衷就很容易理解了。

​	SpringBoot是由Pivotal团队提供的全新框架，其设计目的是用来<font color="#ff0000"><b>简化Spring应用的初始搭建以及开发过程</b></font>。

​	都简化了了哪些东西呢？其实就是针对原始的Spring程序制作的两个方面进行了简化：

- Spring程序缺点
  - 依赖设置繁琐
    - 以前写Spring程序，使用的技术都要自己一个一个的写，现在不需要了，如果做过原始SpringMVC程序的小伙伴应该知道，写SpringMVC程序，最基础的spring-web和spring-webmvc这两个坐标是必须的，就这还不包含你用json啊等等这些坐标，现在呢？一个坐标搞定
  - 配置繁琐
    - 以前写配置类或者配置文件，然后用什么东西就要自己写加载bean这些东西，现在呢？什么都没写，照样能用

​	这些简化操作在SpringBoot中有专业的用语，也是SpringBoot程序的核心功能及优点：

- 起步依赖（简化依赖配置）
  - 依赖配置的书写简化就是靠这个起步依赖达成的
- 自动配置（简化常用工程相关配置）
  - 配置过于繁琐，使用自动配置就可以做响应的简化，但是内部还是很复杂的，后面具体展开说
- 辅助功能（内置服务器，……）
  - 除了上面的功能，其实SpringBoot程序还有其他的一些优势，比如我们没有配置Tomcat服务器，但是能正常运行，这是SpringBoot程序的一个可以感知到的功能，也是SpringBoot的辅助功能之一。一个辅助功能都能做的这么6，太牛了

​	下面结合入门程序来说说这些简化操作都在哪些方面进行体现的，一共分为4个方面

- parent
- starter
- 引导类
- 内嵌tomcat

## parent

SpringBoot关注到开发者在进行开发时，往往对依赖版本的选择具有固定的搭配格式，并且这些依赖版本的选择还不能乱搭配。比如A技术的2.0版与B技术的3.5版可以合作在一起，但是和B技术的3.7版合并使用时就有冲突。其实很多开发者都一直想做一件事情，就是将各种各样的技术配合使用的常见依赖版本进行收集整理，制作出了最合理的依赖版本配置方案，这样使用起来就方便多了。

​	SpringBoot一看这种情况so easy啊，于是将所有的技术版本的常见使用方案都给开发者整理了出来，以后开发者使用时直接用它提供的版本方案，就不用担心冲突问题了，相当于SpringBoot做了无数个技术版本搭配的列表，这个技术搭配列表的名字叫做<font color="#ff0000"><b>parent</b></font>。

​	<font color="#ff0000"><b>parent</b></font>自身具有很多个版本，每个<font color="#ff0000"><b>parent</b></font>版本中包含有几百个其他技术的版本号，不同的parent间使用的各种技术的版本号有可能会发生变化。当开发者使用某些技术时，直接使用SpringBoot提供的<font color="#ff0000"><b>parent</b></font>就行了，由<font color="#ff0000"><b>parent</b></font>帮助开发者统一的进行各种技术的版本管理

​	比如你现在要使用Spring配合MyBatis开发，没有parent之前怎么做呢？选个Spring的版本，再选个MyBatis的版本，再把这些技术使用时关联的其他技术的版本逐一确定下来。当你Spring的版本发生变化需要切换时，你的MyBatis版本有可能也要跟着切换，关联技术呢？可能都要切换，而且切换后还可能出现问题。现在这一切工作都可以交给parent来做了。你无需关注这些技术间的版本冲突问题，你只需要关注你用什么技术就行了，冲突问题由<font color="#ff0000"><b>parent</b></font>负责处理。

​	有人可能会提出来，万一<font color="#ff0000"><b>parent</b></font>给我导入了一些我不想使用的依赖怎么办？记清楚，这一点很关键，<font color="#ff0000"><b>parent</b></font>仅仅帮我们进行版本管理，它不负责帮你导入坐标，说白了用什么还是你自己定，只不过版本不需要你管理了。整体上来说，<font color="#ff0000"><b>使用parent可以帮助开发者进行版本的统一管理</b></font>

​	<font color="#ff0000"><b>关注</b></font>：parent定义出来以后，并不是直接使用的，仅仅给了开发者一个说明书，但是并没有实际使用，这个一定要确认清楚

​	那SpringBoot又是如何做到这一点的呢？可以查阅SpringBoot的配置源码，看到这些定义

- 项目中的pom.xml中继承了一个坐标

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.5.4</version>
</parent>
```

- 打开后可以查阅到其中又继承了一个坐标

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-dependencies</artifactId>
    <version>2.5.4</version>
</parent>
```

- 这个坐标中定义了两组信息，第一组是各式各样的依赖版本号属性，下面列出依赖版本属性的局部，可以看的出来，定义了若干个技术的依赖版本号

```xml
<properties>
    <activemq.version>5.16.3</activemq.version>
    <aspectj.version>1.9.7</aspectj.version>
    <assertj.version>3.19.0</assertj.version>
    <commons-codec.version>1.15</commons-codec.version>
    <commons-dbcp2.version>2.8.0</commons-dbcp2.version>
    <commons-lang3.version>3.12.0</commons-lang3.version>
    <commons-pool.version>1.6</commons-pool.version>
    <commons-pool2.version>2.9.0</commons-pool2.version>
    <h2.version>1.4.200</h2.version>
    <hibernate.version>5.4.32.Final</hibernate.version>
    <hibernate-validator.version>6.2.0.Final</hibernate-validator.version>
    <httpclient.version>4.5.13</httpclient.version>
    <jackson-bom.version>2.12.4</jackson-bom.version>
    <javax-jms.version>2.0.1</javax-jms.version>
    <javax-json.version>1.1.4</javax-json.version>
    <javax-websocket.version>1.1</javax-websocket.version>
    <jetty-el.version>9.0.48</jetty-el.version>
    <junit.version>4.13.2</junit.version>
</properties>
```

​	第二组是各式各样的的依赖坐标信息，可以看出依赖坐标定义中没有具体的依赖版本号，而是引用了第一组信息中定义的依赖版本属性值

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-core</artifactId>
            <version>${hibernate.version}</version>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>${junit.version}</version>
        </dependency>
    </dependencies>
</dependencyManagement>
```

<font color="#ff0000"><b>关注</b></font>：上面的依赖坐标定义是出现在<dependencyManagement>标签中的，其实是对引用坐标的依赖管理，并不是实际使用的坐标。因此当你的项目中继承了这组parent信息后，在不使用对应坐标的情况下，前面的这组定义是不会具体导入某个依赖的

## starter

​	SpringBoot关注到开发者在实际开发时，对于依赖坐标的使用往往都有一些固定的组合方式，比如使用spring-webmvc就一定要使用spring-web。每次都要固定搭配着写，非常繁琐，而且格式固定，没有任何技术含量。

​	SpringBoot一看这种情况，看来需要给开发者带来一些帮助了。安排，把所有的技术使用的固定搭配格式都给开发出来，以后你用某个技术，就不用一次写一堆依赖了，还容易写错，我给你做一个东西，代表一堆东西，开发者使用的时候，直接用我做好的这个东西就好了，对于这样的固定技术搭配，SpringBoot给它起了个名字叫做<font color="#ff0000"><b>starter</b></font>。

​	starter定义了使用某种技术时对于依赖的固定搭配格式，也是一种最佳解决方案，<font color="#ff0000"><b>使用starter可以帮助开发者减少依赖配置</b></font>

​	这个东西其实在入门案例里面已经使用过了，入门案例中的web功能就是使用这种方式添加依赖的。可以查阅SpringBoot的配置源码，看到这些定义

- 项目中的pom.xml定义了使用SpringMVC技术，但是并没有写SpringMVC的坐标，而是添加了一个名字中包含starter的依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

- 在spring-boot-starter-web中又定义了若干个具体依赖的坐标

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
        <version>2.5.4</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-json</artifactId>
        <version>2.5.4</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-tomcat</artifactId>
        <version>2.5.4</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-web</artifactId>
        <version>5.3.9</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>5.3.9</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```

​	之前提到过开发SpringMVC程序需要导入spring-webmvc的坐标和spring整合web开发的坐标，就是上面这组坐标中的最后两个了。

​	但是我们发现除了这两个还有其他的，比如第二个，叫做spring-boot-starter-json。看名称就知道，这个是与json有关的坐标了，但是看名字发现和最后两个又不太一样，它的名字中也有starter，打开看看里面有什么？

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
        <version>2.5.4</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-web</artifactId>
        <version>5.3.9</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.12.4</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.datatype</groupId>
        <artifactId>jackson-datatype-jdk8</artifactId>
        <version>2.12.4</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.datatype</groupId>
        <artifactId>jackson-datatype-jsr310</artifactId>
        <version>2.12.4</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.module</groupId>
        <artifactId>jackson-module-parameter-names</artifactId>
        <version>2.12.4</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```

​	我们可以发现，这个starter中又包含了若干个坐标，其实就是使用SpringMVC开发通常都会使用到Json，使用json又离不开这里面定义的这些坐标，看来还真是方便，SpringBoot把我们开发中使用的东西能用到的都给提前做好了。你仔细看完会发现，里面有一些你没用过的。的确会出现这种过量导入的可能性，没关系，可以通过maven中的排除依赖剔除掉一部分。不过你不管它也没事，大不了就是过量导入呗。

​	到这里基本上得到了一个信息，使用starter可以帮开发者快速配置依赖关系。以前写依赖3个坐标的，现在写导入一个就搞定了，就是加速依赖配置的。

**starter与parent的区别**

​	朦朦胧胧中感觉starter与parent好像都是帮助我们简化配置的，但是功能又不一样，梳理一下。

​	<font color="#ff0000"><b>starter</b></font>是一个坐标中定了若干个坐标，以前写多个的，现在写一个，<font color="#ff0000"><b>是用来减少依赖配置的书写量的</b></font>

​	<font color="#ff0000"><b>parent</b></font>是定义了几百个依赖版本号，以前写依赖需要自己手工控制版本，现在由SpringBoot统一管理，这样就不存在版本冲突了，<font color="#ff0000"><b>是用来减少依赖冲突的</b></font>

**实际开发应用方式**

- 实际开发中如果需要用什么技术，先去找有没有这个技术对应的starter

  - 如果有对应的starter，直接写starter，而且无需指定版本，版本由parent提供
  - 如果没有对应的starter，手写坐标即可

- 实际开发中如果发现坐标出现了冲突现象，确认你要使用的可行的版本号，使用手工书写的方式添加对应依赖，覆盖SpringBoot提供给我们的配置管理

  - 方式一：直接写坐标
  - 方式二：覆盖<properties>中定义的版本号，就是下面这堆东西了，哪个冲突了覆盖哪个就OK了

```xml
<properties>
    <activemq.version>5.16.3</activemq.version>
    <aspectj.version>1.9.7</aspectj.version>
    <assertj.version>3.19.0</assertj.version>
    <commons-codec.version>1.15</commons-codec.version>
    <commons-dbcp2.version>2.8.0</commons-dbcp2.version>
    <commons-lang3.version>3.12.0</commons-lang3.version>
    <commons-pool.version>1.6</commons-pool.version>
    <commons-pool2.version>2.9.0</commons-pool2.version>
    <h2.version>1.4.200</h2.version>
    <hibernate.version>5.4.32.Final</hibernate.version>
    <hibernate-validator.version>6.2.0.Final</hibernate-validator.version>
    <httpclient.version>4.5.13</httpclient.version>
    <jackson-bom.version>2.12.4</jackson-bom.version>
    <javax-jms.version>2.0.1</javax-jms.version>
    <javax-json.version>1.1.4</javax-json.version>
    <javax-websocket.version>1.1</javax-websocket.version>
    <jetty-el.version>9.0.48</jetty-el.version>
    <junit.version>4.13.2</junit.version>
</properties>
```

<font color="#f0f"><b>温馨提示</b></font>

​	SpringBoot官方给出了好多个starter的定义，方便我们使用，而且名称都是如下格式

```
命名规则：spring-boot-starter-技术名称
```

​	所以以后见了spring-boot-starter-aaa这样的名字，这就是SpringBoot官方给出的starter定义。那非官方定义的也有吗？有的，具体命名方式到整合章节再说

## 引导类

​	配置说完了，我们发现SpringBoot确实帮助我们减少了很多配置工作，下面说一下程序是如何运行的。目前程序运行的入口就是SpringBoot工程创建时自带的那个类了，带有main方法的那个类，运行这个类就可以启动SpringBoot工程的运行

```java
@SpringBootApplication
public class Springboot0101QuickstartApplication {
    public static void main(String[] args) {
        SpringApplication.run(Springboot0101QuickstartApplication.class, args);
    }
}
```

​	SpringBoot本身是为了加速Spring程序的开发的，而Spring程序运行的基础是需要创建自己的Spring容器对象（IoC容器）并将所有的对象交给Spring的容器管理，也就是一个一个的Bean。那还了SpringBoot加速开发Spring程序，这个容器还在吗？这个疑问不用说，一定在。当前这个类运行后就会产生一个Spring容器对象，并且可以将这个对象保存起来，通过容器对象直接操作Bean。

```java
@SpringBootApplication
public class Springboot0101QuickstartApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Springboot0101QuickstartApplication.class, args);
        BookController bean = ctx.getBean(BookController.class);
        System.out.println("bean======>" + bean);
    }
}
```

​	通过上述操作不难看出，其实SpringBoot程序启动还是创建了一个Spring容器对象。这个类在SpringBoot程序中是所有功能的入口，称这个类为<font color="#ff0000"><b>引导类</b></font>。

​	作为一个引导类最典型的特征就是当前类上方声明了一个注解<font color="#ff0000"><b>@SpringBootApplication</b></font>

## 内嵌tomcat

​	当前我们做的SpringBoot入门案例勾选了Spirng-web的功能，并且导入了对应的starter。

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

​	SpringBoot发现，既然你要做web程序，肯定离不开使用web服务器，这样吧，帮人帮到底，送佛送到西。我帮你搞一个web服务器，你要愿意用的，直接使用就好了，干脆我再多给你几种选择，你随便切换。万一你不想用我给你提供的，也行，你可以自己搞。

​	由于这个功能不属于程序的主体功能，可用可不用，于是乎SpringBoot将其定位成辅助功能，别小看这么一个辅助功能，它可是帮我们开发者又减少了好多的设置性工作。

​	下面就围绕着这个内置的web服务器，也可以说是内置的tomcat服务器来研究几个问题

1. 这个服务器在什么位置定义的
2. 这个服务器是怎么运行的
3. 这个服务器如果想换怎么换？虽然这个需求很垃圾，搞得开发者会好多web服务器一样，用别人提供好的不香么？非要自己折腾

**内嵌Tomcat定义位置**

​	说到定义的位置，我们就想，如果我们不开发web程序，用的着web服务器吗？肯定用不着啊。那如果这个东西被加入到你的程序中，伴随着什么技术进来的呢？肯定是web相关的功能啊，没错，就是前面导入的web相关的starter做的这件事。

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

​	打开查看web的starter导入了哪些东西

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
        <version>2.5.4</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-json</artifactId>
        <version>2.5.4</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-tomcat</artifactId>
        <version>2.5.4</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-web</artifactId>
        <version>5.3.9</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>5.3.9</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```

​	第三个依赖就是这个tomcat对应的东西了，居然也是一个starter，再打开看看

```xml
<dependencies>
    <dependency>
        <groupId>jakarta.annotation</groupId>
        <artifactId>jakarta.annotation-api</artifactId>
        <version>1.3.5</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>org.apache.tomcat.embed</groupId>
        <artifactId>tomcat-embed-core</artifactId>
        <version>9.0.52</version>
        <scope>compile</scope>
        <exclusions>
            <exclusion>
                <artifactId>tomcat-annotations-api</artifactId>
                <groupId>org.apache.tomcat</groupId>
            </exclusion>
        </exclusions>
    </dependency>
    <dependency>
        <groupId>org.apache.tomcat.embed</groupId>
        <artifactId>tomcat-embed-el</artifactId>
        <version>9.0.52</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>org.apache.tomcat.embed</groupId>
        <artifactId>tomcat-embed-websocket</artifactId>
        <version>9.0.52</version>
        <scope>compile</scope>
        <exclusions>
            <exclusion>
                <artifactId>tomcat-annotations-api</artifactId>
                <groupId>org.apache.tomcat</groupId>
            </exclusion>
        </exclusions>
    </dependency>
</dependencies>
```

这里面有一个核心的坐标，tomcat-embed-core，叫做tomcat内嵌核心。就是这个东西把tomcat功能引入到了我们的程序中。目前解决了第一个问题，找到根儿了，谁把tomcat引入到程序中的？spring-boot-starter-web中的spring-boot-starter-tomcat做的。之所以你感觉很奇妙的原因就是，这个东西是默认加入到程序中了，所以感觉很神奇，居然什么都不做，就有了web服务器对应的功能，再来说第二个问题，这个服务器是怎么运行的

**内嵌Tomcat运行原理**

​	Tomcat服务器是一款软件，而且是一款使用java语言开发的软件，熟悉的小伙伴可能有印象，tomcat安装目录中保存有jar，好多个jar。

​	下面的问题来了，既然是使用java语言开发的，运行的时候肯定符合java程序运行的原理，java程序运行靠的是什么？对象呀，一切皆对象，万物皆对象。那tomcat运行起来呢？也是对象。

​	如果是对象，那Spring容器是用来管理对象的，这个对象能不能交给Spring容器管理呢？把吗去掉，是个对象都可以交给Spring容器管理，行了，这下通了。tomcat服务器运行其实是以对象的形式在Spring容器中运行的，怪不得我们没有安装这个tomcat，而且还能用。闹了白天这东西最后是以一个对象的形式存在，保存在Spring容器中悄悄运行的。具体运行的是什么呢？其实就是上前面提到的那个tomcat内嵌核心

```xml
<dependencies>
    <dependency>
        <groupId>org.apache.tomcat.embed</groupId>
        <artifactId>tomcat-embed-core</artifactId>
        <version>9.0.52</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```

​	那既然是个对象，如果把这个对象从Spring容器中去掉是不是就没有web服务器的功能呢？是这样的，通过依赖排除可以去掉这个web服务器功能

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <exclusions>
            <exclusion>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-tomcat</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
</dependencies>
```

​	上面对web-starter做了一个操作，使用maven的排除依赖去掉了使用tomcat的starter。这下好了，容器中肯定没有这个对象了，重新启动程序可以观察到程序运行了，但是并没有像之前那样运行后会等着用户发请求，而是直接停掉了，就是这个原因了。

**更换内嵌Tomcat**

​	那根据上面的操作我们思考是否可以换个服务器呢？必须的嘛。根据SpringBoot的工作机制，用什么技术，加入什么依赖就行了。SpringBoot提供了3款内置的服务器

- tomcat(默认)：apache出品，粉丝多，应用面广，负载了若干较重的组件

- jetty：更轻量级，负载性能远不及tomcat

- undertow：负载性能勉强跑赢tomcat

  想用哪个，加个坐标就OK。前提是把tomcat排除掉，因为tomcat是默认加载的。

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <exclusions>
            <exclusion>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-tomcat</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jetty</artifactId>
    </dependency>
</dependencies>
```

​	现在就已经成功替换了web服务器，核心思想就是用什么加入对应坐标就可以了。如果有starter，优先使用starter。

# SpringBoot基础配置

入门案例做完了，下面就要研究SpringBoot的用法了。通过入门案例，各位小伙伴能够感知到一个信息，SpringBoot没有具体的功能，它在辅助加快Spring程序的开发效率。我们发现现在几乎不用做任何的配置，功能就有了，确实很好用。但是仔细想想，没有做配置意味着什么？意味着配置已经做好了，不用你自己写了。但是新的问题又来了，如果不想用已经写好的默认配置，该如何干预呢？这就是这一章咱们要研究的问题。

​	如果我们想修改默认的配置，这个信息应该写在什么位置呢？目前我们接触的入门案例中一共有3个文件，第一是pom.xml文件，设置项目的依赖的，这个没什么好研究的，相关的高级内容咱们到原理篇再说，第二是引导类，这个是执行SpringBoot程序的入口，也不像是做配置的地方，其实还有一个信息，就是在resources目录下面有一个空白的文件，叫做application.properties。一看就是个配置文件，咱们这一章就来说说配置文件怎么写，能写什么，怎么干预SpringBoot的默认配置，修改成自己的配置。

## 属性配置

​	SpringBoot通过配置文件application.properties就可以修改默认的配置，那咱们就先找个简单的配置下手，当前访问tomcat的默认端口是8080，好熟悉的味道，但是不便于书写，我们先改成80，通过这个操作来熟悉一下SpringBoot的配置格式是什么样的

<img src=".\images\修改访问端口为80端口.png" alt="修改访问端口为80端口" style="zoom:80%;" />

​	那该如何写呢？properties格式的文件书写规范是key=value

```properties
name=xxx
```

​	这个格式肯定是不能颠覆的，那就尝试性的写就行了，改端口，写port。当你输入port后，神奇的事情就发生了，这玩意儿带提示，太好了

<img src=".\images\properties文件内容提示.png" alt="properties文件内容提示" style="zoom:80%;" />

​	根据提示敲回车，输入80端口，搞定

```properties
server.port=80
```

​	下面就可以直接运行程序，测试效果了。

​	其实到这里我们应该得到如下三个信息

1. SpringBoot程序可以在application.properties文件中进行属性配置
2. application.properties文件中只要输入要配置的属性关键字就可以根据提示进行设置
3. SpringBoot将配置信息集中在一个文件中写，不管你是服务器的配置，还是数据库的配置，总之都写在一起，逃离一个项目十几种配置文件格式的尴尬局面

​	做完了端口的配置，趁热打铁，再做几个配置，目前项目启动时会显示一些日志信息，就来改一改这里面的一些设置。

**关闭运行日志图表（banner)**

```properties
spring.main.banner-mode=off
```

**设置运行日志的显示级别**

```properties
logging.level.root=debug
```

​	你会发现，现在这么搞配置太爽了，以前你做配置怎么做？不同的技术有自己专用的配置文件，文件不同格式也不统一，现在呢？不用东奔西走的找配置文件写配置了，统一格式了。

​	我们现在配置了3个信息，但是又有新的问题了。这个配置是随便写的吗？什么都能配？有没有一个东西显示所有能配置的项呢？此外这个配置和什么东西有关呢？会不会因为我写了什么东西以后才可以写什么配置呢？比如我现在没有写数据库相关的东西，能否配置数据呢？一个一个来，先说第一个问题，都能配置什么。

​	打开SpringBoot的官网，找到SpringBoot官方文档，打开查看附录中的Application Properties就可以获取到对应的配置项了，网址奉上：https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html#application-properties

​	能写什么的问题解决了，再来说第二个问题，这个配置项和什么有关。在pom中注释掉导入的spring-boot-starter-web，然后刷新工程，你会发现配置的提示消失了。闹了半天是设定使用了什么技术才能做什么配置。也合理，不然配置的东西都没有使用对应技术，配了也是白配。

<font color="#f0f"><b>温馨提示</b></font>

​	所有的starter中都会依赖下面这个starter，叫做spring-boot-starter。这个starter是所有的SpringBoot的starter的基础依赖，里面定义了SpringBoot相关的基础配置，关于这个starter我们到开发应用篇和原理篇中再深入讲解。

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <version>2.5.4</version>
    <scope>compile</scope>
</dependency>
```

