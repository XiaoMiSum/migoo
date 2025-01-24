package coder.xyz.migoo;

import coder.xyz.migoo.protocol.*;

public class Processors {

    /**
     * http 前置处理器
     *
     * @param config     http 配置
     * @param extractors 提取器
     * @return http 前置处理器
     */
    public static Pre preprocessor(HTTP config, Extractors... extractors) {
        return withPre("http_pre_processor", config, extractors);
    }

    /**
     * http 后置处理器
     *
     * @param config     http 配置
     * @param extractors 提取器
     * @return http 后置处理器
     */
    public static Post postprocessor(HTTP config, Extractors... extractors) {
        return withPost("http_post_processor", config, extractors);
    }

    /**
     * jdbc 前置处理器
     *
     * @param config     jdbc 配置
     * @param extractors 提取器
     * @return jdbc 前置处理器
     */
    public static Pre preprocessor(JDBC config, Extractors... extractors) {
        return withPre("jdbc_pre_processor", config, extractors);
    }

    /**
     * jdbc 后置处理器
     *
     * @param config     jdbc 配置
     * @param extractors 提取器
     * @return jdbc 后置处理器
     */
    public static Post postprocessor(JDBC config, Extractors... extractors) {
        return withPost("jdbc_post_processor", config, extractors);
    }

    /**
     * redis 前置处理器
     *
     * @param config     redis 配置
     * @param extractors 提取器
     * @return redis 前置处理器
     */
    public static Pre preprocessor(Redis config, Extractors... extractors) {
        return withPre("redis_pre_processor", config, extractors);
    }

    /**
     * redis 后置处理器
     *
     * @param config     redis 配置
     * @param extractors 提取器
     * @return redis 后置处理器
     */
    public static Post postprocessor(Redis config, Extractors... extractors) {
        return withPost("redis_post_processor", config, extractors);
    }

    /**
     * dubbo 前置处理器
     *
     * @param config     dubbo 配置
     * @param extractors 提取器
     * @return dubbo 前置处理器
     */
    public static Pre preprocessor(Dubbo config, Extractors... extractors) {
        return withPre("dubbo_pre_processor", config, extractors);
    }

    /**
     * dubbo 后置处理器
     *
     * @param config     dubbo 配置
     * @param extractors 提取器
     * @return dubbo 后置处理器
     */
    public static Post postprocessor(Dubbo config, Extractors... extractors) {
        return withPost("dubbo_post_processor", config, extractors);
    }

    /**
     * active mq 前置处理器
     *
     * @param config     active mq 配置
     * @param extractors 提取器
     * @return active mq 前置处理器
     */
    public static Pre preprocessor(ActiveMQ config, Extractors... extractors) {
        return withPre("active_mq_pre_processor", config, extractors);
    }

    /**
     * active mq 后置处理器
     *
     * @param config     active mq 配置
     * @param extractors 提取器
     * @return active mq 后置处理器
     */
    public static Post postprocessor(ActiveMQ config, Extractors... extractors) {
        return withPost("active_mq_post_processor", config, extractors);
    }

    /**
     * kafka 前置处理器
     *
     * @param config     kafka 配置
     * @param extractors 提取器
     * @return kafka 前置处理器
     */
    public static Pre preprocessor(Kafka config, Extractors... extractors) {
        return withPre("kafka_pre_processor", config, extractors);
    }

    /**
     * kafka 后置处理器
     *
     * @param config     kafka 配置
     * @param extractors 提取器
     * @return kafka 后置处理器
     */
    public static Post postprocessor(Kafka config, Extractors... extractors) {
        return withPost("kafka_post_processor", config, extractors);
    }

    /**
     * rabbit 前置处理器
     *
     * @param config     rabbit 配置
     * @param extractors 提取器
     * @return kafka 前置处理器
     */
    public static Pre preprocessor(RabbitMQ config, Extractors... extractors) {
        return withPre("rabbit_pre_processor", config, extractors);
    }

    /**
     * rabbit 后置处理器
     *
     * @param config     rabbit 配置
     * @param extractors 提取器
     * @return kafka 后置处理器
     */
    public static Post postprocessor(RabbitMQ config, Extractors... extractors) {
        return withPost("rabbit_post_processor", config, extractors);
    }

    private static Pre withPre(String testClass, El config, Extractors... extractors) {
        return new Pre(testClass, config, extractors);
    }

    private static Post withPost(String testClass, El config, Extractors... extractors) {
        return new Post(testClass, config, extractors);
    }

    public static class Pre extends El {

        private Pre(String testClass, El protocol, Extractors... extractors) {
            super(testClass);
            p("config", protocol.properties());
            p("extractors", extractors);
        }
    }

    public static class Post extends El {

        private Post(String testClass, El protocol, Extractors... extractors) {
            super(testClass);
            p("config", protocol.properties());
            p("extractors", extractors);
        }
    }

}