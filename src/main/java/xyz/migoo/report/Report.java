package xyz.migoo.report;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import xyz.migoo.framework.config.Platform;
import xyz.migoo.framework.AbstractTest;
import xyz.migoo.framework.TestFailure;
import xyz.migoo.framework.TestResult;
import xyz.migoo.exception.ReportException;
import xyz.migoo.loader.reader.AbstractReader;
import xyz.migoo.utils.DateUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xiaomi
 * @date 2018/7/25 11:13
 */
public class Report {

    private final static TemplateEngine TEMPLATE_ENGINE = new TemplateEngine();

    private List<TestResult> testResults = new ArrayList<>();
    private Map<String, Object> report = new HashMap<>(3);
    private String projectName;

    private int total = 0, success = 0, failed = 0, error = 0, skipped = 0;

    public Report() {
        mkDirs();
    }

    public void addResult(TestResult testResult) {
        testResults.add(testResult);
        total += testResult.caseCount();
        success += testResult.successCount();
        failed += testResult.failureCount();
        error += testResult.errorCount();
        skipped += testResult.skipCount();
    }

    public void setProjectName(String projectName){
        report.put("project_name", projectName);
        this.projectName = projectName;
    }

    public void generateIndex() {
        report.put("records", this.records());
        report.put("summary", this.summary());
        String template = HtmlReader.read("classpath://templates/index_report_template.html");
        String content = render(template, report);
        report("index", content, true);
        if (Platform.MAIL_SEND) {
            Email.sendEmail(projectName, content);
        }
    }

    private List<Map<String, Object>> records() {
        List<Map<String, Object>> records = new ArrayList<>();
        AtomicInteger id = new AtomicInteger();
        testResults.forEach(testResult -> {
            Map<String, Object> record = new HashMap<>(8);
            record.put("id", id.get());
            record.put("title", testResult.getName());
            record.put("link", "./html/" + testResult.getName() + ".html");
            record.put("total", testResult.caseCount());
            record.put("success", testResult.successCount());
            record.put("failed", testResult.failureCount());
            record.put("error", testResult.errorCount());
            record.put("skipped", testResult.skipCount());
            records.add(record);
            id.getAndIncrement();
        });
        return records;
    }

    private Map<String, Object> summary() {
        Map<String, Object> summary = new HashMap<>(5);
        summary.put("total", total);
        summary.put("success", success);
        summary.put("failed", failed);
        summary.put("error", error);
        summary.put("skipped", skipped);
        return summary;
    }

    public void generateReport() {
        Map<String, Object> report = new HashMap<>(5);
        String template = HtmlReader.read("classpath://templates/migoo_report_template.html");
        testResults.forEach(testResult -> {
            report.put("summary", this.summary(testResult));
            report.put("records", this.records(testResult));
            report.put("report", testResult.getName());
            report.put("title", testResult.getName());
            String content = render(template, report);
            report(testResult.getName(), content, false);
        });
    }

    private Map<String, Object> summary(TestResult result) {
        Map<String, Object> summary = new HashMap<>(7);
        summary.put("startAt", DateUtil.format(DateUtil.YYYY_MM_DD_HH_MM_SS, result.getStartTime()));
        summary.put("duration", (result.getEndTime() - result.getStartTime()) / 1000.000f + " seconds");
        summary.put("total", result.caseCount());
        summary.put("success", result.successCount());
        summary.put("failed", result.failureCount());
        summary.put("error", result.errorCount());
        summary.put("skipped", result.skipCount());
        return summary;
    }

    private List<Map<String, Object>> records(TestResult result) {
        List<Map<String, Object>> records = new ArrayList<>();
        int id = 1;
        for (TestFailure testFailure : result.errors()) {
            records.add(item(testFailure, null, id, "error"));
            id++;
        }
        for (AbstractTest testFailure : result.failures()) {
            records.add(item(null, testFailure, id, "failure"));
            id++;
        }
        for (AbstractTest skip : result.skips()) {
            records.add(item(null, skip, id, "skipped"));
            id++;
        }
        for (AbstractTest test : result.success()) {
            records.add(item(null, test, id, "success"));
            id++;
        }
        return records;
    }

    private Map<String, Object> item(TestFailure testFailure, AbstractTest test, int id, String status) {
        AbstractTest t = test != null ? test : testFailure.failedTest();
        Map<String, Object> item = new HashMap<>(6);
        item.put("status", status);
        item.put("name", t.getName());
        String time = t.response() != null ? t.response().duration() / 1000.000f + "  s" : "N/A";
        item.put("time", time);
        item.put("detail", this.detail(t, testFailure, id));
        item.put("record_id", "record_" + id);
        item.put("record_href", "#record_" + id);
        return item;
    }

    private synchronized Map<String, Object> detail(AbstractTest test, TestFailure testFailure, int id) {
        Map<String, Object> detail = new HashMap<>(7);
        detail.put("validates", test.validates());
        detail.put("log", this.log(test));
        if (testFailure != null) {
            detail.put("track", testFailure.trace());
        }
        detail.put("validate_id", "validate_" + id);
        detail.put("track_id", "track_" + id);
        detail.put("log_id", "log_" + id);
        detail.put("validate_href", "#validate_" + id);
        detail.put("track_href", "#track_" + id);
        detail.put("log_href", "#log_" + id);
        return detail;
    }

    private synchronized Map<String, Object> log(AbstractTest test) {
        Map<String, Object> res = new HashMap<>(3);
        Map<String, Object> req = new HashMap<>(4);
        if (test.response() != null) {
            res.put("statusCode", test.response().statusCode());
            res.put("body", test.response().text());
        }
        if (test.request() != null) {
            req.put("url", test.request().uri());
            req.put("method", test.request().method());
            req.put("headers", test.request().headers());
            req.put("params", test.request().body());
        }
        Map<String, Object> log = new HashMap<>(2);
        log.put("request", req);
        log.put("response", res);
        return log;
    }

    /**
     * 使用 Thymeleaf 渲染 HTML
     *
     * @param template HTML模板
     * @param report   报告数据
     * @return 渲染后的HTML
     */
    private String render(String template, Map<String, Object> report) {
        Context context = new Context();
        context.setVariables(report);
        return TEMPLATE_ENGINE.process(template, context);
    }

    private void mkDirs() {
        File file = new File(String.format("%s/reports/%s/html", System.getProperty("user.dir"), DateUtil.TODAY_DATE));
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private void report(String name, String content, boolean isIndex) {
        String path = isIndex ? String.format("%s/reports/%s/index.html", System.getProperty("user.dir"), DateUtil.TODAY_DATE)
                : String.format("%s/reports/%s/html/%s.html", System.getProperty("user.dir"), DateUtil.TODAY_DATE, name);
        try (Writer writer = new FileWriter(path)) {
            writer.write(content);
        } catch (Exception e) {
            throw new ReportException("create report error , file path " + path, e);
        }
    }

    private static class HtmlReader extends AbstractReader {
        private static HtmlReader htmlReader = new HtmlReader();

        private static String read(String path) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                htmlReader.stream("html", path);
                BufferedReader reader = new BufferedReader(new InputStreamReader(htmlReader.inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
            } catch (Exception e) {
                MiGooLog.log(e.getMessage(), e);
            }
            return stringBuilder.toString();
        }
    }
}
