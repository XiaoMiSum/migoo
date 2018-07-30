模仿 <a href="https://github.com/HttpRunner/HttpRunner">HttpRunner</a>
基于 Junit 4.8、HTTP Client 4.5.5、thymeleaf 3.0.9.RELEASE 的一个测试工具，
可通过指定格式的json文件读取测试数据，执行测试并生成测试报告。

**使用方式**

    @org.junit.Test
    public void testApi(){
        TestResult result = new Runner().run("path","");
    }
    
说明：Runner类提供 run方法，有两个参数

参数 path ：指定的测试用例文件或测试用例文件所在目录；

参数 template：thymeleaf模版文件，可空，传入空值时，按默认模版生成测试报告。

**测试用例**

测试用例为 json 数组，详情见 TestCase/test_case.json

**关于验证**

验证支持的方式有 equals 、contains，会根据validate.types的值动态加载验证方式。

types详细值，请查看配置文件 application.properties

    [
        {
            "name": "the test suite name",
            "request": {
                "url": "http://127.0.0.1:8080/login/login",
                "method": "post",
                "headers": {
                    "Content-Type": "application/x-www-form-urlencoded"
                    }
            },
            "case": [
                {
                    "title": "正确的用户名\\密码登录，返回值包含期望值",
                    "body": {
                        "user": "admin",
                        "pwd": "123456qq"
                    },
                    "validate": [
                        {
                            "check": "statusCode", 
                            "expect": 200, 
                            "types": "=="
                        },
                        {
                            "check": "body", 
                            "expect": "succ", 
                            "types": "contain"
                        }
                    ]
                },                 
                {
                    "title": "错误的用户名\\密码登录，返回值等于期望值",
                    "body": {
                        "user": "admin",
                        "pwd": "123"
                    },
                    "validate":[
                        {
                            "check": "statusCode", 
                            "expect": 200, 
                            "types": "=="
                        },
                        {
                            "check": "body", 
                            "expect": "success", 
                            "types": "eq"
                        }
                    ]
                }
            ]
        }
    ]

**测试报告**

测试报告保存在项目根目录或模块根目录

报告中的数据来源于 TestResult.report，Map类型；

转化为json格式为：

    {
        "summary":{
            "startAt":"2018-07-26 12:00:00",
            "duration":"7.032 seconds",
            "total": 1,
            "success": 1,
            "failed": 1,
            "error": 1,
            "skipped": 1
            },
        "records":{
            "status": "success",
            "name": "test case name",
            "time": "7.032 seconds",
            "detail": {
                "log": {
                    "request": {
                        "url": "http://127.0.0.0",
                        "method": "post",
                        "headers": [Header],
                        "body": "json string"
                    },
                    "response": {
                        "statusCode": 200,
                        "headers": [Header],
                        "body": "json string"
                    }
                },
                "track": "failure log message",
                "track_id": "",
                "log_id": "",
                "track_href": "",
                "log_href": ""
            }
            
        }
    }
    
**Demo**

1.解压data/login.zip，放到tomcat/webapps目录；

2.启动tomcat，如端口不是 8080，请修改 test_case.json中的 url；

3.运行测试类 Test.class；

4.查看测试报告 "./Mi-Goo/reports/xxxxx-2018-07-26 13:29:30.html"

