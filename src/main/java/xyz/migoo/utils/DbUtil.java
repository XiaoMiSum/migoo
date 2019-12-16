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
