/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
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
 *
 */

package components.xyz.migoo.plugins.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.plugin.Plugin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/**
 * @author yacheng.xiao
 * @date 2021/1/13 10:52
 **/
public class DataSource implements Plugin {

    private final DruidDataSource dataSource = new DruidDataSource();

    @Override
    public void initialize(JSONObject config) throws Exception {
        dataSource.setUrl(config.getString("url"));
        dataSource.setUsername(config.getString("username"));
        dataSource.setPassword(config.getString("password"));
        dataSource.setMaxActive(config.get("max_active") != null ? config.getInteger("max_active") :
                config.get("maxActive") != null ? config.getInteger("maxActive") : 10);
        dataSource.setMaxWait(config.get("max_wait") != null ? config.getInteger("max_wait") :
                config.get("maxWait") != null ? config.getInteger("maxWait") : 10000);
    }

    @Override
    public void close() {
        dataSource.close();
    }

    public Object execute(Integer type, String sql) throws Exception {
        if (type == 1) {
            return execute(sql);
        } else {
            return executeQuery(sql);
        }
    }

    public List<JSONObject> executeQuery(String sql) throws Exception {
        List<JSONObject> results = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet set = stmt.executeQuery(sql)) {
            ResultSetMetaData resultSetMetaData = set.getMetaData();
            int fieldCount = resultSetMetaData.getColumnCount();
            while (set.next()) {
                JSONObject result = new JSONObject();
                for (int i = 1; i <= fieldCount; i++) {
                    String key = resultSetMetaData.getColumnName(i);
                    result.put(key, set.getString(key));
                }
                results.add(result);
            }
        }
        return results;
    }

    public Object execute(String sql) throws Exception {
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {
            return stmt.execute(sql);
        }
    }

}
