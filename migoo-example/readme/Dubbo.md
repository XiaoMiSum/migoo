# Docker Dubbo 环境搭建

1. 拉取镜像 Zookeeper

        docker pull zookeeper:latest

2. 启动 Zookeeper

        docker run --name some-zookeeper -p 2181:2181 -p 2888:2888 -p 3888:3888 -p 8080:8080 --restart always -d zookeeper

# 启动 Dubbo Java 服务端

      启动 dubbo-example 中的 DubboApplication

# 执行 MiGoo Active 测试

      运行 dubbo-example 中的 Test

# 执行结果

- DubboApplication 控制台打印 dubbo Sampler 的请求信息

  ![dubbo_service](images/dubbo_service.png)
- 项目根目录下生成 out-put，并保存HTML 报告

  ![dubbo_example_reports](images/dubbo_example_reports.png)