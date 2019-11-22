package xyz.migoo.utils.database;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;
import xyz.migoo.report.MiGooLog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xiaomi
 */
public class Database {

    private Connection conn;

    private Database(Connection conn) {
        this.conn = conn;
    }

    public static Database create(String cls, String user, String password, String url) {
        Connection conn = null;
        try {
            Class.forName(cls);
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            MiGooLog.log(e.getMessage(), e);
        }
        return new Database(conn);
    }

    public static Database mysql(String user, String password, String url){
        return create("com.mysql.cj.jdbc.Driver", user, password, url);
    }

    public void close() {
        DbUtils.closeQuietly(conn);
    }

    public Object[] array(String sql, Object... params) {
        QueryRunner run = new QueryRunner();
        Object[] list = null;
        try {
            list = run.query(conn, sql, new ArrayHandler(), params);
            MiGooLog.log("execute sql: " + sql + "params: " + paramsToString(params));
        } catch (SQLException e) {
            MiGooLog.log("execute sql exception", e);
        }
        return list;
    }

    public List<Map<String, Object>> maps(String sql, Object... params) {
        QueryRunner run = new QueryRunner();
        List<Map<String, Object>> list = null;
        try {
            list = run.query(conn, sql, new MapListHandler(), params);
            MiGooLog.log("execute sql: " + sql + "  params: " + paramsToString(params));
        } catch (SQLException e) {
            MiGooLog.log("execute sql exception", e);
        }
        return list;
    }

    public List<Object> arrays(String sql, Object... params) {
        QueryRunner run = new QueryRunner();
        ArrayList<Object> list = null;
        try {
            list = (ArrayList<Object>)run.query(conn, sql, new ColumnListHandler<>(), params);
            MiGooLog.log("execute sql: " + sql + "  params: " + paramsToString(params));
        } catch (SQLException e) {
            MiGooLog.log("execute sql exception", e);
        }
        return list;
    }

    public Map<String, Object> query(String sql, Object... params) {
        QueryRunner run = new QueryRunner();
        Map<String, Object> object = null;
        try {
            object = run.query(conn, sql, new MapHandler(), params);
            MiGooLog.log("execute sql: " + sql + "  params: " + paramsToString(params));
        } catch (SQLException e) {
            MiGooLog.log("execute sql exception", e);
        }
        return object;
    }

    public int update(String sql, Object... params){
        QueryRunner run = new QueryRunner();
        int total = 0;
        try {
            total = run.update(conn, sql, params);
            MiGooLog.log("execute sql: " + sql + "  params: " + paramsToString(params));
        } catch (SQLException e) {
            MiGooLog.log("execute sql exception", e);
        }
        return total;
    }

    public int insert(String sql, Object... params){
        return update(sql, params);
    }

    public int delete(String sql, Object... params){
        return update(sql, params);
    }

    private String paramsToString(Object... params){
        StringBuilder sb = new StringBuilder();
        for (Object object: params){
            if (sb.length() > 0){
                sb.append(", ");
            }
            sb.append(object);
        }
        return sb.toString();
    }
}
