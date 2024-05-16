package coder.xyz.migoo

import coder.xyz.migoo.protocol.Protocol

class Configurer extends El {

    private Configurer(String testClass, El protocol) {
        super(testClass)
        p(protocol.customize())
    }

    static Configurer withHttpDefaults(Protocol.HTTP config) {
        return new Configurer("HttpDefaults", config)
    }

    static Configurer withJDBCDatasource(Protocol.JDBC config) {
        return new Configurer("JDBCDatasource", config)
    }

    static Configurer withRedisDatasource(Protocol.Redis config) {
        return new Configurer("RedisDatasource", config)
    }
}
