package coder.xyz.migoo

import coder.xyz.migoo.protocol.Protocol

class Processor extends El {

    private Processor(String testClass, El protocol, Extractor... extractors) {
        super(testClass)
        p(protocol.customize())
        p("extractors", extractors)
    }

    static Processor withHttpPreprocessor(Protocol.HTTP config, Extractor... extractors) {
        return with("HttpPreProcessor", config, extractors)
    }

    static Processor withHttpPostprocessor(Protocol.HTTP config, Extractor... extractors) {
        return with("HttpPostProcessor", config, extractors)
    }

    static Processor withRedisPreprocessor(Protocol.Redis config, Extractor... extractors) {
        return with("RedisPreProcessor", config, extractors)
    }

    static Processor withRedisPostprocessor(Protocol.Redis config, Extractor... extractors) {
        return with("RedisPostProcessor", config, extractors)
    }

    static Processor withJDBCPreprocessor(Protocol.JDBC config, Extractor... extractors) {
        return with("JDBCPreProcessor", config, extractors)
    }

    static Processor withJDBCPostprocessor(Protocol.JDBC config, Extractor... extractors) {
        return with("JDBCPostProcessor", config, extractors)
    }

    static Processor withDubboPreprocessor(Protocol.Dubbo config, Extractor... extractors) {
        return with("DubboPreProcessor", config, extractors)
    }

    static Processor withDubboPostprocessor(Protocol.Dubbo config, Extractor... extractors) {
        return with("DubboPostProcessor", config, extractors)
    }

    private static Processor with(String testClass, El config, Extractor... extractors) {
        return new Processor(testClass, config, extractors)
    }

}