package xyz.migoo.utils.database;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;
import xyz.migoo.utils.Log;

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

    private static Log log = new Log(Database.class);
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
            log.error(e.getMessage(), e);
        }
        return new Database(conn);
    }

    public static Database create(String user, String password, String url){
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
            log.info("execute sql: " + sql + "params: " + paramsToString(params));
        } catch (SQLException e) {
            log.error("execute sql exception: " + sql + "  params: " + paramsToString(params), e);
        }
        return list;
    }

    public List<Map<String, Object>> maps(String sql, Object... params) {
        QueryRunner run = new QueryRunner();
        List<Map<String, Object>> list = null;
        try {
            list = run.query(conn, sql, new MapListHandler(), params);
            log.info("execute sql: " + sql + "  params: " + paramsToString(params));
        } catch (SQLException e) {
            log.error("execute sql exception: " + sql + "  params: " + paramsToString(params), e);
        }
        return list;
    }

    public List arrays(String sql, Object... params) {
        QueryRunner run = new QueryRunner();
        ArrayList list = null;
        try {
            list = (ArrayList)run.query(conn, sql, new ColumnListHandler(), params);
            log.info("execute sql: " + sql + "  params: " + paramsToString(params));
        } catch (SQLException e) {
            log.error("execute sql exception: " + sql + "  params: " + paramsToString(params), e);
        }
        return list;
    }

    public Map query(String sql, Object... params) {
        QueryRunner run = new QueryRunner();
        Map object = null;
        try {
            object = run.query(conn, sql, new MapHandler(), params);
            log.info("execute sql: " + sql + "  params: " + paramsToString(params));
        } catch (SQLException e) {
            log.error("execute sql exception: " + sql + "  params: " + paramsToString(params), e);
        }
        return object;
    }

    public int update(String sql, Object... params){
        QueryRunner run = new QueryRunner();
        int total = 0;
        try {
            total = run.update(conn, sql, params);
            log.info("execute sql: " + sql + "  params: " + paramsToString(params));
        } catch (SQLException e) {
            log.error("execute sql exception: " + sql + "  params: " + paramsToString(params), e);
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