package xyz.migoo.report;

import xyz.migoo.report.html.HtmlReport;
import xyz.migoo.utils.DateUtil;
import xyz.migoo.utils.StringUtil;
import xyz.migoo.utils.Log;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * @author xiaomi
 */
public class Report {

    private static final Log log = new Log(Report.class);
    private static IReport report;

    private static void create(String filePath) {
        report = new HtmlReport();
        File file;
        if (StringUtil.isBlank(filePath)) {
            file = new File(new File(System.getProperty("user.dir")).getParent() + "/report");
        }else {
            file = new File(filePath);

        }
        if (!file.exists()) {
            file.mkdir();
        }
        report.create(file.getPath() + "/report_" + DateUtil.format() + ".html");
    }

    public static void close() {
        report.close();
    }

    public static void report(String message, Object expected, Object actual) {
        if (report == null) {
            create("");
        }
        String act = (String) actual;
        String exp = (String) expected;
        report.report(message, expected, actual);
        log.info("expected is ' " + exp + " ', actual is ' " + act + " '");
        boolean eResult = StringUtils.isNotBlank(act) && StringUtil.containsIgnoreCase(exp, act);
        boolean aResult = StringUtils.isNotBlank(exp) && expected.equals(actual);
        if (aResult || eResult) {
            log.debug("test case verify result is success.");
        } else {
            log.debug("test case verify result is failure.");
        }
    }

    public static void report(String message, String result) {
        if (report == null) {
            create("");
        }
        report.report(message, result);
        log.info("set case title: " + message + ", result: " + result);
    }

}
