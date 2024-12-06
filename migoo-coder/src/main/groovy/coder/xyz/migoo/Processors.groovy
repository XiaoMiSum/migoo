package coder.xyz.migoo

import coder.xyz.migoo.protocol.Protocol

class Processors extends El {

    private Processors(String testClass, El protocol, Extractors... extractors) {
        super(testClass)
        p("config", protocol.customize())
        p("extractors", extractors)
    }

    static Processors preprocessor(Protocol.HTTP config, Extractors... extractors) {
        return with("HttpPreProcessor", config, extractors)
    }

    static Processors postprocessor(Protocol.HTTP config, Extractors... extractors) {
        return with("HttpPostProcessor", config, extractors)
    }

    static Processors preprocessor(Protocol.Redis config, Extractors... extractors) {
        return with("RedisPreProcessor", config, extractors)
    }

    static Processors postprocessor(Protocol.Redis config, Extractors... extractors) {
        return with("RedisPostProcessor", config, extractors)
    }

    static Processors preprocessor(Protocol.JDBC config, Extractors... extractors) {
        return with("JDBCPreProcessor", config, extractors)
    }

    static Processors postprocessor(Protocol.JDBC config, Extractors... extractors) {
        return with("JDBCPostProcessor", config, extractors)
    }

    static Processors preprocessor(Protocol.Dubbo config, Extractors... extractors) {
        return with("DubboPreProcessor", config, extractors)
    }

    static Processors postprocessor(Protocol.Dubbo config, Extractors... extractors) {
        return with("DubboPostProcessor", config, extractors)
    }

    static Processors preprocessor(Protocol.ActiveMQ config, Extractors... extractors) {
        return with("ActiveMQPreprocessor", config, extractors)
    }

    static Processors postprocessor(Protocol.ActiveMQ config, Extractors... extractors) {
        return with("ActiveMQPostprocessor", config, extractors)
    }

    static Processors preprocessor(Protocol.Kafka config, Extractors... extractors) {
        return with("KafkaPreprocessor", config, extractors)
    }

    static Processors postprocessor(Protocol.Kafka config, Extractors... extractors) {
        return with("KafkaPostprocessor", config, extractors)
    }

    private static Processors with(String testClass, El config, Extractors... extractors) {
        return new Processors(testClass, config, extractors)
    }

}