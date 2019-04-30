package xyz.migoo.report;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import xyz.migoo.exception.ReaderException;
import xyz.migoo.exception.ReportException;
import xyz.migoo.reader.AbstractReader;
import xyz.migoo.utils.DateUtil;
import xyz.migoo.utils.EmailUtil;
import xyz.migoo.utils.Log;
import xyz.migoo.utils.StringUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static xyz.migoo.config.Platform.HTTP_CLIENT_VERSION;
import static xyz.migoo.config.Platform.JDK_VERSION;
import static xyz.migoo.config.Platform.OS_VERSION;

/**
 * @author xiaomi
 * @date 2018/7/25 11:13
 */
public class Report {

    private static Log log = new Log(Report.class);
    private final static TemplateEngine TEMPLATE_ENGINE = new TemplateEngine();

    private Report() {
    }

    public static String generateReport(Map<String, Object> report, String reportName, boolean isMain) {
        return generateReport(report, reportName, isMain, true);
    }

    public static String generateReport(Map<String, Object> report, String reportName, boolean isMain, boolean sendEmail) {
        if (StringUtil.isBlank(reportName)) {
            reportName = "Auto Test Report";
        }
        report.put("report", "Test Report:  " + reportName);
        report.put("title", reportName + " - TestReport");
        report.put("platform", platform());
        String content;
        try {
            content = render("classpath://templates/migoo_report_template.html", report);
            String file = report(reportName, content, isMain);
            if (sendEmail) {
                EmailUtil.sendEmail(content, file);
            }
            return file;
        } catch (ReaderException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用 Thymeleaf 渲染 HTML
     *
     * @param template HTML模板
     * @param report   报告数据
     * @return 渲染后的HTML
     */
    private static String render(String template, Map<String, Object> report) throws ReaderException {
        template = HtmlReader.read(template);
        Context context = new Context();
        context.setVariables(report);
        return TEMPLATE_ENGINE.process(template, context);
    }

    private static Map<String, String> platform() {
        Map<String, String> platform = new HashMap<>(3);
        platform.put("jdk", "JDK " + JDK_VERSION);
        platform.put("httpclient", "HTTP Client " + HTTP_CLIENT_VERSION);
        platform.put("os", OS_VERSION);
        return platform;
    }

    private static String report(String name, String template, boolean isMain) {
        File file = new File(System.getProperty("user.dir"));
        if (isMain) {
            file = new File(file.getPath() + "/Reports/" + DateUtil.TODAY_DATE);
        } else {
            file = new File(file.getParent() + "/Reports/" + DateUtil.TODAY_DATE);
        }
        if (!file.exists()) {
            file.mkdir();
        }
        String path = file.getPath() + "/" + name + ".html";
        try (Writer writer = new FileWriter(path)) {
            writer.write(template);
            writer.close();
            return path;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ReportException("create report error , file path " + file.getPath());
        }
    }

    private static class HtmlReader extends AbstractReader {
        private static HtmlReader htmlReader = new HtmlReader();

        private static String read(String path) throws ReaderException {
            htmlReader.stream("html", path);
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(htmlReader.inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new ReportException("load template file error. " + e.getMessage());
            }
            return stringBuilder.toString();
        }
    }
}
