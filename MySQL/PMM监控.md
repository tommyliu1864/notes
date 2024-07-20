# MySQL 安装与配置

```shell
docker run -d \
  --name mysql \
  -p 3306:3306 \
  -e TZ=Asia/Shanghai \
  -e MYSQL_ROOT_PASSWORD=qty32m0HbpFQXJCbBb4L \
  -v ./mysql/data:/var/lib/mysql \
  -v ./mysql/logs:/var/log/mysql \
  -v ./mysql/conf:/etc/mysql/conf.d \
  -v ./mysql/init:/docker-entrypoint-initdb.d \
  mysql
```

my.cnf 配置慢查询、开启performance_schema

```
[mysqld]
slow_query_log = ON
long_query_time = 1
slow_query_log_file = /var/log/mysql/mysql-slow.log
performance_schema = ON
```

# 安装PMM Server

PMM Server是PMM的核心部分，它聚合收集的数据，并以Web界面的表格，仪表板和图形的形式呈现,包括以下组件

- Query Analytics(QAN):按时间周期查询MySQL性能，同客户端的qan agent通讯，包括两个组件：qan api 和qan web app
- Metrics Monitor(MM)：提供MySQL和mongo的性能历史视图

使用Docker安装PMM Server

```shell
docker pull percona/pmm-server:latest

docker create \
  -v /root/pmm/prometheus-data:/opt/prometheus/data \
  -v /root/pmm/consul-data:/opt/consul-data \
  -v /root/pmm/mysql-data:/var/lib/mysql \
  -v /root/pmm/grafana-data:/var/lib/grafana \
  -p 8090:80 \
  -p 8091:443 \
  --name pmm-server \
  percona/pmm-server:latest
  
docker start pmm-server
```

# 安装PMM Client

PMM Client是安装在你要监视的MySQL或MongoDB主机上的一组代理组件。组件收集关于一般系统和数据库性能的各种数据，并将该数据发送到相应的PMM服务器组件。

在要监控的MySQL服务器上安装PMM Client

```shell
sudo yum install -y https://repo.percona.com/yum/percona-release-latest.noarch.rpm
sudo yum install -y pmm2-client
```

配置PMM Client连接到PMM Server

```shell
sudo pmm-admin config --server-insecure-tls --server-url=https://admin:admin@127.0.0.1:8091
```

# 添加要监控的MySQL服务

```shell
# 这里perfschema和慢查询日志都可以作为数据源，二者选其一即可，但我将慢查询日志作为数据源一直失败
sudo pmm-admin add mysql --query-source=perfschema --username=root --password=qty32m0HbpFQXJCbBb4L --host=127.0.0.1 --port=3306
```

# 验证安装

**访问PMM Server的Web界面**

打开浏览器，输入`https://173.212.204.223:8091`，你将看到PMM Server的登录页面。默认用户名和密码均为`admin`。

**检查监控数据**

登录后，你可以看到已经添加的MySQL服务的监控数据。通过不同的Dashboard可以查看查询性能、数据库状态、系统资源使用情况等信息。

# 注意

如果打开PMM Server的Web界面发现，一直loading，分析pmm-server日志发现qan-api2启动失败，可以手动重启qan-api2

```shell
docker exec -it pmm-server supervisorctl restart qan-api2
```

检查 PMM Client 状态

```shell
pmm-admin list
```

重启pmm-agent

```shell
systemctl restart pmm-agent
```

