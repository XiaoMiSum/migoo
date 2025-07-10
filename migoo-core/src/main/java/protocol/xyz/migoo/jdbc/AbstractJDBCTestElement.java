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
import protocol.xyz.migoo.jdbc.config.DataSourceElement;
import protocol.xyz.migoo.jdbc.util.JDBCConstantsInterface;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * @author xiaomi
 */
public abstract class AbstractJDBCTestElement extends AbstractTestElement implements JDBCConstantsInterface {

    protected SampleResult execute() {
        var result = new SampleResult(getPropertyAsString(TITLE));
        try {
            var dataSourceName = getPropertyAsString(DATASOURCE);
            var datasource = (DataSourceElement) getVariables().get(dataSourceName);
            if (StringUtils.isBlank(dataSourceName)) {
                throw new IllegalArgumentException("datasource name is not specified or DataSource is null");
            }
            return execute(datasource, result);
        } catch (Exception e) {
            result.sampleEnd();
            result.setThrowable(e);
        }
        return result;
    }

    protected SampleResult execute(DataSourceElement datasource, SampleResult result) throws Exception {
        result.setTestClass(this.getClass());
        result.setUrl(datasource.getUrl());
        var sql = Optional.ofNullable(getPropertyAsString(SQL)).orElse(getPropertyAsString(STATEMENT));
        if (StringUtils.isBlank(sql)) {
            throw new UnsupportedOperationException("Incorrect SQL statement: sql statement can not empty");
        }
        sql = sql.trim();
        result.sampleStart();
        result.setSamplerData(sql);
        try (var conn = datasource.getConnection(); var statement = conn.createStatement()) {
            var bool = statement.execute(sql);
            result.sampleEnd();
            String results = bool ? toJSONString(statement.getResultSet()) : "Affected rows: " + statement.getUpdateCount();
            result.setResponseData(results);
            return result;
        }
    }

    protected String toJSONString(ResultSet result) throws SQLException {
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
        result.close();
        return (results.size() == 1 ? results.getFirst() : results).toString();
    }
}