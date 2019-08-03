package xyz.migoo.report;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import xyz.migoo.exception.ReaderException;
import xyz.migoo.exception.ReportException;
import xyz.migoo.runner.TestResult;
import xyz.migoo.utils.reader.AbstractReader;
import xyz.migoo.utils.DateUtil;
import xyz.migoo.utils.Log;
import xyz.migoo.utils.StringUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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
    private static Map<String, String> platform = new HashMap<>(3);

    private List<TestResult> testResults = new ArrayList<>();
    Map<String, Object> report = new HashMap<>(2);
    private int total = 0;
    private int success = 0;
    private int failed = 0;
    private int error = 0;
    private int skipped = 0;

    public Report(){
    }

    public void addResult(TestResult testResult){
        total += testResult.testSuite().rTests();
        failed += testResult.testSuite().fTests();
        error += testResult.testSuite().eTests();
        success += testResult.testSuite().getSuccessCount();
        skipped += testResult.testSuite().iTests();;
        testResults.add(testResult);
    }

    public void serialization(){
        Map<String, Object> summary = new HashMap<>(5);
        List<Map<String, Object>> records = new ArrayList<>();
        this.records(records);
        this.summary(summary);
        report.put("records", records);
        report.put("summary", summary);
        report.put("platform", platform);
    }

    private void records(List<Map<String, Object>> records){
        AtomicInteger id = new AtomicInteger();
        testResults.forEach(testResult -> {
            Map<String, Object> record = new HashMap<>(8);
            record.put("id", id.get());
            record.put("title", testResult.testSuite().getName());
            record.put("link", "./html/" + testResult.testSuite().getName() + ".html");
            record.put("total", testResult.testSuite().rTests());
            record.put("success", testResult.testSuite().getSuccessCount());
            record.put("failed", testResult.testSuite().fTests());
            record.put("error", testResult.testSuite().eTests());
            record.put("skipped", testResult.testSuite().iTests());
            records.add(record);
            id.getAndIncrement();
        });
    }

    private void summary(Map<String, Object> summary){
        summary.put("total", total);
        summary.put("success", success);
        summary.put("failed", failed);
        summary.put("error", error);
        summary.put("skipped", skipped);
    }

    public void index(){
        try {
            String content = render("classpath://templates/index_report_template.html", report);
            report("index", content, true);
        } catch (ReaderException e) {
            e.printStackTrace();
        }
    }

    public static String generateReport(Map<String, Object> report, String reportName) {
        return generateReport(report, reportName, true);
    }

    public static String generateReport(Map<String, Object> report, String reportName, boolean sendEmail) {
        if (StringUtil.isBlank(reportName)) {
            reportName = "Auto Test Report";
        }
        report.put("report", reportName);
        report.put("title", reportName + " - TestReport");
        report.put("platform", platform);
        String content;
        try {
            content = render("classpath://templates/migoo_report_template.html", report);
            String file = report(reportName, content, false);
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

    private static String report(String name, String template, boolean isIndex) {
        File file = new File(System.getProperty("user.dir"));
        file = new File(file.getPath() + "/Reports/" + DateUtil.TODAY_DATE);
        String path = file.getPath() + "/html/" + name + ".html";
        if (isIndex){
            path = file.getPath() + "/" + name + ".html";
        }
        if (!file.exists()) {
            file.mkdir();
            file = new File(file.getPath() + "/html");
            file.mkdir();
        }
        try (Writer writer = new FileWriter(path)) {
            writer.write(template);
            writer.close();
            return path;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ReportException("create report error , file path " + file.getPath());
        }
    }

    {
        platform.put("jdk", "JDK " + JDK_VERSION);
        platform.put("httpclient", "HTTP Client " + HTTP_CLIENT_VERSION);
        platform.put("os", OS_VERSION);
    }

    private static class HtmlReader extends AbstractReader {
        private static HtmlReader htmlReader = new HtmlReader();

        private static String read(String path) throws ReaderException {
            htmlReader.stream("html", path);
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(htmlReader.inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new ReportException("load template file error. " + e.getMessage());
            }
            return stringBuilder.toString();
        }
    }


    public static void main(String[] args) throws IOException {
        File f1 = new File("/Users/kogome/test2/1.yml");
        System.out.println(f1.getName());
    }
}
