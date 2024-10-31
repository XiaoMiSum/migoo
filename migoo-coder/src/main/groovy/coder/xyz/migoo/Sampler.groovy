package coder.xyz.migoo

import coder.xyz.migoo.protocol.Protocol

class Sampler extends El {

    private Sampler(String title, String testClass, Map variables, El config, Object[] validators,
                    Object[] preprocessors, Object[] postprocessors, Object[] extractors) {
        super(testClass)
        p("title", title)
        p("variables", variables)
        p("config", config.customize())
        p("validators", validators)
        p("preprocessors", preprocessors)
        p("postprocessors", postprocessors)
        p("extractors", extractors)
    }

    static Sampler sampler(String title, Map<String, Object> variables = null, Protocol.HTTP config,
                           Object[] validators = null, Object[] preprocessors = null,
                           Object[] postprocessors = null, Object[] extractors = null) {
        return new Sampler(title, "httpSampler", variables, config, validators, preprocessors, postprocessors, extractors)
    }

    static Sampler sampler(String title, Map<String, Object> variables = null, Protocol.JDBC config,
                           Object[] validators = null, Object[] preprocessors = null,
                           Object[] postprocessors = null, Object[] extractors = null) {
        return new Sampler(title, "JDBCSampler", variables, config, validators, preprocessors, postprocessors, extractors)
    }


    static Sampler sampler(String title, Map<String, Object> variables = null, Protocol.Redis config,
                           Object[] validators = null, Object[] preprocessors = null,
                           Object[] postprocessors = null, Object[] extractors = null) {
        return new Sampler(title, "RedisSampler", variables, config, validators, preprocessors, postprocessors, extractors)
    }
}
