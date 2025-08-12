package io.github.xiaomisum.ryze.mongo.example.code

import io.github.xiaomisum.ryze.protocol.mongo.MongoMagicBox
import io.github.xiaomisum.ryze.protocol.mongo.builder.MongoConfigureElementsBuilder
import io.github.xiaomisum.ryze.protocol.mongo.builder.MongoPostprocessorsBuilder
import io.github.xiaomisum.ryze.protocol.mongo.builder.MongoPreprocessorsBuilder
import io.github.xiaomisum.ryze.protocol.mongo.builder.MongoSamplersBuilder
import io.github.xiaomisum.ryze.support.testng.annotation.RyzeTest
import org.testng.annotations.Test

class GroovyCodeTestCase {


    @Test
    @RyzeTest
    void test1() {
        MongoMagicBox.suite("测试用例", {
            variables("id", 1)
            variables { put("tick", "ryze") }
            variables Map.of("a", 1, "b", 2)
            configureElements(MongoConfigureElementsBuilder.class, {
                mongo {
                    config {
                        url "127.0.0.1:9092"
                        database "ryze.topic"
                        collection "ryze.topic"
                    }
                }
            })
            preprocessors(MongoPreprocessorsBuilder.class, {
                mongo {
                    config {
                        action "insert"
                        dataMap(data -> {
                            data.put("username", "\${tick}")
                        })
                    }
                }
            })
            postprocessors(MongoPostprocessorsBuilder.class, {
                mongo {
                    config {
                        message "test1: mongo_postprocessor_test "
                    }
                }
            })
            children(MongoSamplersBuilder.class, {
                mongo {
                    title "步骤1"
                    config {
                        message Map.of("name", "\${tick}  步骤1：标准mongo取样器")
                    }
                }
            })
            children(MongoSamplersBuilder.class, {
                mongo {
                    title "步骤2"
                    config {
                        message Map.of("name", "\${tick}  步骤2：标准mongo取样器")
                    }
                }
            })
        })
    }

    @Test
    @RyzeTest
    void test2() {
        MongoMagicBox.mongo("测试用例- test2()", sampler -> {
            configureElements(MongoConfigureElementsBuilder.class, {
                mongo {
                    config {
                        url "127.0.0.1:9092"
                        database "ryze.topic"
                        collection "ryze.topic"
                    }
                }
            })
            preprocessors(MongoPreprocessorsBuilder.class, {
                mongo {
                    config {
                        message(TestObj.class, test -> {
                            test.setName("hahaha")
                        })
                    }
                }
            })
            config {
                message 123456
            }
        })
    }


    @Test
    @RyzeTest
    void test3() {
        MongoMagicBox.mongo({
            title "步骤1——插入用户：tick = redis_preprocessor"
            configureElements(MongoConfigureElementsBuilder.class, {
                mongo {
                    config {
                        url "127.0.0.1:9092"
                        database "ryze.topic"
                        collection "ryze.topic"
                    }
                }
            })
            config {
                message([1, 2, 3, 4, 5])
            }
        })

        MongoMagicBox.mongo({
            title "步骤2——查找用户：tick = ryze_http_sampler"
            configureElements(MongoConfigureElementsBuilder.class, {
                mongo {
                    config {
                        url "127.0.0.1:9092"
                        database "ryze.topic"
                        collection "ryze.topic"
                    }
                }
            })
            config {
                message true
            }
        })
    }
}
