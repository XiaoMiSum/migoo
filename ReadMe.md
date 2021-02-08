# MiGoo (米果)

[![License](http://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/XiaoMiSum/MiGoo/blob/master/LICENSE)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/xyz.migoo/migoo/badge.svg)](https://maven-badges.herokuapp.com/maven-central/xyz.migoo/migoo)
[![MiGoo Author](https://img.shields.io/badge/Author-xiaomi-yellow.svg)](https://github.com/XiaoMiSum)
[![GitHub release](https://img.shields.io/github/release/XiaoMiSum/migoo.svg)](https://github.com/XiaoMiSum/MiGoo/releases)
[![JETBRAINS IDEA](https://img.shields.io/badge/JETBRAINS-IDEA-yellowgreen)](https://www.jetbrains.com/?from=migoo)
## 1. 介绍

MiGoo 是一个Java语言开发的测试工具，可用于接口测试脚本开发。
    
**核心特性**

1.支持API接口的多种请求方法，包括 GET/POST/HEAD/PUT/DELETE 等；

2.测试用例与代码分离，采用 YAML/JSON 描述测试场景，保障测试用例描述的统一性和可维护性；

3.支持测试前后的环境准备与数据清理；

4.极强的可扩展性，轻松实现动态参数务及复杂业；

5.丰富的校验机制，灵活配置校验规则；

6.简洁明了的测试报告与执行日志；

7.支持jenkins持续集成（通过maven运行junit测试类）；

[详细说明](http://note.youdao.com/noteshare?id=568901613e4f36cfb23af2413e36fd09 "详细说明")
 
## 2. 其他

现已上传Maven中央仓库，请在pom.xml中引用

``` xml
<!-- https://mvnrepository.com/artifact/xyz.migoo/migoo -->
<dependency>
    <groupId>xyz.migoo</groupId>
    <artifactId>migoo</artifactId>
    <version>最新版本</version>
</dependency>
```

## 3. 感谢

感谢 jetbrains 提供开发工具支持