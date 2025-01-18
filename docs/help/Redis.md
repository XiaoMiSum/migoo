# Redis 协议

## 配置元件 [示例](../template/配置元件/redis_datasource.yaml)

Redis 数据源：使用该组件配置 Redis数据源，用于 Redis处理器\取样器引用。

```yaml
# redis取样器、处理器必须引用一个redis数据源
testclass: RedisDatasource # 配置元件类型
variable_name: JDBCDataSource_var  # 数据源名称
username: 'root'
password: '123456qq'
host: localhost # 服务器地址
port: 6379 # 端口 默认 6379
time_out: 5000 # 连接超时时间，默认 10000ms
max_total: 10 # 可空 默认 10
max_idle: 5 # 可空 默认 5
min_idle: 1 # 可空，默认 1
```

## 处理器

### 前置处理器 [示例](../template/处理器/redis_preprocessor.yaml)

```yaml
testclass: redis_preprocessor # redis 前置处理器类型
config: # 处理器配置
  datasource: RedisDataSource_var # 数据源名称，必须先配置redis数据源
  command: hset    # redis 命令
  send: syshash,key2,value2 # 参数，本示例为 向 syshash 这个类型为 hash的key中设置 key2的值为 value2
```

### 后置处理器 [示例](../template/处理器/redis_postprocessor.yaml)

```yaml
testclass: redis_postprocessor # redis 后置处理器类型
config: # 处理器配置
  datasource: RedisDataSource_var # 数据源名称，必须先配置redis数据源
  command: hset    # redis 命令
  send: syshash,key2,value2 # 参数，本示例为 向 syshash 这个类型为 hash的key中设置 key2的值为 value2
```

## 取样器 [示例](../template/取样器/redis_sampler.yaml)

```yaml
title: 标准redis取样器
testclass: redissampler # 取样器类型
config: # 取样器配置
  datasource: RedisDataSource_var # 数据源名称，必须先配置redis数据源
  command: hset    # redis 命令
  send: syshash,key2,value2 # 参数，本示例为 向 syshash 这个类型为 hash的key中设置 key2的值为 value2
```
