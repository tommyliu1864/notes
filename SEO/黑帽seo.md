# 关键词挖掘

## 长尾词、相关词

- 工具：5188 https://www.5118.com/ci

  ![5188关键词挖掘](./images/5188关键词挖掘.png)

## 竞品词

- 工具：爱站网百度权重查询 https://baidurank.aizhan.com/mobile/www.1010jz.com/

![百度权重关键词挖掘](./images/百度权重关键词挖掘.png)

## 双标题

- 将关键词和搜索下拉的词组合，成为两个关键词组合的标题

![百度搜索下拉](./images/百度搜索下拉.png)

![双标题工具](./images/双标题工具.png)

# 小旋风蜘蛛池

## 安装

### 关系梳理

先弄清楚宝塔站点、小旋风蜘蛛池程序、网站三者之间的关系

![站点与蜘蛛池关系](./images/站点与蜘蛛池关系.png)

- 我们通过在宝塔上新建一个站点，向站点上传一个蜘蛛池程序，通过这个蜘蛛池程序可以创建很多个网站
- 每个网站有独立的域名可供访问，但是这些网站都运行在同一台机器、同一个IP上，所以这些域名需要通过nginx进行反向代理
- 我们通过宝塔对域名进行管理，其本质就是通过宝塔配置nginx

### 域名解析

提前进行域名解析，*代表泛二级域名

![域名解析](./images/域名解析.png)

### 宝塔配置

#### 环境安装

确保宝塔安装了蜘蛛池运行所需的软件，Nginx、MySQL、PHP（7.2）

![蜘蛛池运行所需的软件](./images/蜘蛛池运行所需的软件.png)

#### 添加站点

记得选择PHP版本

![宝塔添加站点](./images/宝塔添加站点.png)

#### 添加域名

点击`网站名`，添加域名，这里添加了 `*.s1u.cn`，支持二级域名访问

![添加域名](./images/添加域名.png)

#### 上传蜘蛛池程序

点击 `/www/wwwroot/s1u.cn`，上传文件

![上传蜘蛛池程序](./images/上传蜘蛛池程序.png)

- 解压安装包

![解压蜘蛛池安装包](./images/解压蜘蛛池安装包.png)

###  安装蜘蛛池

![安装蜘蛛池1](./images/安装蜘蛛池1.png)

![安装蜘蛛池2](./images/安装蜘蛛池2.png)

### 配置网站域名

因为小旋风蜘蛛池自带测试网站，并且已经配置好内容，所以这里修改域名，配置成前面解析的`s1u.cn`域名

![蜘蛛池配置网站域名](./images/蜘蛛池配置网站域名.png)

### Nginx 伪静态配置

- 打开宝塔目录`/www/wwwroot/s1u.cn/temp/rewrite`下文件`nginx的伪静态规则.txt`，并复制

```nginx
rewrite ^/template/(.*)\.html$ /index.php last;
rewrite ^/temp/(data|db|robotlog|tplrules|errpage|logs|session)/(.*)$ /index.php last;
rewrite ^/(temp|template|core|static)/(.*)\.php$ /index.php last;
if (!-e $request_filename){
  rewrite ^/(.*)$ /index.php?$1 last;
}
```

![nginx伪静态规则](./images/nginx伪静态规则.png)

- 添加站点的nginx伪静态配置

![伪静态配置](./images/伪静态配置.png)



### 开启蜘蛛池状态

![开启蜘蛛池状态](./images/开启蜘蛛池状态.png)



### 访问网站

- http://www.s1u.cn/

![访问网站1](./images/访问网站1.png)

- http://bbs.s1u.cn/

![访问网站2](./images/访问网站2.png)

- http://flash.s1u.cn/

![访问网站3](./images/访问网站3.png)



这三个域名之所以能够访问，是因为默认设置了`自定义泛域名前缀`

![自定义泛域名](./images/自定义泛域名.png)



## 设置模版

### 自动选取

创建一个网站时，我们可以指定`所属模型`，然后程序会自动从`模型模版`选取模版

![自动选取模版](./images/自动选取模版.png)

程序会从`行业企业`的模版列表中任意选取一个模版，作为网站的模版

![模版列表](./images/模版列表.png)

### 绑定模版

如果我们不希望程序自动去选取模版，而是使用指定的模版

![绑定模版](./images/绑定模版.png)

