/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018. Lorem XiaoMiSum (mi_xiao@qq.com)
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining
 *  * a copy of this software and associated documentation files (the
 *  * 'Software'), to deal in the Software without restriction, including
 *  * without limitation the rights to use, copy, modify, merge, publish,
 *  * distribute, sublicense, and/or sell copies of the Software, and to
 *  * permit persons to whom the Software is furnished to do so, subject to
 *  * the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be
 *  * included in all copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 *  * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 *  * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 *  * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 *  * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package protocol.xyz.migoo.jdbc.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.annotation.JSONField;
import core.xyz.migoo.testelement.AbstractTestElement;
import core.xyz.migoo.testelement.Alias;
import core.xyz.migoo.testelement.TestStateListener;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author xiaomi
 */
@Alias(aliasList = {"jdbcdatasource", "jdbc_datasource"})
public class DataSourceElement extends AbstractTestElement implements TestStateListener {

    private static final String URL_KEY = "url";
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private static final String MAX_ACTIVE_KEY = "max_active";
    private static final String MAX_WAIT_KEY = "max_wait";
    private static final String VARIABLE_NAME_KEY = "variable_name";

    @JSONField(serialize = false)
    private final DruidDataSource dataSource = new DruidDataSource();

    @Override
    public void testStarted() {
        dataSource.setUrl(getPropertyAsString(URL_KEY));
        dataSource.setUsername(getPropertyAsString(USERNAME_KEY));
        dataSource.setPassword(getPropertyAsString(PASSWORD_KEY));
        dataSource.setMaxActive(getPropertyAsInt(MAX_ACTIVE_KEY) > 0 ? getPropertyAsInt(MAX_ACTIVE_KEY) : 10);
        dataSource.setMaxWait(getPropertyAsInt(MAX_WAIT_KEY) > 0 ? getPropertyAsInt(MAX_WAIT_KEY) : 10000);
        getVariables().put(getPropertyAsString(VARIABLE_NAME_KEY), this);
    }

    @Override
    public void testEnded() {
        if (dataSource.isEnable()) {
            dataSource.close();
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public String getUrl() {
        return dataSource.getUrl();
    }
}