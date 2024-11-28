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

package protocol.xyz.migoo.jdbc;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.testelement.AbstractTestElement;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author xiaomi
 */
public abstract class AbstractJDBCTestElement extends AbstractTestElement {

    static final String STATEMENT = "statement";

    protected SampleResult execute(Connection conn) throws SQLException, UnsupportedOperationException {
        return execute(conn, new SampleResult(getPropertyAsString(TITLE)));
    }

    protected SampleResult execute(Connection conn, SampleResult result) throws SQLException, UnsupportedOperationException {
        result.setTestClass(this.getClass());
        var sql = getPropertyAsString(STATEMENT);
        if (StringUtils.isBlank(sql)) {
            throw new UnsupportedOperationException("Incorrect SQL statement: sql statement can not empty");
        }
        sql = sql.trim();
        result.sampleStart();
        result.setSamplerData(sql);
        try (var stmt = conn.createStatement()) {
            boolean bool = stmt.execute(sql);
            result.sampleEnd();
            String results = bool ? this.rsToJSONString(stmt.getResultSet()) : "Affected rows: " + stmt.getUpdateCount();
            result.setResponseData(results);
            return result;
        }
    }

    protected String rsToJSONString(ResultSet rs) throws SQLException {
        var results = new JSONArray();
        var resultSetMetaData = rs.getMetaData();
        int rowCount = 0;
        int fieldCount = resultSetMetaData.getColumnCount();
        while (rs.next()) {
            var result = new JSONObject();
            for (int i = 1; i <= fieldCount; i++) {
                String key = resultSetMetaData.getColumnName(i);
                result.put(key, rs.getString(key));
            }
            results.add(result);
            rowCount++;
        }
        if (rowCount == 1) {
            return results.getFirst().toString();
        }
        rs.close();
        return results.toJSONString();
    }
}