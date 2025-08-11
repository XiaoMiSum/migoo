# Kafka 协议

## 配置元件 [示例](../template/配置元件/kafka_defaults.yaml)

Kafka 默认配置：使用该组件，可配置 Kafka协议的默认配置，降低测试集合的配置复杂度。

当测试集合描述文件中存在此配置时，下级测试集合中包含的 Kafka 取样器\处理器从此配置中获取相关配置。

```yaml
# kafka 默认配置，各配置项的优先级为：取样器 > 默认配置
testclass: kafka # 配置元件类型
config: # 可简化填写，无需config关键字，直接将配置内容至于首层
  key.serializer: org.apache.kafka.common.serialization.StringSerializer # key 序列化 可空
  value.serializer: org.apache.kafka.common.serialization.StringSerializer # value 序列化 可空
  acks: 1 # 可空
  retries: 5  # 可空
  linger.ms: 20   # 可空
  bootstrap.servers: 192.168.1.7:9092 # 服务器地址
  topic: xxxx  # ProducerRecord 中的 topic
  key: xxxx # ProducerRecord 中的 key
```

## 处理器

### 前置处理器 [示例](../template/处理器/kafka_preprocessor.yaml)

```yaml
testclass: kafka # kafka 前置处理器类型
config: # 处理器配置
  topic: ryze.topic # ProducerRecord 中的 topic
  key: test # ProducerRecord 中的 key
  bootstrap.servers: 192.168.1.7:9092 # 服务器地址
  key.serializer: org.apache.kafka.common.serialization.StringSerializer # key 序列化 可空
  value.serializer: org.apache.kafka.common.serialization.StringSerializer # value 序列化
  acks: 1
  retries: 5
  linger.ms: 20
  message: # 发送的消息，ProducerRecord中的 value, 可以是任意json对象、字符串、数字等
    name: test
    age: 18
```

### 后置处理器 [示例](../template/处理器/kafka_postprocessor.yaml)

```yaml
testclass: kafka # kafka 后置处理器类型
config: # 处理器配置
  topic: ryze.topic # ProducerRecord 中的 topic
  key: test # ProducerRecord 中的 key
  bootstrap.servers: 192.168.1.7:9092 # 服务器地址
  key.serializer: org.apache.kafka.common.serialization.StringSerializer # key 序列化 可空
  value.serializer: org.apache.kafka.common.serialization.StringSerializer # value 序列化
  acks: 1
  retries: 5
  linger.ms: 20
  message: # 发送的消息，ProducerRecord中的 value, 可以是任意json对象、字符串、数字等
    name: test
    age: 18
```

## 取样器 [示例](../template/取样器/kafka_sampler.yaml)

```yaml
title: 标准kafka取样器
testclass: kafka # 取样器类型
config: # 取样器配置
  topic: ryze.topic # ProducerRecord 中的 topic
  key: test # ProducerRecord 中的 key
  bootstrap.servers: 192.168.1.7:9092 # 服务器地址
  key.serializer: org.apache.kafka.common.serialization.StringSerializer # key 序列化 可空
  value.serializer: org.apache.kafka.common.serialization.StringSerializer # value 序列化
  acks: 1
  retries: 5
  linger.ms: 20
  message: # 发送的消息，ProducerRecord中的 value, 可以是任意json对象、字符串、数字等
    name: test
    age: 18
```

# 常见问题

1. 当一个测试集合内存在多个 Kafka默认配置时，多个 Kafka默认配置中的配置会合并，相同配置项以后定义的 Kafka默认配置的值为准。
2. 当取样器中的 Kafka配置项 与 Kafka默认配置中的配置项重复时，以取样器中的配置项的值为准