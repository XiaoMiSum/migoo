# Dubbo 协议

## 配置元件 [示例](../template/配置元件/dubbo_defaults.yaml)

Dubbo 默认配置：使用该组件，可配置 Dubbo协议的默认配置，降低测试集合的配置复杂度。

当测试集合描述文件中存在此配置时，下级测试集合中包含的 Dubbo 取样器\处理器从此配置中获取相关配置。

```yaml
# Dubbo 默认配置，各配置项的优先级为：取样器 > 默认配置
testclass: dubbo # 取样器类型
config: # 可简化填写，无需config关键字，直接将配置内容至于首层
  registry: # 注册中心配置
    protocol: zookeeper # zookeeper、nacos
    address: localhost:2181
    username:
    password:
    version: 1.0.0
  reference: # reference配置
    version: 1.0.0
    retries: 1
    timeout: 5000
    async: false
    load_balance: random
```

## 处理器

### 前置处理器 [示例](../template/处理器/dubbo_preprocessor.yaml)

```yaml
testclass: dubbo # http前置处理器 类型
config: # 取样器配置
  registry: # 注册中心配置
    protocol: zookeeper
    address: localhost:2181
    username:
    password:
    version: 1.0.0
  reference: # reference配置
    version: 1.0.0
    retries: 1
    timeout: 5000
    async: false
    load_balance: random
  interface: protocol.xyz.ryze.dubbo.dubboserver.service.DemoService  # 接口类名全称
  method: sayHello  # 接口方法
  parameter_types: # 方法参数类型，根据接口定义
    - java.lang.String
  parameters: # 接口参数名称
    - name: test
  attachment_args: # 附加参数 keyword形式，可空
```

### 后置处理器 [示例](../template/处理器/dubbo_postprocessor.yaml)

```yaml
testclass: dubbo # http后置处理器 类型
config: # 取样器配置
  registry: # 注册中心配置
    protocol: zookeeper
    address: localhost:2181
    username:
    password:
    version: 1.0.0
  reference: # reference配置
    version: 1.0.0
    retries: 1
    timeout: 5000
    async: false
    load_balance: random
  interface: io.github.xiaomisum.ryze.dubbo.example.DemoService  # 接口类名全称
  method: sayHello  # 接口方法
  parameter_types: # 方法参数类型，根据接口定义
    - java.lang.String
  parameters: # 接口参数名称
    - name: test
  attachment_args: # 附加参数 keyword形式，可空
```

## 取样器 [示例](../template/取样器/dubbo_sampler.yaml)

```yaml
title: 标准dubbo取样器
testclass: dubbo # 取样器类型
config: # 取样器配置
  registry: # 注册中心配置
    protocol: zookeeper
    address: localhost:2181
    username:
    password:
    version: 1.0.0
  reference: # reference配置
    version: 1.0.0
    retries: 1
    timeout: 5000
    group: test
    async: false
    load_balance: random
  interface: io.github.xiaomisum.ryze.dubbo.example.DemoService  # 接口类名全称
  method: sayHello  # 接口方法
  parameter_types: # 方法参数类型，根据接口定义
    - java.lang.String
  parameters: # 接口参数名称
    - name: test
  attachment_args: # 附加参数 keyword形式，可空
```

# 常见问题

1. 当一个测试集合内存在多个 Dubbo默认配置时，多个 Dubbo默认配置中的配置会合并，相同配置项以后定义的 Dubbo默认配置的值为准。
2. 当取样器中的 Dubbo配置项 与 Dubbo默认配置中的配置项重复时，以取样器中的配置项的值为准