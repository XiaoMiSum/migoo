# JDBC 协议

## 配置元件 [示例](../template/配置元件/jdbc_datasource.yaml)

JDBC 数据源：使用该组件配置 JDBC数据源，用于 JDBC处理器\取样器引用。

```yaml
# jdbc取样器、处理器必须引用一个jdbc数据源
testclass: jdbc # 配置元件类型
ref_name: JDBCDataSource_var  # 数据源名称
config: # 可简化填写，无需config关键字，直接将配置内容至于首层
  driver: com.mysql.cj.bc.Driver # jdbc驱动，jdbc版本没支持 SPI时必填
  url: 'jdbc:mysql://127.0.0.1:3306/db-template?characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2b8&failOverReadOnly=false'
  username: 'root'
  password: '123456'
  max_active: '10'
  max_wait: '60000'
```

## 处理器

### 前置处理器 [示例](../template/处理器/jdbc_preprocessor.yaml)

```yaml
testclass: jdbc  # jdbc 前置处理器类型
config: # 处理器配置
  datasource: JDBCDataSource_var  # 数据源，必须先定义数据源
  statement: 'select * from sys_user where id = 1;'  # sql语句
```

### 后置处理器 [示例](../template/处理器/jdbc_postprocessor.yaml)

```yaml
testclass: jdbc_postprocessor  # jdbc 后置处理器类型
config: # 处理器配置
  datasource: JDBCDataSource_var  # 数据源，必须先定义数据源
  statement: 'select * from sys_user where id = 1;'  # sql语句
```

## 取样器 [示例](../template/取样器/jdbc_sampler.yaml)

```yaml
title: 标准jdbc取样器
testclass: jdbc  # 取样器类型
config: # 取样器配置
  datasource: JDBCDataSource_var  # 数据源，必须先定义数据源
  statement: 'select * from sys_user where id = 1;'  # sql语句
```
