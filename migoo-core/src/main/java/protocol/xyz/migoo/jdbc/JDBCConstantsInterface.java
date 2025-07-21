package protocol.xyz.migoo.jdbc;

import core.xyz.migoo.testelement.TestElementConstantsInterface;

public interface JDBCConstantsInterface extends TestElementConstantsInterface {

    String DRIVER = "driver";

    String URL = "url";

    String USERNAME = "username";

    String PASSWORD = "password";

    String MAX_ACTIVE = "max_active";

    String MAX_WAIT = "max_wait";

    String SQL = "sql";

    /**
     * 废弃属性，请使用 sql @see {@link #SQL}
     */
    @Deprecated(since = "5.3.0")
    String STATEMENT = "statement";
}
