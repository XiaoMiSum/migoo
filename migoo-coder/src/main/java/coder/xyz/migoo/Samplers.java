package coder.xyz.migoo;

import coder.xyz.migoo.protocol.*;

import java.util.HashMap;
import java.util.Map;

public class Samplers extends El {

    private Samplers(String title, String testClass, Map<String, Object> variables, El config, Validators[] validators,
                     Processors.Pre[] preprocessors, Processors.Post[] postprocessors, Extractors[] extractors) {
        super(testClass);
        p("title", title);
        p("variables", variables);
        p("config", config.properties());
        p("validators", validators);
        p("preprocessors", preprocessors);
        p("postprocessors", postprocessors);
        p("extractors", extractors);
    }

    /**
     * 最简单的取样器配置
     *
     * @param title      描述
     * @param config     http 配置
     * @param validators 验证器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, HTTP config, Validators... validators) {
        return new Samplers(title, "http_sampler", new HashMap<>(), config, validators, null, null, null);
    }

    /**
     * 有变量的取样器
     *
     * @param title      描述
     * @param config     http 配置
     * @param variables  变量
     * @param validators 验证器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, HTTP config, Map<String, Object> variables, Validators... validators) {
        return new Samplers(title, "http_sampler", variables, config, validators, null, null, null);
    }

    /**
     * 最简单的取样器配置
     *
     * @param title      描述
     * @param config     jdbc 配置
     * @param validators 验证器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, JDBC config, Validators... validators) {
        return new Samplers(title, "jdbc_sampler", new HashMap<>(), config, validators, null, null, null);
    }

    /**
     * 有变量的取样器
     *
     * @param title      描述
     * @param config     jdbc 配置
     * @param variables  变量
     * @param validators 验证器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Map<String, Object> variables, JDBC config, Validators... validators) {
        return new Samplers(title, "jdbc_sampler", variables, config, validators, null, null, null);
    }

    /**
     * 最简单的取样器配置
     *
     * @param title      描述
     * @param config     redis 配置
     * @param validators 验证器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Redis config, Validators... validators) {
        return new Samplers(title, "redis_sampler", new HashMap<>(), config, validators, null, null, null);
    }

    /**
     * 有变量的取样器
     *
     * @param title      描述
     * @param config     redis 配置
     * @param variables  变量
     * @param validators 验证器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Map<String, Object> variables, Redis config, Validators... validators) {
        return new Samplers(title, "redis_sampler", variables, config, validators, null, null, null);
    }

    /**
     * 最简单的取样器配置
     *
     * @param title      描述
     * @param config     dubbo 配置
     * @param validators 验证器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Dubbo config, Validators... validators) {
        return new Samplers(title, "dubbo_sampler", new HashMap<>(), config, validators, null, null, null);
    }

    /**
     * 有变量的取样器
     *
     * @param title      描述
     * @param config     dubbo 配置
     * @param variables  变量
     * @param validators 验证器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Map<String, Object> variables, Dubbo config, Validators... validators) {
        return new Samplers(title, "dubbo_sampler", variables, config, validators, null, null, null);
    }

    /**
     * 最简单的取样器配置
     *
     * @param title      描述
     * @param config     active_mq 配置
     * @param validators 验证器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, ActiveMQ config, Validators... validators) {
        return new Samplers(title, "active_mq_sampler", new HashMap<>(), config, validators, null, null, null);
    }

    /**
     * 有变量的取样器
     *
     * @param title      描述
     * @param config     active_mq 配置
     * @param variables  变量
     * @param validators 验证器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Map<String, Object> variables, ActiveMQ config, Validators... validators) {
        return new Samplers(title, "active_mq_sampler", variables, config, validators, null, null, null);
    }

    /**
     * 最简单的取样器配置
     *
     * @param title      描述
     * @param config     kafka 配置
     * @param validators 验证器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Kafka config, Validators... validators) {
        return new Samplers(title, "kafka_sampler", new HashMap<>(), config, validators, null, null, null);
    }

    /**
     * 有变量的取样器
     *
     * @param title      描述
     * @param config     kafka 配置
     * @param variables  变量
     * @param validators 验证器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Map<String, Object> variables, Kafka config, Validators... validators) {
        return new Samplers(title, "kafka_sampler", variables, config, validators, null, null, null);
    }

    /**
     * 最简单的取样器配置
     *
     * @param title      描述
     * @param config     rabbit_mq 配置
     * @param validators 验证器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, RabbitMQ config, Validators... validators) {
        return new Samplers(title, "rabbit_mq_sampler", null, config, validators, null, null, null);
    }

    /**
     * 有变量的取样器
     *
     * @param title      描述
     * @param variables  变量
     * @param config     rabbit_mq 配置
     * @param validators 验证器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Map<String, Object> variables, RabbitMQ config, Validators... validators) {
        return new Samplers(title, "rabbit_mq_sampler", variables, config, validators, null, null, null);
    }

    /**
     * 设置取样器的前置处理器
     *
     * @param preprocessors 前置处理器
     * @return 当前取样器
     */
    public Samplers preprocessors(Processors.Pre... preprocessors) {
        p("preprocessors", preprocessors);
        return this;
    }

    /**
     * 设置取样器的后置处理器
     *
     * @param postprocessors 后置处理器
     * @return 当前取样器
     */
    public Samplers postprocessors(Processors.Post... postprocessors) {
        p("postprocessors", postprocessors);
        return this;
    }

    /**
     * 设置取样器的提取器
     *
     * @param extractors 提取器
     * @return 当前取样器
     */
    public Samplers extractors(Extractors... extractors) {
        p("extractors", extractors);
        return this;
    }

}
