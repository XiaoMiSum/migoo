package coder.xyz.migoo;

import coder.xyz.migoo.protocol.*;

public class Configurers extends El {

    private Configurers(String testClass, El protocol) {
        super(testClass);
        p("config", protocol.properties());
    }

    public static Configurers defaults(HTTP config) {
        return new Configurers("Http_Defaults", config);
    }

    public static Configurers datasource(JDBC config) {
        return new Configurers("JDBC_Datasource", config);
    }

    public static Configurers datasource(Redis config) {
        return new Configurers("Redis_Datasource", config);
    }

    public static Configurers defaults(ActiveMQ config) {
        return new Configurers("ActiveMQ_Defaults", config);
    }

    public static Configurers defaults(Kafka config) {
        return new Configurers("Kafka_Defaults", config);
    }
}
