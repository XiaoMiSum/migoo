package coder.xyz.migoo

import coder.xyz.migoo.protocol.Protocol

class Sampler extends El {

    private Sampler(String title, String testClass, Map<String, Object> variables, El config, Validator[] validators,
                    Processor[] preprocessors, Processor[] postprocessors, Extractor[] extractors) {
        super(testClass)
        p("title", title)
        p("variables", variables)
        p("config", config.customize())
        p("validators", validators)
        p("preprocessors", preprocessors)
        p("postprocessors", postprocessors)
        p("extractors", extractors)
    }


    static Sampler withHttpSampler(String title, Map<String, Object> variables, Protocol.HTTP config, Validator[] validators,
                                   Extractor[] extractors) {
        return withHttpSampler(title, variables, config, validators, null, null, extractors)
    }

    static Sampler withHttpSampler(String title, Map<String, Object> variables = null, Protocol.HTTP config,
                                   Validator[] validators = null, Processor[] preprocessors = null,
                                   Processor[] postprocessors = null, Extractor[] extractors = null) {
        return new Sampler(title, "httpSampler", variables, config, validators, preprocessors, postprocessors, extractors)
    }

    static Sampler withJDBCSampler(String title, Map<String, Object> variables, Protocol.JDBC config, Validator[] validators,
                                   Extractor[] extractors) {
        return withJDBCSampler(title, variables, config, validators, null, null, extractors)
    }

    static Sampler withJDBCSampler(String title, Map<String, Object> variables = null, Protocol.JDBC config,
                                   Validator[] validators = null, Processor[] preprocessors = null,
                                   Processor[] postprocessors = null, Extractor[] extractors = null) {
        return new Sampler(title, "JDBCSampler", variables, config, validators, preprocessors, postprocessors, extractors)
    }

    static Sampler withRedisSampler(String title, Map<String, Object> variables, Protocol.Redis config, Validator[] validators,
                                    Extractor[] extractors) {
        return withRedisSampler(title, variables, config, validators, null, null, extractors)
    }

    static Sampler withRedisSampler(String title, Map<String, Object> variables = null, Protocol.Redis config,
                                    Validator[] validators = null, Processor[] preprocessors = null,
                                    Processor[] postprocessors = null, Extractor[] extractors = null) {
        return new Sampler(title, "RedisSampler", variables, config, validators, preprocessors, postprocessors, extractors)
    }
}
