/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022.  Lorem XiaoMiSum (mi_xiao@qq.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * 'Software'), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.github.xiaomisum.ryze.protocol.jdbc.config;

import com.alibaba.fastjson2.annotation.JSONField;
import io.github.xiaomisum.ryze.core.config.ConfigureItem;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.AbstractTestElement;
import io.github.xiaomisum.ryze.protocol.jdbc.JDBCConstantsInterface;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xiaomi
 */
public class JDBCConfigureItem implements ConfigureItem<JDBCConfigureItem>, JDBCConstantsInterface {

    @JSONField(name = DATASOURCE)
    protected String datasource;

    @JSONField(name = DRIVER, ordinal = 1)
    protected String driver;

    @JSONField(name = URL, ordinal = 2)
    protected String url;

    @JSONField(name = USERNAME, ordinal = 3)
    protected String username;

    @JSONField(name = PASSWORD, ordinal = 4)
    protected String password;

    @JSONField(name = MAX_ACTIVE, ordinal = 5)
    protected int maxActive;

    @JSONField(name = MAX_WAIT, ordinal = 6)
    protected int maxWait;

    @JSONField(name = SQL, ordinal = 7)
    protected String sql;

    public JDBCConfigureItem() {
    }

    public static Builder builder() {
        return new Builder();
    }

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
        self.maxActive = (self.maxActive = self.maxActive > 0 ? localOther.maxActive : self.maxActive) > 0 ? self.maxActive : 10;
        self.maxWait = (self.maxWait = self.maxWait > 0 ? localOther.maxWait : self.maxWait) > 0 ? self.maxWait : 5000;
        return self;
    }

    @Override
    public JDBCConfigureItem evaluate(ContextWrapper context) {
        datasource = (String) context.evaluate(datasource);
        driver = (String) context.evaluate(driver);
        url = (String) context.evaluate(url);
        username = (String) context.evaluate(username);
        password = (String) context.evaluate(password);
        sql = (String) context.evaluate(sql);
        return this;
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
        return maxActive > 1 ? maxActive : 10;
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

    /**
     * JDBC 协议配置项构建类
     */
    public static class Builder extends AbstractTestElement.ConfigureBuilder<Builder, JDBCConfigureItem> {

        private JDBCConfigureItem configure = new JDBCConfigureItem();

        public Builder datasource(String datasource) {
            configure.datasource = datasource;
            return self;
        }

        public Builder driver(String driver) {
            configure.driver = driver;
            return self;
        }

        public Builder url(String url) {
            configure.url = url;
            return self;
        }

        public Builder username(String username) {
            configure.username = username;
            return self;
        }

        public Builder password(String password) {
            configure.password = password;
            return self;
        }

        public Builder maxActive(int maxActive) {
            configure.maxActive = maxActive;
            return self;
        }

        public Builder maxWait(int maxWait) {
            configure.maxWait = maxWait;
            return self;
        }

        public Builder sql(String sql) {
            configure.sql = sql;
            return self;
        }

        public Builder config(JDBCConfigureItem config) {
            configure = configure.merge(config);
            return self;
        }

        @Override
        public JDBCConfigureItem build() {
            return configure;
        }
    }
}
