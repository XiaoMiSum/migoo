package coder.xyz.migoo

import coder.xyz.migoo.protocol.Protocol

class Samplers extends El {

    private Samplers(String title, String testClass, Map variables, El config, List validators, List preprocessors,
                     List postprocessors, List extractors) {
        super(testClass)
        p("title", title)
        p("variables", variables)
        p("config", config.customize())
        p("validators", validators)
        p("preprocessors", preprocessors)
        p("postprocessors", postprocessors)
        p("extractors", extractors)
    }

    static Samplers sampler(String title, Map<String, Object> variables = null, Protocol.HTTP config, List validators = null,
                            List preprocessors = null, List postprocessors = null, List extractors = null) {
        return new Samplers(title, "HTTPSampler", variables, config, validators, preprocessors, postprocessors, extractors)
    }

    static Samplers sampler(String title, Map<String, Object> variables = null, Protocol.JDBC config, List validators = null,
                            List preprocessors = null, List postprocessors = null, List extractors = null) {
        return new Samplers(title, "JDBCSampler", variables, config, validators, preprocessors, postprocessors, extractors)
    }

    static Samplers sampler(String title, Map<String, Object> variables = null, Protocol.Redis config, List validators = null,
                            List preprocessors = null, List postprocessors = null, List extractors = null) {
        return new Samplers(title, "RedisSampler", variables, config, validators, preprocessors, postprocessors, extractors)
    }

    static Samplers sampler(String title, Map<String, Object> variables = null, Protocol.Dubbo config, List validators = null,
                            List preprocessors = null, List postprocessors = null, List extractors = null) {
        return new Samplers(title, "DubboSampler", variables, config, validators, preprocessors, postprocessors, extractors)
    }

    static Samplers sampler(String title, Map<String, Object> variables = null, Protocol.ActiveMQ config, List validators = null,
                            List preprocessors = null, List postprocessors = null, List extractors = null) {
        return new Samplers(title, "ActiveMQSampler", variables, config, validators, preprocessors, postprocessors, extractors)
    }

    static Samplers sampler(String title, Map<String, Object> variables = null, Protocol.Kafka config, List validators = null,
                            List preprocessors = null, List postprocessors = null, List extractors = null) {
        return new Samplers(title, "KafkaSampler", variables, config, validators, preprocessors, postprocessors, extractors)
    }
}
