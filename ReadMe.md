参考 <a href="https://github.com/HttpRunner/HttpRunner">HttpRunner</a>
使用Java语言，基于Junit 4.8、HTTP Client 4.5.5、thymeleaf 3.0.9.RELEASE等开源项目实现的一个接口测试工具，
可通过指定格式的json\yaml文件读取测试数据，执行测试并生成测试报告。

**使用方式**

    @org.junit.Test
    public void testApi(){
        TestResult result = Runner.getInstance().run("path");
    }
    
    
说明：Runner类提供 run 方法作为执行入口

参数 path ：指定的测试用例文件、测试用例文件所在目录、指定格式的json文本；

**测试用例**

测试用例为 json 或 json数组，详情见 TestCase/test_case.json、TestCase/test_case2.json、TestCase/test_case.yml

**关于验证**

验证支持的方式有 equals 、contains、isEmpty、isNotEmpty、regex，会根据validate.types的值动态加载验证方式。

types详细值，请查看配置文件 application.properties

**测试报告**

测试报告为HTML文件，使用 thymeleaf模板生成；

报告中的数据来源于 TestResult.report，Map类型；

    
**Demo**

TestCase目录中提供了3个测试用例文件，分别提供了基础\进阶\高阶的示例。

1.运行测试类 Test.class；

2.查看测试报告 "./Reports/xxxxx-2018-07-26 13:29:30.html"；

**JDK**
请在 JDK 1.8 及以上版本中运行或开发测试脚本；