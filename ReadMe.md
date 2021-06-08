<!-- Add banner here -->

# MiGoo(米果)

<!-- Add buttons here -->

[![License](http://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/XiaoMiSum/MiGoo/blob/master/LICENSE)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/xyz.migoo/migoo/badge.svg)](https://maven-badges.herokuapp.com/maven-central/xyz.migoo/migoo)
[![Author](https://img.shields.io/badge/Author-xiaomi-yellow.svg)](https://github.com/XiaoMiSum)
[![Release](https://img.shields.io/github/release/XiaoMiSum/migoo.svg)](https://github.com/XiaoMiSum/MiGoo/releases)

<!-- Describe your project in brief -->

migoo 是一个Java语言开发的测试框架，适用于HTTP(S)、Dubbo、JDBC、Redis等协议或工具的测试。

**核心特性**

1.测试用例与代码分离，采用JSON描述测试场景，保障测试用例描述的统一性和可维护性；

2.丰富的测试组件，轻松实现动态参数及复杂业务；

3.丰富的校验机制，灵活配置校验规则；

4.极强的可扩展性，满足个性化扩展需求；

5.简洁美观的测试报告与执行日志；

# 目录

- [项目简介](#MiGoo(米果))
- [目录](#目录)
- [使用](#使用)
- [开发](#开发)
- [鸣谢](#鸣谢)
- [License](#license)

# 使用
[(Back to top)](#目录)

本项目已发布在Maven中央仓库，请在pom.xml中引用

``` xml
<!-- https://mvnrepository.com/artifact/xyz.migoo/migoo -->
<dependency>
    <groupId>xyz.migoo</groupId>
    <artifactId>migoo</artifactId>
    <version>${version}</version>
</dependency>
```

# 示例
[(Back to top)](#目录)

见src/test/java/example/xyz/migoo

# 开发
[(Back to top)](#目录)

sampler开发：请参考 protocol.xyz.migoo包

function开发：请参考 function.xyz.migoo包

断言\提取器等组件开发：请参考 components.xyz.migoo包

### 鸣谢
[(Back to top)](#目录)

[JetBrains IDEA](https://www.jetbrains.com)

# License
[(Back to top)](#目录)

[![License](http://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/XiaoMiSum/MiGoo/blob/master/LICENSE)

The MIT License (MIT)

Copyright (c) 2018. Lorem XiaoMiSum (mi_xiao@qq.com)

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
'Software'), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.