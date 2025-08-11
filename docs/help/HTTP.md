# HTTP 协议

## 配置元件 [示例](../template/配置元件/http_defaults.yaml)

HTTP 默认配置：使用该组件，可配置 HTTP协议的默认配置，降低测试集合的配置复杂度。

当测试集合描述文件中存在此配置时，下级测试集合中包含的 HTTP 取样器\处理器从此配置中获取相关配置。

```yaml
# Http 默认配置，各配置项的优先级为：取样器 > 默认配置
testclass: http  # 配置元件类型
config: # 可简化填写，无需config关键字，直接将配置内容至于首层
  method: GET  # 请求方法，默认Get
  protocol: http  # 请求协议，http、https，可空，默认 http
  host: localhost
  port: 8080 # 端口，默认 80
  path: /test # 接口路径，可空
  http/2: false # 是否 http2， ture、false，可空，默认 false
  headers: # 请求头，可空
    h1: 1
```

## 处理器

### 前置处理器 [示例](../template/处理器/http_preprocessor.yaml)

```yaml
testclass: http  # http前置处理器 类型
config: # 处理器配置
  method: post   # 请求方法
  protocol: http   # 请求协议，默认 http
  http/2: false # 是否 http2， ture、false，可空，默认 false
  port: 8080   # 请求端口，默认 80
  host: localhost  # 服务器地址
  path: /user   # 接口path
  headers: # 请求头，可空
    h1: 1
  query: { } # url中的参数，如: ?id=1&name=t {id: 1, name: t}
  data: { } # 请求data
  body: { userName: 'ryze', password: '123456qq' } # 请求body 优先级高于 data
```

### 后置处理器 [示例](../template/处理器/http_postprocessor.yaml)

```yaml
testclass: http  # http后置处理器 类型
config: # 处理器配置
  method: post   # 请求方法
  protocol: http   # 请求协议，默认 http
  http/2: false # 是否 http2， ture、false，可空，默认 false
  port: 8080   # 请求端口，默认 80
  host: localhost  # 服务器地址
  path: /user   # 接口path
  headers: # 请求头，可空
    h1: 1
  query: { } # url中的参数，如: ?id=1&name=t {id: 1, name: t}
  data: { } # 请求data
  body: { userName: 'ryze', password: '123456qq' } # 请求body 优先级高于 data
```

## 取样器 [示例](../template/取样器/http_sampler.yaml)

```yaml
title: 标准HTTP取样器
testclass: http # 取样器类型
config: # 取样器配置
  method: post   # 请求方法
  protocol: http   # 请求协议，默认 http
  http/2: false # 是否 http2， ture、false，可空，默认 false
  port: 8080   # 请求端口，默认 80
  host: localhost  # 服务器地址
  headers: # 请求头，可空
    h1: 1
  path: /user   # 接口path
  query: { } # url中的参数，如: ?id=1&name=t {id: 1, name: t}
  data: { } # 请求data
  body: { userName: 'ryze', password: '123456qq' } # 请求body 优先级高于 data
```

# 常见问题

1. 当一个测试集合内存在多个 HTTP默认配置时，多个 HTTP默认配置中的配置会合并，相同配置项以后定义的 HTTP默认配置的值为准。
2. 当取样器中的 HTTP配置项 与 HTTP默认配置中的配置项重复时，以取样器中的配置项的值为准