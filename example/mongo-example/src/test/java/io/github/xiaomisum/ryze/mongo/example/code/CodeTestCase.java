package io.github.xiaomisum.ryze.mongo.example.code;

import io.github.xiaomisum.ryze.protocol.mongo.MongoMagicBox;
import io.github.xiaomisum.ryze.protocol.mongo.builder.MongoConfigureElementsBuilder;
import io.github.xiaomisum.ryze.protocol.mongo.builder.MongoPostprocessorsBuilder;
import io.github.xiaomisum.ryze.protocol.mongo.builder.MongoPreprocessorsBuilder;
import io.github.xiaomisum.ryze.protocol.mongo.builder.MongoSamplersBuilder;
import io.github.xiaomisum.ryze.support.testng.annotation.RyzeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeTestCase {

    @Test
    @RyzeTest
    public void test1() {
        MongoMagicBox.suite("测试用例", suite -> {
            suite.variables("id", 1);
            suite.variables(var -> var.put("tick", "mongo_preprocessor"));
            suite.variables(Map.of("a", 1, "b", 2));
            suite.configureElements(MongoConfigureElementsBuilder.class, builder -> builder
                    .mongo(mongo -> mongo.config(config -> config.bootstrapServers("127.0.0.1:9092").topic("ryze.topic").key("ryze")))
            );
            suite.preprocessors(MongoPreprocessorsBuilder.class, builder -> builder
                    .mongo(mongo -> mongo.config(config -> config.message(HashMap.class, message -> {
                        message.put("username", "${tick}");
                        message.put("age", 18);
                    })))
            );
            suite.postprocessors(MongoPostprocessorsBuilder.class, builder -> builder
                    .mongo(mongo -> mongo.config(config -> config.message("test1: mongo_postprocessor_test ")))
            );
            suite.children(MongoSamplersBuilder.class, builder -> builder
                    .mongo(mongo -> mongo.title("步骤1").variables("username", "ryze")
                            .config(config -> config.message(Map.of("name", "${username}  步骤1：标准mongo取样器"))))
            );
            suite.children(MongoSamplersBuilder.builder()
                    .mongo(mongo -> mongo.title("步骤2").variables("username", "ryze")
                            .config(config -> config.message(Map.of("name", "${username}  步骤2：标准mongo取样器"))))
                    .build());
        });
    }

    @Test
    @RyzeTest
    public void test2() {
        MongoMagicBox.mongo("测试用例- test2()", sampler -> {
            sampler.configureElements(MongoConfigureElementsBuilder.class, builder -> builder
                    .mongo(mongo -> mongo.config(config -> config.bootstrapServers("127.0.0.1:9092").topic("ryze.topic").key("ryze")))
            );
            sampler.preprocessors(MongoPreprocessorsBuilder.class, builder -> builder
                    .mongo(mongo -> mongo.config(config -> config.message(HashMap.class, message -> {
                        message.put("username", "${tick}");
                        message.put("age", 18);
                    })))
            );
            sampler.config(config -> config.message(123456));
        });
    }


    @Test
    @RyzeTest
    public void test3() {
        MongoMagicBox.mongo("测试用例- test3()-1", sampler -> {
            sampler.configureElements(MongoConfigureElementsBuilder.class, builder -> builder
                    .mongo(mongo -> mongo.config(config -> config.bootstrapServers("127.0.0.1:9092").topic("ryze.topic").key("ryze")))
            );
            sampler.config(config -> config.message(List.of(1, 2, 3, 4)));
        });


        MongoMagicBox.mongo("测试用例- test3()-2", sampler -> {
            sampler.configureElements(MongoConfigureElementsBuilder.class, builder -> builder
                    .mongo(mongo -> mongo.config(config -> config.bootstrapServers("127.0.0.1:9092").topic("ryze.topic").key("ryze")))
            );
            sampler.config(config -> config.message(true));
        });
    }
}
