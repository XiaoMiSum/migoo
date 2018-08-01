package xyz.migoo.report.html;

import xyz.migoo.report.IReport;
import xyz.migoo.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
* @author xiaomi
 */
public class HtmlReport implements IReport {

    protected static final String PASS = "PASS";
    protected static final String FAIL = "FAIL";
    protected static final String NT = "NT";

    private Logger htmlLog;
    private FileHandler fileHandler;
    private RecordStore recordStore = RecordStore.getInstance();

    @Override
    public void create(String fileName) {
        try {
            fileHandler = new FileHandler(fileName);
            //设置日志格式化
            fileHandler.setFormatter(new HtmlFormatter());
            htmlLog = Logger.getLogger(HtmlReport.class+"");
            htmlLog.setLevel(Level.ALL);
            //添加日志
            htmlLog.addHandler(fileHandler);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        fileHandler.close();
        recordStore.setPass(0);
        recordStore.setFail(0);
        recordStore.setResult("");
        recordStore.setActual("");
        recordStore.setExpected("");
    }

    @Override
    public void report(String info, String result) {
        recordStore.setResult(result);
        htmlLog.severe(info);
    }

    @Override
    public void report(Object logMsg, Object exp, Object act) {
        String msg = (String) logMsg;
        String actual = (String) act;
        String expected = (String) exp;
        recordStore.setActual(actual);
        recordStore.setExpected(expected);
        boolean eResult = StringUtils.isNotBlank(actual) && StringUtil.containsIgnoreCase(expected, actual);
        boolean aResult = StringUtils.isNotBlank(expected) && expected.equals(actual);
        if (aResult || eResult) {
            recordStore.setResult(PASS);
        } else {
            recordStore.setResult(FAIL);
        }
        try {
            htmlLog.severe(msg);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" logger write exception!");
        }
    }

    @Override
    public void info(String info) {

    }

    @Override
    public void debug(String info) {

    }

    @Override
    public void error(String info) {

    }

    @Override
    public void error(String info, Exception e) {

    }
}
