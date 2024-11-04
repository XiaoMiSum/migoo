package coder.xyz.migoo

import coder.xyz.migoo.protocol.Protocol

class Processor extends El {

    private Processor(String testClass, El protocol, Extractor... extractors) {
        super(testClass)
        p("config", protocol.customize())
        p("extractors", extractors)
    }

    static Processor preprocessor(Protocol.HTTP config, Extractor... extractors) {
        return with("HttpPreProcessor", config, extractors)
    }

    static Processor postprocessor(Protocol.HTTP config, Extractor... extractors) {
        return with("HttpPostProcessor", config, extractors)
    }

    static Processor preprocessor(Protocol.Redis config, Extractor... extractors) {
        return with("RedisPreProcessor", config, extractors)
    }

    static Processor postprocessor(Protocol.Redis config, Extractor... extractors) {
        return with("RedisPostProcessor", config, extractors)
    }

    static Processor preprocessor(Protocol.JDBC config, Extractor... extractors) {
        return with("JDBCPreProcessor", config, extractors)
    }

    static Processor postprocessor(Protocol.JDBC config, Extractor... extractors) {
        return with("JDBCPostProcessor", config, extractors)
    }

    static Processor preprocessor(Protocol.Dubbo config, Extractor... extractors) {
        return with("DubboPreProcessor", config, extractors)
    }

    static Processor postprocessor(Protocol.Dubbo config, Extractor... extractors) {
        return with("DubboPostProcessor", config, extractors)
    }

    private static Processor with(String testClass, El config, Extractor... extractors) {
        return new Processor(testClass, config, extractors)
    }

}