# 运行环境

java 21 及以上，并正确配置java环境变量

# 操作说明

>

    options:
        -f: 测试用例文件路径，如：/user/migoo/testcase.yaml
        -r: 测试报告保存路径，如：/user/migoo/report
        -h2m: har文件路径，将指定的har文件转换为标准Http取样器文件
        -p2m: postman文件路径，将指定的postman文件(v2.1)转换为标准Http取样器文件
        -h: 帮助信息
        
        example: ./migoo.sh -f ./example/example_project.yaml -r ./report

# 执行准备

>

    必须：启动mysql，并创建数据库，执行 example.sql，修改configelements.yaml 中的JDBC配置
    如果想体验Redis、Dubbo取样器，则需要另外开启Redis服务、Zookeeper、Dubbo官方示例，并取消 example_testcase.yaml、configelements.yaml中的注释