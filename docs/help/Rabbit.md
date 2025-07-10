# RabbitMQ 协议

## 配置元件 [示例](../template/配置元件/rabbitmq_defaults.yaml)

RabbitMQ 默认配置：使用该组件，可配置 RabbitMQ协议的默认配置，降低测试集合的配置复杂度。

当测试集合描述文件中存在此配置时，下级测试集合中包含的 RabbitMQ 取样器\处理器从此配置中获取相关配置。

```yaml
# rabbitmq 默认配置，各配置项的优先级为：取样器 > 默认配置
testclass: rabbitmq_defaults  # 配置元件类型
host: 'localhost' # rabbitmq服务器地址
port: 5672 # rabbitmq服务器端口，可空，默认值：5672
username: guest # rabbitmq 用户名，可空，默认值：guest
password: guest # rabbitmq 密码，可空，默认值：guest
timeout: 5000 # 连接超时时间，毫秒，默认值：60000
queue: # 队列配置
  name: migoo  # 消息队列名称
  durable:  # 是否持久化队列，可空，默认值：false
  exclusive:  # 是否独占队列，可空，默认值：false
  auto_delete:  # 是否自动删除，可空，默认值：false
  arguments:  # 消息队列其他属性，keyword对象，可空
exchange: # exchange 配置，可空，简单模式（不经过交换机）时，此配置为空
  name: migoo # exchange 名称
  type: topic # 交换机类型，参考值：fanout、direct、topic，可空，默认值：topic
  routing_key:  # 路由key，当 type为 fanout 时可空
```

## 处理器

### 前置处理器 [示例](../template/处理器/rabbitmq_preprocessor.yaml)

```yaml
testclass: rabbitmq_preprocessor  # 前置处理器类型
config: # 处理器配置
  host: 'localhost' # rabbitmq服务器地址
  port: 5672 # rabbitmq服务器端口，可空，默认值：5672
  username: guest # rabbitmq 用户名，可空，默认值：guest
  password: guest # rabbitmq 密码，可空，默认值：guest
  timeout: 5000 # 连接超时时间，毫秒，默认值：60000
  queue: # 队列配置
    name: migoo  # 消息队列名称
    durable:  # 是否持久化队列，可空，默认值：false
    exclusive:  # 是否独占队列，可空，默认值：false
    auto_delete:  # 是否自动删除，可空，默认值：false
    arguments:  # 消息队列其他属性，keyword对象，可空
  exchange: # exchange 配置，可空，简单模式（不经过交换机）时，此配置为空
    name: migoo # exchange 名称
    type: topic # 交换机类型，参考值：fanout、direct、topic，可空，默认值：topic
    routing_key:  # 路由key，当 type为 fanout 时可空
  props: # 消息的其他属性，可空，非空时该配置下的所有属性均需设值
    content_type: xdd
    content_encoding: ddd
    headers:
      aa: aa # 示例
    delivery_mode: 1
    priority: 1
    correlation_id: ddd
    reply_to: ddd
    expiration: eqe
    message_id: ddd
    timestamp: 222
    type: ddd
    user_id: ddd
    app_id: ddd
    cluster_id: ddd
  message: # 发送的消息，可以是任意json对象、字符串、数字等
    name: test
    age: 18
```

### 后置处理器 [示例](../template/处理器/rabbitmq_postprocessor.yaml)

```yaml
testclass: rabbitmq_postprocessor  # 后置处理器类型
config: # 处理器配置
  host: 'localhost' # rabbitmq服务器地址
  port: 5672 # rabbitmq服务器端口，可空，默认值：5672
  username: guest # rabbitmq 用户名，可空，默认值：guest
  password: guest # rabbitmq 密码，可空，默认值：guest
  timeout: 5000 # 连接超时时间，毫秒，默认值：60000
  queue: # 队列配置
    name: migoo  # 消息队列名称
    durable:  # 是否持久化队列，可空，默认值：false
    exclusive:  # 是否独占队列，可空，默认值：false
    auto_delete:  # 是否自动删除，可空，默认值：false
    arguments:  # 消息队列其他属性，keyword对象，可空
  exchange: # exchange 配置，可空，简单模式（不经过交换机）时，此配置为空
    name: migoo # exchange 名称
    type: topic # 交换机类型，参考值：fanout、direct、topic，可空，默认值：topic
    routing_key:  # 路由key，当 type为 fanout 时可空
  props: # 消息的其他属性，可空，非空时该配置下的所有属性均需设值
    content_type: xdd
    content_encoding: ddd
    headers:
      aa: aa # 示例
    delivery_mode: 1
    priority: 1
    correlation_id: ddd
    reply_to: ddd
    expiration: eqe
    message_id: ddd
    timestamp: 222
    type: ddd
    user_id: ddd
    app_id: ddd
    cluster_id: ddd
  message: # 发送的消息，可以是任意json对象、字符串、数字等
    name: test
    age: 18
```

## 取样器 [示例](../template/取样器/rabbitmq_sampler.yaml)

```yaml
title: 标准rabbitmq取样器
testclass: rabbitmqSampler  # 取样器类型
config: # 取样器配置
  host: 'localhost' # rabbitmq服务器地址
  port: 5672 # rabbitmq服务器端口，可空，默认值：5672
  username: guest # rabbitmq 用户名，可空，默认值：guest
  password: guest # rabbitmq 密码，可空，默认值：guest
  timeout: 5000 # 连接超时时间，毫秒，默认值：60000
  queue: # 队列配置
    name: migoo  # 消息队列名称
    durable:  # 是否持久化队列，可空，默认值：false
    exclusive:  # 是否独占队列，可空，默认值：false
    auto_delete:  # 是否自动删除，可空，默认值：false
    arguments:  # 消息队列其他属性，keyword对象，可空
  exchange: # exchange 配置，可空，简单模式（不经过交换机）时，此配置为空
    name: migoo # exchange 名称
    type: topic # 交换机类型，参考值：fanout、direct、topic，可空，默认值：topic
    routing_key:  # 路由key，当 type为 fanout 时可空
  props: # 消息的其他属性，可空，非空时该配置下的所有属性均需设值
    content_type: xdd
    content_encoding: ddd
    headers:
      aa: aa # 示例
    delivery_mode: 1
    priority: 1
    correlation_id: ddd
    reply_to: ddd
    expiration: eqe
    message_id: ddd
    timestamp: 222
    type: ddd
    user_id: ddd
    app_id: ddd
    cluster_id: ddd
  message: # 发送的消息，可以是任意json对象、字符串、数字等
    name: test
    age: 18
```

# 常见问题

1. 当一个测试集合内存在多个 RabbitMQ默认配置时，多个 RabbitMQ默认配置中的配置会合并，相同配置项以后定义的
   RabbitMQ默认配置的值为准。
2. 当取样器中的 RabbitMQ配置项 与 RabbitMQ默认配置中的配置项重复时，以取样器中的配置项的值为准