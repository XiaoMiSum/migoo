package xyz.migoo.report;

/**
 * @author i-lov
 */
public interface IReport {

    /**
     * 创建日志文件
     * @param logName
     */
    void create(String logName);

    /**
     * 写入日志内容
     * @param info
     */
    void info(String info);

    /**
     * 写入日志内容
     * @param info
     */
    void debug(String info);

    /**
     * 写入日志内容
     * @param info
     */
    void error(String info);

    /**
     * 写入日志内容
     * @param info
     * @param e
     */
    void error(String info, Exception e);

    /**
     * 写入测试报告内容
     * @param info
     * @param result
     */
    void report(String info, String result);

    /**
     * 写入测试报告内容
     * @param info
     * @param exp
     * @param act
     */
    void report(Object info, Object exp, Object act);

    /**
     * 关闭日志文件
     */
    void close();
}
