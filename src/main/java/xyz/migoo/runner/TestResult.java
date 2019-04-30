package xyz.migoo.runner;

import xyz.migoo.http.Request;
import xyz.migoo.http.Response;
import xyz.migoo.utils.DateUtil;

import java.util.*;

/**
 * @author xiaomi
 * @date 2018/7/24 14:26
 */
public class TestResult extends junit.framework.TestResult{

    private long startAt;
    private long endAt;
    private Map<String, Object> report;
    private CaseSuite caseSuite;

    public TestResult(){
        super();
    }

    protected synchronized void serialization(){
        report = new HashMap<>(1);
        report.put("records", this.records());
        report.put("summary", this.summary());
    }

    public void setTestSuite(CaseSuite caseSuite){
        this.caseSuite = caseSuite;
    }

    private List<Map<String, Object>> records(){
        List<Map<String, Object>> records = new ArrayList();
        int id = 1;
        for (TestSuite testSuite : caseSuite.testSuites()){
            for (TestCase testCase : testSuite.testCases()){
                Map<String, Object> record = new HashMap<>(6);
                record.put("status", "success");
                record.put("name", testCase.name());
                record.put("time", testCase.response().duration() / 1000.000f + "  s");
                record.put("detail", this.detail(testCase, record, id));
                record.put("record_id", "record_" + id);
                record.put("record_href", "#record_" + id);
                records.add(record);
                id++;
            }
        }
        return records;
    }


    private synchronized Map<String, Object> detail(TestCase testCase, Map<String, Object> record, int id){
        Map<String, Object> detail = new HashMap<>(7);
        detail.put("validate", testCase.validate());
        detail.put("log", this.log(testCase.response(), testCase.request()));
        detail.put("track", null);
        if (testCase.error() != null){
            record.put("status", "error");
            detail.put("track", testCase.error().getMessage());
        }
        if (testCase.failure() != null){
            record.put("status", "failure");
            detail.put("track", testCase.failure().getMessage());
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
        res.put("statusCode", response.statusCode());
        res.put("body", response.body());

        Map<String, Object> req = new HashMap<>(4);
        req.put("url", request.url());
        req.put("method", request.method());
        req.put("headers", request.headers());
        req.put("body", request.body());
        req.put("data", request.data());
        req.put("query", request.query());

        Map<String, Object> log = new HashMap<>(2);
        log.put("request", req);
        log.put("response", res);
        return log;
    }

    private synchronized Map<String, Object> summary(){
        Map<String, Object> summary = new HashMap<>(7);
        summary.put("startAt", DateUtil.format(DateUtil.YYYY_MM_DD_HH_MM_SS, startAt));
        summary.put("duration", (endAt - startAt) / 1000.000f + " seconds");
        summary.put("total", caseSuite.rTests());
        summary.put("success", caseSuite.rTests() - caseSuite.fTests() - caseSuite.eTests());
        summary.put("failed", caseSuite.fTests());
        summary.put("error", caseSuite.eTests());
        summary.put("skipped", 0);
        return summary;
    }
    synchronized void startAt(long startAt) {
        this.startAt = startAt;
    }

    synchronized void endAt(long endAt) {
        this.endAt = endAt;
    }

    synchronized Map<String, Object> report(){
        return report;
    }

}
