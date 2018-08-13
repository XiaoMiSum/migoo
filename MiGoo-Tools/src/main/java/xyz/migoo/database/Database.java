package xyz.migoo.database;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.KeyedHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import xyz.migoo.utils.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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

    public <T>T query(Class clazz, String sql, Object... params) {
        QueryRunner run = new QueryRunner();
        T object = null;
        try {
            object = (T)run.query(conn, sql, new BeanHandler(clazz), params);
        } catch (SQLException e) {
            log.error("execute sql exception: " + sql, e);
        }
        return object;
    }

    public List<?> queryList(Class clazz, String sql, Object... params) {
        QueryRunner run = new QueryRunner();
        List<?> list = null;
        try {
            list = (List<?>) run.query(conn, sql, new BeanListHandler<>(clazz), params);
        } catch (SQLException e) {
            log.error("execute sql exception: " + sql, e);
        }
        return list;
    }

    public List<?> query(Class clazz, String sql) {
        QueryRunner run = new QueryRunner();
        List<?> list = null;
        try {
            list = (List<?>) run.query(conn, sql, new BeanListHandler<>(clazz));
        } catch (SQLException e) {
            log.error("execute sql exception: " + sql, e);
        }
        return list;
    }

    public Map query(String sql, String columnName, Object... params) {
        QueryRunner run = new QueryRunner();
        Map object = null;
        try {
            object = (Map) run.query(conn, sql, new KeyedHandler(columnName), params);
        } catch (SQLException e) {
            log.error("execute sql exception: " + sql, e);
        }
        return object;
    }

    public Map query(String sql, Object... params) {
        QueryRunner run = new QueryRunner();
        Map object = null;
        try {
            object = run.query(conn, sql, new MapHandler(), params);
        } catch (SQLException e) {
            log.error("execute sql exception: " + sql, e);
        }
        return object;
    }
}