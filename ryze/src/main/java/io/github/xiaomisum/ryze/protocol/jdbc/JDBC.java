/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2025.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

package io.github.xiaomisum.ryze.protocol.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.github.xiaomisum.ryze.core.testelement.sampler.DefaultSampleResult;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author xiaomi
 * Created at 2025/7/21 19:31
 */
public class JDBC {

    public static byte[] execute(DruidDataSource datasource, String sql, DefaultSampleResult result) {
        result.sampleStart();
        try (var conn = datasource.getConnection(); var statement = conn.createStatement()) {
            var hasResultSet = statement.execute(sql);
            return (hasResultSet ? toJSONBytes(statement) : "Affected rows: " + statement.getUpdateCount()).getBytes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            result.sampleEnd();
        }
    }

    private static String toJSONBytes(Statement statement) throws SQLException {
        try (var result = statement.getResultSet()) {
            var results = new JSONArray();
            var meta = result.getMetaData();
            while (result.next()) {
                var item = new JSONObject();
                for (var i = 1; i <= meta.getColumnCount(); i++) {
                    String key = meta.getColumnName(i);
                    item.put(key, result.getString(key));
                }
                results.add(item);
            }
            return results.size() == 1 ? results.getJSONObject(0).toJSONString() : results.toJSONString();
        }
    }
}
