/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2021. Lorem XiaoMiSum (mi_xiao@qq.com)
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

package protocol.xyz.migoo.jdbc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.samplers.SampleResult;
import core.xyz.migoo.testelement.AbstractTestElement;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;

public abstract class AbstractJDBCTestElement extends AbstractTestElement {

    static final String SELECT = "select";
    static final String UPDATE = "update";
    static final String STATEMENT = "statement";
    static final String QUERY_TYPE = "query_type";


    protected SampleResult execute(Connection conn) throws SQLException, UnsupportedOperationException {
        return execute(conn, new SampleResult(getPropertyAsString(TITLE), 1));
    }

    protected SampleResult execute(Connection conn, SampleResult result) throws SQLException, UnsupportedOperationException {
        result.setTestClass(this.getClass());
        String sqlQuery = getPropertyAsString(STATEMENT);
        if (StringUtils.isBlank(sqlQuery)) {
            throw new UnsupportedOperationException("Incorrect SQL statement: statement is empty");
        }
        sqlQuery = sqlQuery.trim();
        String queryType = getPropertyAsString(QUERY_TYPE);
        result.sampleStart();
        result.setSamplerData(sqlQuery);
        if (sqlQuery.startsWith(SELECT) || SELECT.equalsIgnoreCase(queryType)) {
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlQuery)) {
                result.sampleEnd();
                String results = this.rsToJSONString(rs);
                result.setResponseData(results);
                return result;
            }
        } else if (sqlQuery.startsWith(UPDATE) || UPDATE.equalsIgnoreCase(queryType)) {
            sqlQuery = sqlQuery.trim();
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(sqlQuery);
                result.sampleEnd();
                int updateCount = stmt.getUpdateCount();
                String results = updateCount + " updates";
                result.setResponseData(results);
                return result;
            }
        } else { // User provided incorrect query type
            throw new UnsupportedOperationException("Unexpected query type or Incorrect SQL statement");
        }
    }

    protected String rsToJSONString(ResultSet rs) throws SQLException {
        JSONArray results = new JSONArray();
        ResultSetMetaData resultSetMetaData = rs.getMetaData();
        int rowCount = 0;
        int fieldCount = resultSetMetaData.getColumnCount();
        while (rs.next()) {
            JSONObject result = new JSONObject();
            for (int i = 1; i <= fieldCount; i++) {
                String key = resultSetMetaData.getColumnName(i);
                result.put(key, rs.getString(key));
            }
            results.add(result);
            rowCount ++;
        }
        if (rowCount  == 1) {
            return results.get(0).toString();
        }
        return results.toJSONString();
    }
}