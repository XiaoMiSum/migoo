package xyz.migoo.database;

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

    public static Database create(String cls,String user,String password,String url) {
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

    public void close() {
        DbUtils.closeQuietly(conn);
    }

    public Object[] queryList(String sql, Object... params) {
        QueryRunner run = new QueryRunner();
        Object[] list = null;
        try {
            list = run.query(conn, sql, new ArrayHandler(), params);
            log.info("execute sql: " + sql);
        } catch (SQLException e) {
            log.error("execute sql exception: " + sql, e);
        }
        return list;
    }

    public Map query(String sql, Object... params) {
        QueryRunner run = new QueryRunner();
        Map object = null;
        try {
            object = run.query(conn, sql, new MapHandler(), params);
            log.info("execute sql: " + sql);
        } catch (SQLException e) {
            log.error("execute sql exception: " + sql, e);
        }
        return object;
    }
}