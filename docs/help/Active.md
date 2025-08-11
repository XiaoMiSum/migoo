# ActiveMQ 协议

## 配置元件 [示例](../template/配置元件/active_defaults.yaml)

ActiveMQ 默认配置：使用该组件，可配置 ActiveMQ协议的默认配置，降低测试集合的配置复杂度。

当测试集合描述文件中存在此配置时，下级测试集合中包含的 ActiveMQ 取样器\处理器从此配置中获取相关配置。

```yaml
# activemq 默认配置，各配置项的优先级为：取样器 > 默认配置
testclass: active  # 取样器类型
config: # 可简化填写，无需config关键字，直接将配置内容至于首层
  username: admin # activemq 用户名
  password: admin # activemq 密码
  queue: ryze.queue # 订阅模式 优先级高于 topic，根据实际选择使用
  topic: test  # 广播模式
  broker.url: 'tcp://192.168.1.7:61616'
```

## 处理器

### 前置处理器 [示例](../template/处理器/active_preprocessor.yaml)

```yaml
testclass: active  # active mq 前置处理器类型
config: # 处理器配置
  username: admin # activemq 用户名
  password: admin # activemq 密码
  queue: ryze.queue # 订阅模式 优先级高于 topic，根据实际选择使用
  topic: test  # 广播模式
  broker.url: 'tcp://192.168.1.7:61616'
  message: # 发送的消息，可以是任意json对象、字符串、数字等
    name: test
    age: 18
```

### 后置处理器 [示例](../template/处理器/active_postprocessor.yaml)

```yaml
testclass: active  # active mq 后置处理器类型
config: # 处理器配置
  username: admin # activemq 用户名
  password: admin # activemq 密码
  queue: ryze.queue # 订阅模式 优先级高于 topic，根据实际选择使用
  topic: test  # 广播模式
  broker.url: 'tcp://192.168.1.7:61616'
  message: # 发送的消息，可以是任意json对象、字符串、数字等
    name: test
    age: 18
```

## 取样器 [示例](../template/取样器/active_sampler.yaml)

```yaml
title: 标准activemq取样器
testclass: active  # 取样器类型
config: # 取样器配置
  username: admin # activemq 用户名
  password: admin # activemq 密码
  queue: ryze.queue # 订阅模式 优先级高于 topic，根据实际选择使用
  topic: test  # 广播模式
  broker.url: 'tcp://192.168.1.7:61616'
  message: # 发送的消息，可以是任意json对象、字符串、数字等
    name: test
    age: 18
```

# 常见问题

1. 当一个测试集合内存在多个 ActiveMQ默认配置时，多个 ActiveMQ默认配置中的配置会合并，相同配置项以后定义的
   ActiveMQ默认配置的值为准。
2. 当取样器中的 ActiveMQ配置项 与 ActiveMQ默认配置中的配置项重复时，以取样器中的配置项的值为准