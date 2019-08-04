package xyz.migoo.core;

import xyz.migoo.http.Request;
import xyz.migoo.http.Response;
import xyz.migoo.utils.DateUtil;
import xyz.migoo.utils.StringUtil;

import java.util.*;

import static xyz.migoo.core.CaseResult.*;

/**
 * @author xiaomi
 * @date 2018/7/24 14:26
 */
public class TestResult extends junit.framework.TestResult{

    private long startAt;
    private long endAt;
    private Map<String, Object> report;
    private TestSuite testSuite;
    private int success = 0;
    private int error = 0;
    private int failure = 0;
    private int skipped = 0;

    TestResult(){
        super();
    }

    synchronized void serialization(){
        report = new HashMap<>(2);
        report.put("records", this.records());
        report.put("summary", this.summary());
    }

    void setTestSuite(TestSuite testSuite){
        this.testSuite = testSuite;
    }

    private List<Map<String, Object>> records(){
        List<Map<String, Object>> records = new ArrayList<>();
        int id = 1;
        for (CaseResult result : testSuite.caseResults()){
            Map<String, Object> record = new HashMap<>(6);
            calculator(result.status());
            record.put("status", result.status());
            record.put("name", result.name());
            String time = result.response() != null ? result.response().duration() / 1000.000f + "  s" : "N/A";
            record.put("time", time);
            record.put("detail", this.detail(result, id));
            record.put("record_id", "record_" + id);
            record.put("record_href", "#record_" + id);
            records.add(record);
            id ++;
        }

        return records;
    }


    private synchronized Map<String, Object> detail(CaseResult result, int id){
        Map<String, Object> detail = new HashMap<>(7);
        if (result.validates() != null){
            detail.put("validate", result.validates());
        }
        detail.put("log", this.log(result.response(), result.request()));
        if (result.throwable() != null){
            detail.put("track", StringUtil.getStackTrace(result.throwable()));
        }
        detail.put("validate_id", "validate_" + id);
        detail.put("track_id", "track_" + id);
        detail.put("log_id", "log_" + id);
        detail.put("validate_href", "#validate_" + id);
        detail.put("track_href", "#track_" + id);
        detail.put("log_href", "#log_" + id);
        return detail;
    }


    private synchronized Map<String, Object> log(Response response, Request request){
        Map<String, Object> res = new HashMap<>(3);
        if (response != null) {
            res.put("statusCode", response.statusCode());
            res.put("body", response.body());
        }
        Map<String, Object> req = new HashMap<>(4);
        if (request != null) {
            req.put("url", request.url());
            req.put("method", request.method());
            req.put("headers", request.headers());
            req.put("body", request.body());
            req.put("data", request.data());
            req.put("query", request.query());
        }
        Map<String, Object> log = new HashMap<>(2);
        log.put("request", req);
        log.put("response", res);
        return log;
    }

    private synchronized Map<String, Object> summary(){
        Map<String, Object> summary = new HashMap<>(7);
        summary.put("startAt", DateUtil.format(DateUtil.YYYY_MM_DD_HH_MM_SS, startAt));
        summary.put("duration", (endAt - startAt) / 1000.000f + " seconds");
        summary.put("total", testSuite.caseResults().size());
        summary.put("success", success);
        summary.put("failed", failure);
        summary.put("error", error);
        summary.put("skipped", skipped);
        return summary;
    }
    synchronized void startAt(long startAt) {
        this.startAt = startAt;
    }

    synchronized void endAt(long endAt) {
        this.endAt = endAt;
    }

    public synchronized Map<String, Object> report(){
        return report;
    }

    public TestSuite testSuite(){
        return testSuite;
    }

    private void calculator(String status){
        switch (status){
            case SUCCESS:
                success += 1;
                return;
            case ERROR:
                error += 1;
                return;
            case FAILURE:
                failure += 1;
                return;
            case SKIPPED:
                skipped += 1;
                return;
            default:
        }
    }

    public int success() {
        return success;
    }

    public int error() {
        return error;
    }

    public int failure() {
        return failure;
    }

    public int skipped() {
        return skipped;
    }
}
