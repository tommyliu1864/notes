# 0.导入商城项目

在课前资料中给大家提供了商城项目的资料，我们需要先导入这个单体项目。不过需要注意的是，本篇及后续的微服务学习都是基于Centos7系统下的Docker部署。

## 0.1.安装MySQL

在课前资料提供好了MySQL的一个目录：

![img](SpringCloud.assets/17030735076324.png)

其中有MySQL的配置文件和初始化脚本：



```bash
docker run -d \
  --name mysql \
  -p 3306:3306 \
  -e TZ=Asia/Shanghai \
  -e MYSQL_ROOT_PASSWORD=Root1234$ \
  -v /root/mysql/data:/var/lib/mysql \
  -v /root/mysql/conf:/etc/mysql/conf.d \
  -v /root/mysql/init:/docker-entrypoint-initdb.d \
  --network hm-net\
  mysql
```

