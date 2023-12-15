# Spring Security简介

Spring Security是一个功能强大且高度可定制的身份验证和访问控制框架。Spring Security致力于为Java应用程序提供身份验证和授权的能力。像所有Spring项目一样，Spring Security的真正强大之处在于它可以轻松扩展以满足定制需求的能力。

![角色和权限时序](./images/角色和权限时序.png)

Spring Security两大重要核心功能：**用户认证（Authentication）**和**用户授权（Authorization）**。

- 用户认证：验证某个用户是否为系统中的合法主体，也就是说用户能否访问该系统。用户认证一般要求用户提供用户名和密码。系统通过校验用户名和密码来完成认证过程。
- 用户授权：验证某个用户是否有权限执行某个操作。在一个系统中，不同用户所有的权限是不同的。比如对一个文件来说，有的用户只能进行读取，有的用户既能读取，又能修改。一般来说，系统会为不同的用户分配不同的角色，而每个角色则对应一系列的权限。

# 快速开始

使用Springboot工程搭建Spring Security项目。

## 引入依赖

在pom中新增了Spring Security的依赖

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

## 创建测试访问接口

用于访问接口时触发Spring Security登陆页面

```java
@RestController
public class SecurityController {
  
    @RequestMapping("/hello")
    public String hello() {
        return "hello security";
    }
  
}
```

## 访问接口，自动跳转至Security登陆页面

访问hello接口，将自动跳转至Security的登陆页面

![login](./images/login.png)

默认账号是： user

默认密码是：启动项目的控制台中输出的密码

![自动生成的密码](./images/自动生成的密码.png)

# Spring Security基础概念

在上一节中访问add接口，发现被Spring Security的登陆页面拦截，可以猜到这是触发了Security框架的过滤器。Spring Security本质上就是一个过滤器链。下面将介绍Security框架的过滤器链。

## 过滤器链

- WebAsyncManagerIntegrationFilter：将SecurityContext集成到Spring MVC中用于管理异步请求处理的WebAsyncManager中。
- SecurityContextPersistenceFilter：在当前会话中填充SecurityContext，SecurityContext即Security的上下文对象，里面包含了当前用户的认证及权限信息等。
- HeaderWriterFilter：向请求的Header中添加信息
- CsrfFilter：用于防止CSRF（跨域请求伪造）攻击。Spring Security会对所有post请求验证是否包含系统生成的CSRF的信息，如果不包含则报错。
- LogoutFilter：匹配URL为“/logout”的请求，清除认证信息，实现用户注销功能。
- UsernamePasswordAuthenticationFilter：认证操作的过滤器，用于匹配URL为“/login”的POST请求做拦截，校验表单中的用户名和密码。
- DefaultLoginPageGeneratingFilter：如果没有配置登陆页面，则生成默认的认证页面
- DefaultLogoutPageGeneratingFilter：用于生成默认的退出页面
- BasicAuthenticationFilter：用于Http基本认证，自动解析Http请求头中名为Authentication的内容，并获得内容中“basic”开头之后的信息。
- RequestCacheAwareFilter：用于缓存HttpServletRequest
- SecurityContextHolderAwareRequestFilter：用于封装ServletRequest，让ServletRequest具备更多功能。
- AnonymousAuthenticationFilter：对于未登录情况下的处理，当SecurityContextHolder中认证信息为空，则会创建一个匿名用户存入到SecurityContextHolder中
- SessionManagementFilter：限制同一用户开启多个会话
- ExceptionTranslationFilter：异常过滤器，用来处理在认证授权过程中抛出异常。
- FilterSecurityInterceptor：获取授权信息，根据SecurityContextHolder中存储的用户信息判断用户是否有权限访问

## 过滤器加载过程

Springboot在整合Spring Security项目时会自动配置**DelegatingFilterProxy**过滤器，若非Springboot工程，则需要手动配置该过滤器。

![springsecurity过滤器链](./images/springsecurity过滤器链.png)

过滤器如何进行加载的？

结合上图和源码，Security在**DelegatingFilterProxy**的doFilter()调用了initDelegat()方法，在该方法中调用了WebApplicationContext的getBean()方法，该方法出发FilterChainProxy的doFilterInternal方法，用于获取过滤链中的所有过滤器并进行加载。

# Spring Security的认证方式

## 认证概念

所谓的认证，就是用来判断系统中是否存在某用户，并判断该用户的身份是否合法的过程，解决的其实是用户登录的问题。认证的存在，是为了保护系统中的隐私数据与资源，只有合法的用户才可以访问系统中的资源。

## 认证方式

在Spring Security中，常见的认证方式可以分为HTTP层面和表单层面，常见的认证方式如下:

- HTTP基本认证

- Form表单认证

- HTTP摘要认证

# 基本认证

HTTP基本认证是在RFC2616标准中定义的一种认证模式，它以一种很简单的方式与用户进行交互。HTTP基本认证可以分为如下4个步骤：

- 客户端首先发起一个未携带认证信息的请求；

- 然后服务器端返回一个401 Unauthorized的响应信息，并在WWW-Authentication头部中说明认证形式：当进行HTTP基本认证时，WWW-Authentication会被设置为Basic realm=“被保护的页面”；

- 接下来客户端会收到这个401 Unauthorized响应信息，并弹出一个对话框，询问用户名和密码。当用户输入后，客户端会将用户名和密码使用冒号进行拼接并用Base64编码，然后将其放入到请求的Authorization头部并发送给服务器；

- 最后服务器端对客户端发来的信息进行解码得到用户名和密码，并对该信息进行校验判断是否正确，最终给客户端返回响应内容。

HTTP基本认证是一种无状态的认证方式，与表单认证相比，HTTP基本认证是一种基于HTTP层面的认证方式，无法携带Session信息，也就无法实现Remember-Me功能。另外，用户名和密码在传递时仅做了一次简单的Base64编码，几乎等同于以明文传输，极易被进行密码窃听和重放攻击。所以在实际开发中，很少会使用这种认证方式来进行安全校验。

基本认证的代码实现：

- 创建SecurityConfig配置类

```java
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        //1.配置基本认证方式
        httpSecurity.authorizeHttpRequests()
                .anyRequest()
                .authenticated()
                .and()
                // 开启basic认证
                .httpBasic();
        return httpSecurity.build();
    }

}
```

- Basic认证详解

在未登录状态下访问目标资源时，查看响应头，可以看到WWW-Authenticate认证信息：WWW-Authenticate：Basic realm="Realm"，其中WWW-Authenticate: 表示服务器告知浏览器进行代理认证工作。Basic: 表示认证类型为Basic认证。realm="Realm": 表示认证域名为Realm域。

根据401和以上响应头信息，浏览器会弹出一个对话框，要求输入 用户名/密码，Basic认证会将其拼接成 “用户名:密码” 格式，中间是一个冒号，并利用Base64编码成加密字符串xxx；然后在请求头中附加 Authorization: Basic xxx 信息，发送给后台认证；后台需要利用Base64来进行解码xxx，得到用户名和密码，再校验 用户名:密码 信息。如果认证错误，浏览器会保持弹框；如果认证成功，浏览器会缓存有效的Base64编码，在之后的请求中，浏览器都会在请求头中添加该有效编码。

# Form表单认证

在SpringBoot开发环境中，只要我们添加了Spring Security的依赖包，就会自动实现表单认证。

```java
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
      	// 表单认证
        httpSecurity.authorizeHttpRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin();
        return httpSecurity.build();
    }

}
```

## 表单认证中的预置url和页面

默认的formLogin配置中，自动配置了一些url和页面:

- **/login(get)**: get请求时会跳转到这个页面，只要我们访问任意一个需要认证的请求时，都会跳转到这个登录界面。
- **/login(post)**: post请求时会触发这个接口，在登录页面点击登录时，默认的登录页面表单中的action就是关联这个login接口。
- **/login?error**: 当用户名或密码错误时，会跳转到该页面。
- **/:** 登录成功后，默认跳转到该页面，如果配置了index.html页面，则 ”/“ 会重定向到index.html页面，当然这个页面要由我们自己实现。
- **/logout:** 注销页面。
- **/login?logout:** 注销成功后跳转到的页面。

由此可见，SpringSecurity默认有两个login，即登录页面和登录接口的地址都是 /login:

- GET http://localhost:8080/login
- POST http://localhost:8080/login

如果是 GET 请求，表示你想访问登录页面；如果是 POST 请求，表示你想提交登录数据。
对于这几个URL接口，我们简单了解即可。

## 自定义认证页面

- 自定义登陆页面

- 自定义首页
- 自定义错误页面

![自定义认证页面](./images/自定义认证页面.png)

## 自定义配置项

```java
package com.example.springsecuritybasic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // 表单认证
        httpSecurity.authorizeHttpRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login.html")
                .permitAll()
                // 当登陆成功后，是否指定跳转到首页
                .defaultSuccessUrl("/index.html", true)
                // post请求的登陆接口
                .loginProcessingUrl("/login")
                // 登陆失败，用户名或密码错误
                .failureUrl("/error.html")
                .and()
                // 注销接口
                .logout().logoutUrl("/logout")
                // 注销成功后跳转的页面
                .logoutSuccessUrl("/login.html").permitAll()
                .and()
                // 需要关闭csrf防护功能，否则登陆不成功
                .csrf().disable();
        return httpSecurity.build();
    }

}
```

# 自定义用户名和密码

## 在配置文件中定义

使用application.yml

```yaml
spring:
  security:
    user:
      name: admin
      # 明文为：123456
      password: $2a$10$BusrXu8MiE3QpCn5hoUkr.6Ib2cpe6dXesL9CXOA/A2aPDJVw1bPG
```

还需要向IOC容器里注入一个PasswordEncoder，用于生成密码的base64编码的字符串，和解析base64编码的字符串为实际密码内容。

```java
@Configuration
public class SecurityConfig {

    //.....
  
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

## 在内存中定义

将用户名和密码写在配置类里，虽然配置类中可以自己编写用户名和密码的代码，但因为它是配置类的缘故，不适合将从数据库中获取用户名和密码的业务代码写入到配置类中。

```java
@Configuration
public class SecurityConfig {

    //...

    // 基于内存的验证
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        UserDetails user = User.builder()
                .passwordEncoder(passwordEncoder()::encode)
                .username("admin")
                .password("123456")
                .roles("role1")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

## 在数据库中定义

- 设计数据库表

```sql
CREATE TABLE `sys_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO security_study.sys_user (username,password) VALUES
	 ('admin','$2a$10$BusrXu8MiE3QpCn5hoUkr.6Ib2cpe6dXesL9CXOA/A2aPDJVw1bPG');
```

- 编写UserDetailService实现类

```java
@Service
public class MyUserDetailService implements UserDetailsService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(username), SysUser::getUsername, username);
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        User user = null;
        if (Objects.nonNull(sysUser)) {
            // 创建一个角色集合
            List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("user");
            user = new User(username, sysUser.getPassword(), authorities);
        }
        return user;
    }
}
```

- 编写SecurityConfig配置类，指明对UserDetailsService实现类认证

```java
@Configuration
public class SecurityConfig {

    @Resource
    private UserDetailsService userDetailsService;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // 指定认证管理器，通过UserDetailsService查询用户信息
        AuthenticationManagerBuilder managerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        managerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        AuthenticationManager authenticationManager = managerBuilder.build();

        // 表单认证
        httpSecurity.authorizeHttpRequests()
                .anyRequest()
                .authenticated()
                .and()
                .authenticationManager(authenticationManager)
                .formLogin()
                .loginPage("/login.html")
                .permitAll()
                // 当登陆成功后，是否指定跳转到首页
                .defaultSuccessUrl("/index.html", true)
                // post请求的登陆接口
                .loginProcessingUrl("/login")
                // 登陆失败，用户名或密码错误
                .failureUrl("/error.html")
                .and()
                // 注销接口
                .logout().logoutUrl("/logout")
                // 注销成功后跳转的页面
                .logoutSuccessUrl("/login.html").permitAll()
                .and()
                // 需要关闭csrf防护功能，否则登陆不成功
                .csrf().disable();

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

# 角色和权限

## 角色和权限的概念

所谓权限，就是用户是否有访问当前页面，或者是执行某个操作的权利。

所谓角色，是对权限的汇总，比如“管理员”角色，可以对数据进行增删改查，增删改查是数据的四个权限，拥有“管理员”角色的用户拥有这四个权限。“普通用户”角色，只具备数据的增和查两种权限，那么拥有“普通用户”角色的用户只拥有这两个权限。

Spring Security提供了四个方法用于角色和权限的访问控制。通过这些方法，对用户是否具有某个或某些权限，进行过滤访问。对用户是否具备某个或某些角色，进行过滤访问。

- hasAuthority
- hasAnyAuthority
- hasRole
- hasAnyRole

## hasAuthority方法

判断当前主体是否有指定的权限，有返回true，否则返回false，该方法适用于只拥有一个权限的用户。

- 在配置类中设置当前主体具有怎样的权限才能访问。

```java
@Configuration
public class SecurityConfig {

    @Resource
    private UserDetailsService userDetailsService;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // 指定认证管理器，通过UserDetailsService查询用户信息
        AuthenticationManagerBuilder managerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        managerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        AuthenticationManager authenticationManager = managerBuilder.build();

        // 没有权限跳转到no-permission.html页面
        httpSecurity.exceptionHandling().accessDeniedPage("/no-permission.html");
        httpSecurity
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/users.html").hasAuthority("8") //hasAuthority方法：当前登陆用户，只有具有8权限才可以访问这个路径
                        .anyRequest().authenticated())
                .authenticationManager(authenticationManager)
                .formLogin()
                .loginPage("/login.html").permitAll() // 设置自定义登陆页面
                .loginProcessingUrl("/login") // 登陆时访问的路径
                .failureUrl("/error.html") // 登陆失败的页面
                .defaultSuccessUrl("/index.html") // 登陆成功后跳转的路径
                .and()
                .logout().logoutUrl("/logout") // 注销接口
                .logoutSuccessUrl("/login.html") // 注销成功后跳转的页面
                .and()
                .csrf().disable(); // 需要关闭csrf防护功能，否则登陆不成功

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

- 从数据库查询用户的所有权限

```java
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 查询指定用户下的所有权限集合
     * @param userId
     * @return
     */
    @Select("SELECT permission_id FROM sys_role_permission WHERE role_id in (SELECT role_id FROM sys_user_role WHERE user_id = #{userId})")
    Set<Integer> getAllPermissions(Integer userId);

}
```

- 在userdetailsService，为返回的User对象设置权限

```java
@Service
public class MyUserDetailService implements UserDetailsService {
    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(username), SysUser::getUsername, username);
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        User user = null;
        if (Objects.nonNull(sysUser)) {
            // 获取用户的所有权限
            Set<Integer> permissions = sysUserMapper.getAllPermissions(sysUser.getId());
            List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(permissions.stream().map(String::valueOf).collect(Collectors.joining(",")));
            user = new User(username, sysUser.getPassword(), authorities);
        }
        return user;
    }
}
```

- SQL语句

```sql
-- 权限表
CREATE TABLE `sys_permission` (
  `id` int NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限名称',
  `parent_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 角色表
CREATE TABLE `sys_role` (
  `id` int NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 用户角色表
CREATE TABLE `sys_user_role` (
  `user_id` int NOT NULL,
  `role_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 角色权限表
CREATE TABLE `sys_role_permission` (
  `role_id` int DEFAULT NULL,
  `permission_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


INSERT INTO sys_permission (id,name,parent_id) VALUES
	 (1,'用户管理',0),
	 (2,'查询',1),
	 (3,'新增',1),
	 (4,'修改',1),
	 (5,'删除',1),
	 (6,'视频管理',0),
	 (7,'查询',6),
	 (8,'新增',6);	 

INSERT INTO sys_role (id,name) VALUES
	 (1,'管理员'),
	 (2,'运营');
	 
INSERT INTO sys_user_role (user_id,role_id) VALUES
	 (1,1),
	 (2,1),
	 (2,2);

INSERT INTO sys_role_permission (role_id,permission_id) VALUES
	 (1,1),
	 (1,2),
	 (1,3),
	 (1,4),
	 (1,5),
	 (2,6),
	 (2,7),
	 (2,8);
```

## hasAnyAuthority方法

适用于一个主体有多个权限的情况

```java
@Configuration
public class SecurityConfig {

    @Resource
    private UserDetailsService userDetailsService;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // 指定认证管理器，通过UserDetailsService查询用户信息
        AuthenticationManagerBuilder managerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        managerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        AuthenticationManager authenticationManager = managerBuilder.build();

        // 没有权限跳转到no-permission.html页面
        httpSecurity.exceptionHandling().accessDeniedPage("/no-permission.html");
        httpSecurity
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/users.html").hasAnyAuthority("2","9") // 当前登录用户，只要有“2”或“9”任意一个权限即可
                        .anyRequest().authenticated())
                .authenticationManager(authenticationManager)
                .formLogin()
                .loginPage("/login.html").permitAll() // 设置自定义登陆页面
                .loginProcessingUrl("/login") // 登陆时访问的路径
                .failureUrl("/error.html") // 登陆失败的页面
                .defaultSuccessUrl("/index.html") // 登陆成功后跳转的路径
                .and()
                .logout().logoutUrl("/logout") // 注销接口
                .logoutSuccessUrl("/login.html") // 注销成功后跳转的页面
                .and()
                .csrf().disable(); // 需要关闭csrf防护功能，否则登陆不成功

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

## hasRole方法

如果用户具备给定角色就允许访问

- SecurityConfig

```java
@Configuration
public class SecurityConfig {

    @Resource
    private UserDetailsService userDetailsService;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // 指定认证管理器，通过UserDetailsService查询用户信息
        AuthenticationManagerBuilder managerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        managerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        AuthenticationManager authenticationManager = managerBuilder.build();

        // 没有权限跳转到no-permission.html页面
        httpSecurity.exceptionHandling().accessDeniedPage("/no-permission.html");
        httpSecurity
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/users.html").hasRole("3") // 当前登陆用户具备给定角色就允许访问
                        .anyRequest().authenticated())
                .authenticationManager(authenticationManager)
                .formLogin()
                .loginPage("/login.html").permitAll() // 设置自定义登陆页面
                .loginProcessingUrl("/login") // 登陆时访问的路径
                .failureUrl("/error.html") // 登陆失败的页面
                .defaultSuccessUrl("/index.html") // 登陆成功后跳转的路径
                .and()
                .logout().logoutUrl("/logout") // 注销接口
                .logoutSuccessUrl("/login.html") // 注销成功后跳转的页面
                .and()
                .csrf().disable(); // 需要关闭csrf防护功能，否则登陆不成功

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

- SysUserMapper

```java
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 查询指定用户下的所有权限集合
     * @param userId
     * @return
     */
    @Select("SELECT permission_id FROM sys_role_permission WHERE role_id in (SELECT role_id FROM sys_user_role WHERE user_id = #{userId})")
    Set<Integer> getAllPermissions(Integer userId);

    /**
     * 查询指定用户的所有角色集合
     * @param userId
     * @return
     */
    @Select("SELECT role_id FROM sys_user_role WHERE user_id = #{userId}")
    Set<Integer> getAllRoles(Integer userId);
}
```

- MyUserDetailService

```java
@Service
@Log4j2
public class MyUserDetailService implements UserDetailsService {
    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(username), SysUser::getUsername, username);
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        User user = null;
        if (Objects.nonNull(sysUser)) {
            // 获取用户的所有权限
            Set<Integer> permissions = sysUserMapper.getAllPermissions(sysUser.getId());
            String permissionText = permissions.stream().map(String::valueOf).collect(Collectors.joining(","));
            log.info("permissionText:{}", permissionText);
            // 获取用户的所有角色
            Set<Integer> roles = sysUserMapper.getAllRoles(sysUser.getId());
            String roleText = roles.stream().map(num -> "ROLE_" + num).collect(Collectors.joining(","));
            log.info("roleText:{}", roleText);
            List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(permissionText + "," + roleText);
            user = new User(username, sysUser.getPassword(), authorities);
        }
        return user;
    }
}
```

## hasAnyRole方法

设置多个角色，只要用户具有某一个角色，就能访问。

```java
@Configuration
public class SecurityConfig {

    @Resource
    private UserDetailsService userDetailsService;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // 指定认证管理器，通过UserDetailsService查询用户信息
        AuthenticationManagerBuilder managerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        managerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        AuthenticationManager authenticationManager = managerBuilder.build();

        // 没有权限跳转到no-permission.html页面
        httpSecurity.exceptionHandling().accessDeniedPage("/no-permission.html");
        httpSecurity
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/users.html").hasAnyRole("1", "3") // 只要当前登录用户具有某一个角色，就能访问。
                        .anyRequest().authenticated())
                .authenticationManager(authenticationManager)
                .formLogin()
                .loginPage("/login.html").permitAll() // 设置自定义登陆页面
                .loginProcessingUrl("/login") // 登陆时访问的路径
                .failureUrl("/error.html") // 登陆失败的页面
                .defaultSuccessUrl("/index.html") // 登陆成功后跳转的路径
                .and()
                .logout().logoutUrl("/logout") // 注销接口
                .logoutSuccessUrl("/login.html") // 注销成功后跳转的页面
                .and()
                .csrf().disable(); // 需要关闭csrf防护功能，否则登陆不成功

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

# 自动登录

## 准备数据库表

创建persistent_logins表，用于持久化自动登陆的信息。

```sql
CREATE TABLE `persistent_logins` (
  `username` varchar(64) COLLATE utf8mb4_general_ci NOT NULL,
  `series` varchar(64) COLLATE utf8mb4_general_ci NOT NULL,
  `token` varchar(64) COLLATE utf8mb4_general_ci NOT NULL,
  `last_used` timestamp NOT NULL,
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
```

## 实现自动登陆

```java
@Configuration
public class SecurityConfig {

    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private DataSource dataSource;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        // 指定认证管理器，通过UserDetailsService查询用户信息
        AuthenticationManagerBuilder managerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        managerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        AuthenticationManager authenticationManager = managerBuilder.build();

        // 没有权限跳转到no-permission.html页面
        httpSecurity.exceptionHandling().accessDeniedPage("/no-permission.html");
        httpSecurity
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .anyRequest().authenticated())
                .authenticationManager(authenticationManager)
                .formLogin()
                .loginPage("/login.html").permitAll() // 设置自定义登陆页面
                .loginProcessingUrl("/login") // 登陆时访问的路径
                .failureUrl("/error.html") // 登陆失败的页面
                .defaultSuccessUrl("/index.html") // 登陆成功后跳转的路径
                .and()
                .rememberMe() // 开启记住我的功能
                .tokenRepository(jdbcTokenRepository()) // 持久化令牌的方案
                .tokenValiditySeconds(60*60*24*7) // 设置令牌的有效期，以秒为单位
                .and()
                .logout().logoutUrl("/logout") // 注销接口
                .logoutSuccessUrl("/login.html") // 注销成功后跳转的页面
                .and()
                .csrf().disable(); // 需要关闭csrf防护功能，否则登陆不成功

        return httpSecurity.build();
    }

    @Bean
    public JdbcTokenRepositoryImpl jdbcTokenRepository(){
        // 配置数据源
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        // 自动在数据库创建权限表（再次启动会再次创建表，所以会报错，表示表已经存在）
        // jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

- 前端页面添加自动登陆表单项

```html
<div class="checkbox">
  <label>
    <input type="checkbox" name="remember-me"> 记住我
  </label>
</div>
```

## 自动登陆底层实现逻辑

- 首先从前端传来的 cookie 中解析出 series 和 token；

- 根据 series 从数据库中查询出一个 PersistentRememberMeToken 实例；

- 如果查出来的 token 和前端传来的 token 不相同，说明账号可能被人盗用(别人用你的令牌登录之后，token 会变)。此时根据用户名移除相关的 token，相当于必须要重新输入用户名密码登录才能获取新的自动登录权限。

- 接下来校验 token 是否过期;

- 构造新的 PersistentRememberMeToken 对象，并且更新数据库中的 token(这就是我们文章开头说的，新的会话都会对应一个新的 token)；

- 将新的令牌重新添加到 cookie 中返回；

- 根据用户名查询用户信息，再走一波登录流程。

# 用户注销

![用户注销流程](./images/用户注销流程.png)

## 在配置类添加注销的配置

```java
@Configuration
public class SecurityConfig {

    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private DataSource dataSource;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        // 指定认证管理器，通过UserDetailsService查询用户信息
        AuthenticationManagerBuilder managerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        managerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        AuthenticationManager authenticationManager = managerBuilder.build();

        // 没有权限跳转到no-permission.html页面
        httpSecurity.exceptionHandling().accessDeniedPage("/no-permission.html");
        
        httpSecurity
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .anyRequest().authenticated())
                .authenticationManager(authenticationManager)
                .formLogin()
                .loginPage("/login.html").permitAll() // 设置自定义登陆页面
                .loginProcessingUrl("/login") // 登陆时访问的路径
                .failureUrl("/error.html") // 登陆失败的页面
                .defaultSuccessUrl("/index.html") // 登陆成功后跳转的路径
                .and()
                .rememberMe() // 开启记住我的功能
                .tokenRepository(jdbcTokenRepository()) // 持久化令牌的方案
                .tokenValiditySeconds(60*60*24*7) // 设置令牌的有效期，以秒为单位
                .and()
                .logout().logoutUrl("/logout") // 注销接口
                .logoutSuccessUrl("/login.html") // 注销成功后跳转的页面
                .and()
                .csrf().disable(); // 需要关闭csrf防护功能，否则登陆不成功

        return httpSecurity.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

## 设置注销链接

添加success.html页面作为登陆成功后的跳转页面

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    登陆成功 <a href="/logout">退出</a>
</body>
</html>
```

登陆后访问退出按钮，实现注销功能。

# JWT(Json Web Token)

## 基于Token的认证方式

使用基于Token的身份验证方法，在服务端不需要存储用户的登陆信息。流程如下：

- 客户端使用用户名和密码请求登陆。
- 服务端收到请求，去验证用户名和密码。
- 验证成功后，服务端会签发一个Token，再把这个Token发送给客户端。
- 客户端收到Token以后可以把它存储在Cookie本地。
- 客户端每次向服务端请求资源时需要携带Cookie中该Token。
- 服务端收到请求后，验证客户端携带的Token，如果验证成功则返回数据。

![jwt认证](./images/jwt认证.png)

## 什么是JWT

JSON Web Token （JWT）是一个开放的行业标准（RFC 7519），它定义了一种简洁的、自包含的协议格式，用于在通信双方传递json对象，传递的信息经过数字签名可以被验证和信任。JWT可以使用HMAC算法或使用RSA的公钥/私钥对进行签名，防止被篡改。

JWT官网： https://jwt.io

JWT令牌的优点：

- JWT基于json，非常方便解析。
- 可以在令牌中自定义丰富的内容，易扩展。
- 通过非对称加密算法及数字签名技术，JWT防止篡改，安全性高。
- 资源服务使用JWT可不依赖认证服务即完成授权。

JWT令牌的缺点：

- JWT令牌较长，占存储空间比较大。

## JWT组成

一个JWT实际上就一个字符串，它由三部分组成，头部、负载与签名。

### 头部（Header）

头部用于描述关于该JWT的最基本信息，例如其类型（即JWT）以及签名所用的算法（如HMAC SHA256 或 RSA）等。这也可以被表示成一个JSON对象。

```json
{
  "alg":"HS256",
  "typ":"JWT"
}
```

- alg：签名算法
- typ：类型

我们对头部的json字符串进行BASE64编码，编码后的字符串如下：

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
```

Base64是一种基于64个可打印字符串来表示二进制数据的表示方式。JDK提供了非常方便的Base64Encoder和Base64Decoder，用它们可以非常方便的完成基于Base64的编码和解码。

### 负载（Payload）

负载，是存放有效信息的地方，比如用户的基本信息可以存在该部分中。负载包含三个部分：

- 标准中注册的声明（建议但不强制使用）
  - iss：jwt签发者
  - sub：jwt所面向的用户
  - aud：接收jwt的一方
  - exp：jwt的过期时间，过期时间必须大于签发时间
  - nbf：定义在什么时间之前，该jwt都是不可用的
  - iat：jwt的签发时间
  - jti：jwt的唯一身份标识，主要用来作为一次性token，从而回避重放攻击。

- 公共的声明

公共的声明可以添加任何信息，一般添加用户的相关信息或其他业务需要的必要信息，但不建议添加敏感信息，因为该部分在客户端可解密。

- 私有的声明

私有声明是提供者和消费者所共同定义的声明，一般不建议存放敏感信息，因为base64是对称解密的，意味着该部分信息可以归类为明文信息。

私有声明也就是自定义claim，用于存放自定义键值对。

```json
{
  "sub": "1234567890",
  "name": "John Doe",
  "iat": 1516239022
}
```

其中sub是标准的声明，name是自定义的私有声明，编码后如下：

```
eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ
```

### 签证、签名（Signature）

jwt的第三部分是一个签证信息，由三部分组成：

- Header（Base64编码后）
- Payload（Base64编码后）
- Secret（盐，必须保密）

这个部分需要Base64加密后的header和base4加密后的payload使用.连接组成的字符串，然后通过header重声明的加密方式进行加盐Secret组合加密，然后就构成了JWT的第三部分——使用“java01”作为盐：

```
eZqdTo1mRMB-o7co1oAiTvNvumfCkt-1H-CdfNm78Cw
```

从官方工具中可以看到，三个部分组合出的完整字符串：

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.eZqdTo1mRMB-o7co1oAiTvNvumfCkt-1H-CdfNm78Cw
```

<img src="./images/jwttoken.png" alt="jwttoken" style="zoom:80%;" />

注意：secret是保存在服务器端的，jwt在签发生成也是在服务器端的，secret就是用来进行jwt的签发和验证，所以，它就是服务器端的私钥，在任何场景都不应该泄漏。一旦客户端得知这个secret，那就意味着客户端是可以自我签发jwt了。

## 使用JJWT

JJWT是一个提供端到端的JWT创建和验证的开源Java库。也就是说使用JJWT能快速完成JWT的功能开发。

- 引入依赖

创建Springboot工程并引入jjwt依赖，pom.xml如下：

```xml
<!--jjwt-->
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt</artifactId>
  <version>RELEASE</version>
</dependency>
```

- 创建Token

```java
@Test
void testCreateToken() {
  // 创建JWT对象
  JwtBuilder jwtBuilder = Jwts.builder()
    // 设置负载内容
    // 用户的ID
    .setId("1001")
    // 用户名
    .setSubject("小明")
    // JWT 的签发时间
    .setIssuedAt(new Date())
    // 设置签名算法和盐
    .signWith(SignatureAlgorithm.HS256, "java01");
  // 获取JWT对象中的字符串
  String token = jwtBuilder.compact();
  System.out.println(token);
}
```

JWT将用户信息转换成Token字符串，生成结果如下：

```
eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMDAxIiwic3ViIjoi5bCP5piOIiwiaWF0IjoxNjg2MTIwOTg3fQ.KZsoZeqD5qVNwEsRV7jAYCVzVTR_CyXwYyuDhSzUj_U
```

- 解析Token

通过JWT解析Token，获取Token中存放的用户信息，即生成Claims对象。

```java
@Test
public void testParseToken() {
  String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMDAxIiwic3ViIjoi5bCP5piOIiwiaWF0IjoxNjg2MTIwOTg3fQ.KZsoZeqD5qVNwEsRV7jAYCVzVTR_CyXwYyuDhSzUj_U";
  // 解析Token，生成Claims对象，Token中存放的用户信息解析到了claims对象中
  Claims claims = Jwts.parser().setSigningKey("java01").parseClaimsJws(token).getBody();
  System.out.println("id:" + claims.getId());
  System.out.println("subject:" + claims.getSubject());
  System.out.println("IssuedAt:" + claims.getIssuedAt());
}
```

解析结果如下：

```
id:1001
subject:小明
IssuedAt:Wed Jun 07 13:56:27 ICT 2023
```

- Token过期检验

在有效期内Token可以正常读取，超过有效期则Token失效

```java
@Test
void testExpToken() {
  JwtBuilder jwtBuilder = Jwts.builder()
    .setId("1001")
    .setSubject("小明")
    .setIssuedAt(new Date())
    .signWith(SignatureAlgorithm.HS256, "java01")
    .setExpiration(new Date(System.currentTimeMillis() + 10 * 1000)); // 设置10秒之后超时
  // 获取JWT对象中的字符串
  String token = jwtBuilder.compact();
  System.out.println(token);
}
```

- 自定义claims

除了使用官方api设置属性值，也可以添加自定义键值对。

```java
@Test
void testCustomClaims() {
  JwtBuilder jwtBuilder = Jwts.builder()
    .setId("1001")
    .setSubject("小明")
    .setIssuedAt(new Date())
    .signWith(SignatureAlgorithm.HS256, "java01")
    .claim("role", "admin");
  String token = jwtBuilder.compact();
  System.out.println(token);
}
```

使用下面语句获取属性值：

```java
claims.get("role");
```

# 微服务项目-使用Security+JWT实现权限管理

## 前后端分离的权限管理

![Spring Security + jwt 前后端分离的权限系统的时序图](./images/Spring Security + jwt 前后端分离的权限系统的时序图.png)

## 引入依赖



