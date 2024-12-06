package coder.xyz.migoo

import coder.xyz.migoo.protocol.Protocol

class Configurers extends El {

    private Configurers(String testClass, El protocol) {
        super(testClass)
        p("config", protocol.customize())
    }

    static Configurers defaults(Protocol.HTTP config) {
        return new Configurers("HttpDefaults", config)
    }

    static Configurers datasource(Protocol.JDBC config) {
        return new Configurers("JDBCDatasource", config)
    }

    static Configurers datasource(Protocol.Redis config) {
        return new Configurers("RedisDatasource", config)
    }

    static Configurers defaults(Protocol.ActiveMQ config) {
        return new Configurers("ActiveMQ_Defaults", config)
    }

    static Configurers defaults(Protocol.Kafka config) {
        return new Configurers("Kafka_Defaults", config)
    }
}
