package protocol.xyz.migoo.jdbc.config;

import com.alibaba.fastjson2.annotation.JSONField;
import com.alibaba.fastjson2.annotation.JSONType;
import core.xyz.migoo.config.ConfigureItem;
import org.apache.commons.lang3.StringUtils;
import protocol.xyz.migoo.jdbc.JDBCConstantsInterface;

@JSONType(deserializer = JDBCConfigureItemObjectReader.class)
public class JDBCConfigureItem implements ConfigureItem<JDBCConfigureItem>, JDBCConstantsInterface {

    @JSONField(name = DATASOURCE)
    private String datasource;

    @JSONField(name = DRIVER, ordinal = 1)
    private String driver;

    @JSONField(name = URL, ordinal = 2)
    private String url;

    @JSONField(name = USERNAME, ordinal = 3)
    private String username;

    @JSONField(name = PASSWORD, ordinal = 4)
    private String password;

    @JSONField(name = MAX_ACTIVE, ordinal = 5)
    private int maxActive;

    @JSONField(name = MAX_WAIT, ordinal = 6)
    private int maxWait;

    @JSONField(name = SQL, ordinal = 7)
    private String sql;


    @Override
    public JDBCConfigureItem merge(JDBCConfigureItem other) {
        if (other == null) {
            return copy();
        }
        var localOther = other.copy();
        var self = copy();
        self.datasource = StringUtils.isBlank(self.datasource) ? localOther.datasource : self.datasource;
        self.driver = StringUtils.isBlank(self.driver) ? localOther.driver : self.driver;
        self.url = StringUtils.isBlank(self.url) ? localOther.url : self.url;
        self.username = StringUtils.isBlank(self.username) ? localOther.username : self.username;
        self.password = StringUtils.isBlank(self.password) ? localOther.password : self.password;
        self.sql = StringUtils.isBlank(self.sql) ? localOther.sql : self.sql;
        self.maxActive = self.maxActive > 0 ? localOther.maxActive : self.maxActive;
        self.maxWait = self.maxWait > 0 ? localOther.maxWait : self.maxWait;
        return self;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMaxActive() {
        return maxActive > 1 ? 10 : maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMaxWait() {
        return maxWait < 1000 ? 5000 : maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
