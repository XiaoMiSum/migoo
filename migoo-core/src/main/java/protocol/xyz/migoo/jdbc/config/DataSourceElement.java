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

package protocol.xyz.migoo.jdbc.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson2.annotation.JSONField;
import core.xyz.migoo.testelement.AbstractTestElement;
import core.xyz.migoo.testelement.Alias;
import core.xyz.migoo.testelement.TestStateListener;
import protocol.xyz.migoo.jdbc.util.JDBCConstantsInterface;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author xiaomi
 */
@Alias({"JDBCDatasource", "jdbc_datasource", "datasource", "data_source", "JDBCSource", "jdbc_source"})
public class DataSourceElement extends AbstractTestElement implements TestStateListener, JDBCConstantsInterface {


    @JSONField(serialize = false)
    private final DruidDataSource dataSource = new DruidDataSource();

    @Override
    public void testStarted() {
        try {
            // 兼容 没有使用 SPI 的 JDBC驱动
            if (getProperty().containsKey(DRIVER)) {
                Class.forName(getPropertyAsString(DRIVER));
            }
        } catch (Exception ignored) {
        }
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