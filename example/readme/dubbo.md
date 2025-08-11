# Docker Dubbo 环境搭建

1. 拉取镜像 Zookeeper

        docker pull zookeeper:latest

2. 启动 Zookeeper

        docker run --name some-zookeeper -p 42181:2181 -p 42888:2888 -p 43888:3888 -p 48080:8080 --restart always -d zookeeper

# 启动 Dubbo Java 服务端

      启动 dubbo-example 中的 DubboApplication

# 执行 Ryze Dubbo 测试

      运行 dubbo-example 中的 Test

# 执行结果

- DubboApplication 控制台打印 dubbo Sampler 的请求信息

  ![dubbo_service](images/dubbo_service.png)