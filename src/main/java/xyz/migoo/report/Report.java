package xyz.migoo.report;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import xyz.migoo.framework.core.AbstractTest;
import xyz.migoo.framework.core.TestFailure;
import xyz.migoo.framework.core.TestResult;
import xyz.migoo.exception.ReaderException;
import xyz.migoo.exception.ReportException;
import xyz.migoo.framework.http.Response;
import xyz.migoo.utils.MiGooLog;
import xyz.migoo.parser.reader.AbstractReader;
import xyz.migoo.utils.DateUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static xyz.migoo.framework.config.Platform.HTTP_CLIENT_VERSION;
import static xyz.migoo.framework.config.Platform.JDK_VERSION;
import static xyz.migoo.framework.config.Platform.OS_VERSION;

/**
 * @author xiaomi
 * @date 2018/7/25 11:13
 */
public class Report {

    private final static TemplateEngine TEMPLATE_ENGINE = new TemplateEngine();
    private static Map<String, String> platform = new HashMap<>(3);

    private List<TestResult> testResults = new ArrayList<>();
    private Map<String, Object> report = new HashMap<>(2);

    private int total = 0, success= 0, failed = 0, error = 0, skipped = 0;

    public Report(){
    }

    public void addResult(TestResult testResult){
        testResults.add(testResult);
        total += testResult.caseCount();
        success += testResult.successCount();
        failed += testResult.failureCount();
        error += testResult.errorCount();
        skipped += testResult.skipCount();
    }

    public void serialization(){
        Map<String, Object> summary = new HashMap<>(5);
        List<Map<String, Object>> records = new ArrayList<>();
        this.records(records);
        this.summary(summary);
        report.put("records", records);
        report.put("summary", summary);
    }

    private void records(List<Map<String, Object>> records){
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

    public void generateReport() {
        Map<String, Object> report = new HashMap<>(5);
        testResults.forEach(testResult -> {
            report.put("summary", this.summary(testResult));
            report.put("records", this.records(testResult));
            report.put("report", testResult.getName());
            report.put("title", testResult.getName() + " - TestReport");
            report.put("platform", platform);
            try {
                String content = render("classpath://templates/migoo_report_template.html", report);
                report(testResult.getName(), content, false);

            } catch (ReaderException e) {
                e.printStackTrace();
            }
        });
    }

    private Map<String, Object> summary(TestResult result){
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

    private List<Map<String, Object>> records(TestResult result){
        List<Map<String, Object>> records = new ArrayList<>();
        int id = 1;
        for (TestFailure testFailure : result.errors()){
            records.add(item(testFailure, null, id, "error"));
            id ++;
        }
        for (TestFailure testFailure : result.failures()){
            records.add(item(testFailure, null, id, "failure"));
            id ++;
        }
        for (TestFailure testFailure : result.skips()){
            records.add(item(testFailure, null,id, "skipped"));
            id ++;
        }
        for (AbstractTest test : result.success()){
            records.add(item(null, test, id, "success"));
            id ++;
        }
        return records;
    }

    private Map<String, Object> item(TestFailure testFailure, AbstractTest test, int id, String status){
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

    private synchronized Map<String, Object> detail(AbstractTest test, TestFailure testFailure, int id){
        Map<String, Object> detail = new HashMap<>(7);
        detail.put("validate", test.validate());
        detail.put("log", this.log(test.response()));
        if (testFailure != null){
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

    private synchronized Map<String, Object> log(Response response){
        Map<String, Object> res = new HashMap<>(3);
        Map<String, Object> req = new HashMap<>(4);
        if (response != null) {
            res.put("statusCode", response.statusCode());
            res.put("body", response.body());
            if (response.request() != null) {
                req.put("url", response.request().url());
                req.put("method", response.request().method());
                req.put("headers", response.request().headers());
                req.put("body", response.request().body());
                req.put("data", response.request().data());
                req.put("query", response.request().query());
            }
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
            MiGooLog.log(e.getMessage(), e);
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
                MiGooLog.log(e.getMessage(), e);
                throw new ReportException("load template file error. " + e.getMessage());
            }
            return stringBuilder.toString();
        }
    }
}
