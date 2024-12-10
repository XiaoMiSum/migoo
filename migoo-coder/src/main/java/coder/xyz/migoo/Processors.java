package coder.xyz.migoo;

import coder.xyz.migoo.protocol.*;

public class Processors extends El {

    private Processors(String testClass, El protocol, Extractors... extractors) {
        super(testClass);
        p("config", protocol.properties());
        p("extractors", extractors);
    }

    public static Processors preprocessor(HTTP config, Extractors... extractors) {
        return with("http_pre_processor", config, extractors);
    }

    public static Processors postprocessor(HTTP config, Extractors... extractors) {
        return with("http_post_processor", config, extractors);
    }

    public static Processors preprocessor(Redis config, Extractors... extractors) {
        return with("redis_pre_processor", config, extractors);
    }

    public static Processors postprocessor(Redis config, Extractors... extractors) {
        return with("redis_post_processor", config, extractors);
    }

    public static Processors preprocessor(JDBC config, Extractors... extractors) {
        return with("jdbc_pre_processor", config, extractors);
    }

    public static Processors postprocessor(JDBC config, Extractors... extractors) {
        return with("jdbc_post_processor", config, extractors);
    }

    public static Processors preprocessor(Dubbo config, Extractors... extractors) {
        return with("dubbo_pre_processor", config, extractors);
    }

    public static Processors postprocessor(Dubbo config, Extractors... extractors) {
        return with("dubbo_post_processor", config, extractors);
    }

    public static Processors preprocessor(ActiveMQ config, Extractors... extractors) {
        return with("active_mq_pre_processor", config, extractors);
    }

    public static Processors postprocessor(ActiveMQ config, Extractors... extractors) {
        return with("active_mq_post_processor", config, extractors);
    }

    public static Processors preprocessor(Kafka config, Extractors... extractors) {
        return with("kafka_pre_processor", config, extractors);
    }

    public static Processors postprocessor(Kafka config, Extractors... extractors) {
        return with("kafka_post_processor", config, extractors);
    }

    private static Processors with(String testClass, El config, Extractors... extractors) {
        return new Processors(testClass, config, extractors);
    }

}