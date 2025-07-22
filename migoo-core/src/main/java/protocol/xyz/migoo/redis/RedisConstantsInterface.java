package protocol.xyz.migoo.redis;

import core.xyz.migoo.testelement.TestElementConstantsInterface;

public interface RedisConstantsInterface extends TestElementConstantsInterface {

    String REDIS_URL_TEMPLATE = "redis://";
    /**
     * redis://[username:password@]host[:port][/db]
     */
    String URL = "url";
    /**
     * 废弃属性，请使用 host @see {@link #URL}
     */
    @Deprecated(since = "5.3.0")
    String HOST = "host";
    /**
     * 废弃属性，请使用 host @see {@link #URL}
     */
    @Deprecated(since = "5.3.0")
    String PORT = "port";
    /**
     * 废弃属性，请使用 host @see {@link #URL}
     */
    @Deprecated(since = "5.3.0")
    String DATABASE = "database";
    /**
     * 废弃属性，请使用 host @see {@link #URL}
     */
    @Deprecated(since = "5.3.0")
    String USERNAME = "username";
    /**
     * 废弃属性，请使用 host @see {@link #URL}
     */
    @Deprecated(since = "5.3.0")
    String PASSWORD = "password";
    /**
     * 废弃属性，请使用 timeout @see {@link #TIMEOUT}
     */
    @Deprecated(since = "5.3.0")
    String TIME_OUT = "time_out";

    String TIMEOUT = "timeout";

    String MAX_TOTAL = "max_total";

    String MAX_IDLE = "max_idle";

    String MIN_IDLE = "min_idle";

    String COMMAND = "command";

    String SEND = "send";
}
