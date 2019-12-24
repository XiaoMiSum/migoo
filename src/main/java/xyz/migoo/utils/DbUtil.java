/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
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

package xyz.migoo.utils;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;
import xyz.migoo.report.MiGooLog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xiaomi
 */
public class DbUtil {

    private Connection conn;

    private DbUtil(Connection conn) {
        this.conn = conn;
    }

    public static DbUtil create(String cls, String user, String password, String url) {
        Connection conn = null;
        try {
            Class.forName(cls);
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            MiGooLog.log(e.getMessage(), e);
        }
        return new DbUtil(conn);
    }

    public static DbUtil mysql(String user, String password, String url) {
        return create("com.mysql.cj.jdbc.Driver", user, password, url);
    }

    public void close() {
        DbUtils.closeQuietly(conn);
    }

    public Object[] array(String sql, Object... params) throws Exception {
        QueryRunner run = new QueryRunner();
        Object[] list = run.query(conn, sql, new ArrayHandler(), params);
        MiGooLog.log(String.format("execute sql: %s  params: %s", sql, paramsToString(params)));
        return list;
    }

    public List<Map<String, Object>> maps(String sql, Object... params) throws Exception {
        QueryRunner run = new QueryRunner();
        List<Map<String, Object>> list = run.query(conn, sql, new MapListHandler(), params);
        MiGooLog.log(String.format("execute sql: %s  params: %s", sql, paramsToString(params)));
        return list;
    }

    public List<Object> arrays(String sql, Object... params) throws Exception {
        QueryRunner run = new QueryRunner();
        ArrayList<Object> list = (ArrayList<Object>) run.query(conn, sql, new ColumnListHandler<>(), params);
        MiGooLog.log(String.format("execute sql: %s  params: %s", sql, paramsToString(params)));
        return list;
    }

    public Map<String, Object> query(String sql, Object... params) throws Exception {
        QueryRunner run = new QueryRunner();
        Map<String, Object> object = run.query(conn, sql, new MapHandler(), params);
        MiGooLog.log(String.format("execute sql: %s  params: %s", sql, paramsToString(params)));
        return object;
    }

    public int update(String sql, Object... params) throws Exception {
        QueryRunner run = new QueryRunner();
        int total = run.update(conn, sql, params);
        MiGooLog.log(String.format("execute sql: %s  params: %s", sql, paramsToString(params)));
        return total;
    }

    public int insert(String sql, Object... params) throws Exception {
        return update(sql, params);
    }

    public int delete(String sql, Object... params) throws Exception {
        return update(sql, params);
    }

    private String paramsToString(Object... params) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (Object object : params) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(object);
        }
        return sb.toString();
    }
}
