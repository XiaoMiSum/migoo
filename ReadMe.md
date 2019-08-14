# MiGoo

[![License](http://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/XiaoMiSum/MiGoo/blob/master/LICENSE)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/xyz.migoo/migoo/badge.svg)](https://maven-badges.herokuapp.com/maven-central/xyz.migoo/migoo)
[![MiGoo Author](https://img.shields.io/badge/Author-xiaomi-yellow.svg)](https://maven-badges.herokuapp.com/maven-central/xyz.migoo/migoo)
[![GitHub release](https://img.shields.io/github/release/XiaoMiSum/migoo.svg)](https://github.com/XiaoMiSum/MiGoo/releases)
## 1. 介绍

MiGoo 是一个Java语言开发的测试工具，可用于接口测试脚本开发。
    
**核心特性**

1.数据驱动，采用 YAML/JSON 描述测试场景，保障测试用例描述的统一性和可维护性；

2.支持扩展函数和自定义变量，可实现复杂的业务逻辑；

3.支持测试前后的环境准备和数据清理（有限支持）；

4.丰富的校验机制，包括：一致(忽略大小写)、不一致、大于、大于等于、小于、小于等于、包含、不包含、空、非空、正则表达式等校验机制，并支持自定义扩展；

5.简洁明了的测试报告与执行日志；

6.支持jenkins持续集成（通过maven运行junit测试类）；

[详细说明](http://note.youdao.com/noteshare?id=568901613e4f36cfb23af2413e36fd09 "详细说明")
 
## 2. 其他

现已上传Maven中央仓库，请在pom.xml中引用

``` xml
<!-- https://mvnrepository.com/artifact/xyz.migoo/migoo -->
<dependency>
    <groupId>xyz.migoo</groupId>
    <artifactId>migoo</artifactId>
    <version>1.2.0</version>
</dependency>
```
