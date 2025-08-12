# MongoDB 协议

## 配置元件 [示例](../template/配置元件/mongo_defaults.yaml)

MongoDB 默认配置：使用该组件，可配置 MongoDB协议的默认配置，降低测试集合的配置复杂度。

当测试集合描述文件中存在此配置时，下级测试集合中包含的 MongoDB 取样器\处理器从此配置中获取相关配置。

```yaml
# MongoDB 默认配置，各配置项的优先级为：取样器 > 默认配置
testclass: mongo  # 取样器类型
ref_name: mongo_source # 引用名称，可空，空值时，使用默认Key作为引用名称
config: # 可简化填写，无需config关键字，直接将配置内容至于首层
  url: 'mongodb://root:123456@127.0.0.1:27017/?authSource=admin' # 可空，空值时，使用默认配置中的 url
  database: 'demo' # 数据库名称，可空，空值时，使用默认配置中的 database
  collection: 'demo' # 表名，可空，空值时，使用默认配置中的 collection
```

## 处理器

### 前置处理器 [示例](../template/处理器/mongo_preprocessor.yaml)

```yaml
testclass: mongo  # 前置处理器类型
config: # 处理器器配置
  url: 'mongodb://root:123456@127.0.0.1:27017/?authSource=admin' # 可空，空值时，使用默认配置中的 url
  database: 'demo' # 数据库名称，可空，空值时，使用默认配置中的 database
  collection: 'demo' # 表名，可空，空值时，使用默认配置中的 collection
  action: find  # 操作类型：find、insert、update、delete
  condition: { } # 条件，非空
```

### 后置处理器 [示例](../template/处理器/mongo_postprocessor.yaml)

```yaml
testclass: mongo  # 后置处理器类型
config: # 处理器器配置
  url: 'mongodb://root:123456@127.0.0.1:27017/?authSource=admin' # 可空，空值时，使用默认配置中的 url
  database: 'demo' # 数据库名称，可空，空值时，使用默认配置中的 database
  collection: 'demo' # 表名，可空，空值时，使用默认配置中的 collection
  action: find  # 操作类型：find、insert、update、delete
  condition: { } # 条件，非空
```

## 取样器 [示例](../template/取样器/mongo_sampler.yaml)

```yaml
testclass: mongo  # 取样器类型
config: # 取样器配置
  url: 'mongodb://root:123456@127.0.0.1:27017/?authSource=admin' # 可空，空值时，使用默认配置中的 url
  database: 'demo' # 数据库名称，可空，空值时，使用默认配置中的 database
  collection: 'demo' # 表名，可空，空值时，使用默认配置中的 collection
  action: find  # 操作类型：find、insert、update、delete
  condition: { } # 条件，非空
```

# 常见问题

1. 当一个测试集合内存在多个 MongoDB默认配置时，多个 MongoDB默认配置中的配置会合并，相同配置项以后定义的
   MongoDB默认配置的值为准。
2. 当取样器中的 MongoDB配置项 与 MongoDB默认配置中的配置项重复时，以取样器中的配置项的值为准