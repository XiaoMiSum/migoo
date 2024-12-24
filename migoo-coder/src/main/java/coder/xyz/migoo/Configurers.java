package coder.xyz.migoo;

import coder.xyz.migoo.protocol.*;

public class Configurers extends El {

    private Configurers(String testClass, El protocol) {
        super(testClass);
        p("config", protocol.properties());
    }

    /**
     * http 默认配置 配置元件
     *
     * @param config http 配置
     * @return http 默认配置
     */
    public static Configurers defaults(HTTP config) {
        return new Configurers("Http_Defaults", config);
    }

    /**
     * jdbc 数据源 配置元件
     *
     * @param config jdbc 配置
     * @return jdbc 数据源 配置元件
     */
    public static Configurers datasource(JDBC config) {
        return new Configurers("JDBC_Datasource", config);
    }

    /**
     * redis 数据源 配置元件
     *
     * @param config redis 配置
     * @return redis 数据源 配置元件
     */
    public static Configurers datasource(Redis config) {
        return new Configurers("Redis_Datasource", config);
    }

    /**
     * dubbo 默认配置 配置元件
     *
     * @param config dubbo 配置
     * @return dubbo 默认配置 配置元件
     */
    public static Configurers defaults(Dubbo config) {
        return new Configurers("Dubbo_Defaults", config);
    }

    /**
     * active mq 默认配置 配置元件
     *
     * @param config active mq 配置
     * @return active mq 默认配置
     */
    public static Configurers defaults(ActiveMQ config) {
        return new Configurers("ActiveMQ_Defaults", config);
    }

    /**
     * kafka 默认配置 配置元件
     *
     * @param config kafka 配置
     * @return kafka 默认配置
     */
    public static Configurers defaults(Kafka config) {
        return new Configurers("Kafka_Defaults", config);
    }

    public static Configurers defaults(RabbitMQ config) {
        return new Configurers("rabbitmq_defaults", config);
    }
}
