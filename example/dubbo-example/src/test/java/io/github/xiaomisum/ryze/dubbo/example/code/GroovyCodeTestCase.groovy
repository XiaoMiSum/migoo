package io.github.xiaomisum.ryze.dubbo.example.code

import io.github.xiaomisum.ryze.MagicBox
import io.github.xiaomisum.ryze.support.testng.annotation.RyzeTest
import org.testng.annotations.Test

class GroovyCodeTestCase {


    @Test
    @RyzeTest
    void test1() {
        MagicBox.suite("测试用例", {
            variables("id", 1)
            variables { put("tick", "ryze") }
            variables Map.of("a", 1, "b", 2)
            configureElements {
                redis {
                    refName "redis_source"
                    config {
                        url "redis://127.0.0.1:6379/0"
                    }
                }
            }
            children {
                redis {
                    title "步骤1"
                    variables("username", "ryze")
                    config {
                        datasource "redis_source"
                        command "set"
                        send "test_case,\${tick}"
                    }
                }
            }
            children {
                redis {
                    title "步骤2"
                    variables("username", "ryze")
                    config {
                        datasource "redis_source"
                        command "get"
                        send "test_case"
                    }
                    validators {
                        result { expected "\${tick}" }
                    }
                }
            }
        })
    }

    @Test
    @RyzeTest
    void test2() {
        MagicBox.redis("测试用例- test2()", sampler -> {
            configureElements {
                redis {
                    refName "redis_source"
                    config {
                        url "redis://127.0.0.1:6379/0"
                    }
                }
            }
            preprocessors {
                redis {
                    title "前置处理器写入用户"
                    config {
                        datasource "redis_source"
                        command "set"
                        send "test2_redis_preprocessor,test2_redis_preprocessor"
                    }
                }
            }
            config {
                datasource "redis_source"
                command "get"
                send "test2_redis_preprocessor"
            }
            validators {
                result { expected "test2_redis_preprocessor" }
            }
        })
    }


    @Test
    @RyzeTest
    void test3() {
        MagicBox.redis({
            title "步骤1——插入用户：tick = redis_preprocessor"
            configureElements {
                redis {
                    refName "redis_source"
                    config {
                        url "redis://127.0.0.1:6379/0"
                    }
                }
            }
            config {
                datasource "redis_source"
                command "set"
                send "test3_redis_sampler,test3_redis_sampler"
            }
        })

        MagicBox.redis({
            title "步骤2——查找用户：tick = ryze_http_sampler"
            configureElements {
                redis {
                    refName "redis_source"
                    config {
                        url "redis://127.0.0.1:6379/0"
                    }
                }
            }
            config {
                datasource "redis_source"
                command "get"
                send "test3_redis_sampler"
            }
            assertions {
                result { expected "test3_redis_sampler" }
            }
        })
    }
}
