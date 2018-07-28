package xyz.migoo.runner;

import junit.framework.TestFailure;
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
    private List<String> failures;
    private List<Response> responses;
    private Map<String, Object> report;

    public TestResult(){
        super();
        responses = new ArrayList<>();
    }

    protected synchronized void serialization(){
        report = new HashMap<>(size());
        report.put("records", this.records());
        report.put("summary", this.summary());
    }

    private synchronized List<Map<String, Object>> records(){
        List<Map<String, Object>> records = new ArrayList();
        int id = 1;
        for (Response response : responses){
            Map<String, Object> record = new HashMap<>(4);
            Map<String, Object> detail = new HashMap<>(2);
            detail.put("log", this.log(response));
            record.put("status", "success");
            detail.put("track", null);
            for (String failure : failures) {
                if (failure.equals(response.request().title())) {
                    for (TestFailure testFailure : fFailures){
                        TestCase testCase = (TestCase)testFailure.failedTest();
                        if (testCase.getName().equals(failure)) {
                            Throwable throwable = testFailure.thrownException();
                            detail.put("track", throwable.getMessage());
                        }
                    }
                    record.put("status", "failure");
                }
            }
            detail.put("track_id", "track_" + id);
            detail.put("log_id", "log_" + id);
            detail.put("track_href", "#track_" + id);
            detail.put("log_href", "#log_" + id);
            record.put("name", response.request().title());
            record.put("time", response.duration() / 1000.000f + "  seconds");
            record.put("detail", detail);
            record.put("record_id", "record_" + id);
            record.put("record_href", "#record_" + id);
            records.add(record);
            id ++;
        }
        return records;
    }

    private synchronized Map<String, Object> summary(){
        Map<String, Object> summary = new HashMap<>(7);
        summary.put("startAt", DateUtil.format(startAt(), DateUtil.YYYY_MM_DD_HH_MM_SS));
        summary.put("duration", duration() / 1000.000f + " seconds");
        summary.put("total", size());
        summary.put("success", successCount());
        summary.put("failed", failureCount());
        summary.put("error", errorCount());
        summary.put("skipped", 0);
        return summary;
    }

    private synchronized Map<String, Object> log(Response response){
        Map<String, Object> res = new HashMap<>(3);
        res.put("statusCode", response.statusCode());
        res.put("headers", response.headers());
        res.put("body", response.body());

        Map<String, Object> req = new HashMap<>(4);
        req.put("url", response.request().url());
        req.put("method", response.request().method());
        req.put("headers", response.request().headers());
        req.put("body", response.request().body());

        Map<String, Object> log = new HashMap<>(2);
        log.put("request", req);
        log.put("response", res);
        return log;
    }

    protected synchronized void responses(List<Response> responses) {
        this.responses.addAll(responses);
    }

    protected synchronized void failures(List<String> failures){
        this.failures = failures;
    }

    protected synchronized void startAt(long startAt) {
        this.startAt = startAt;
    }

    protected synchronized void endAt(long endAt) {
        this.endAt = endAt;
    }

    public synchronized long startAt() {
        return startAt;
    }

    public synchronized int successCount() {
        return size() - failureCount() - errorCount();
    }

    public synchronized long duration(){
        return endAt - startAt;
    }

    public synchronized int size(){
        return fRunTests;
    }

    public synchronized Map<String, Object> report(){
        return report;
    }

}
