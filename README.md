# Saber
Saber 是针对中小型组织设计的消息推送和触达平台，采用 Java、Spring Boot、Spring Cloud 和 Nacos 构建的分布式架构。平台
集成了飞书、短信、邮箱等多种下游服务，为上游应用提供便捷、灵活的消息接入通道。目前已成功部署于一个超过500名成员的学
生组织，服务用户超过2000人，并衍生了多个优秀的上游应用。

## 部分页面
![规则管理](/img/saber-1.png)
![消息模版](/img/saber-2.png)
![同志记录](/img/saber-3.png)
![日志监控](/img/saber-4.png)

## 流程示意
![流程图](/img/saber-5.png)

## 使用说明
需要有开发能力！！！
1. 运行 `docker-compose.yml` 搭建环境
2. 登陆到 rabbitmq、nacos 进行的基本配置
3. 连接到数据库手动初始化 root 用户，并关联到 root 应用方
4. 尝试本地启动项目
5. 执行 `Dockerfile` 构建镜像
6. 在 docker 以镜像的方式启动