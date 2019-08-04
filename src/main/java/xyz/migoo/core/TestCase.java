package xyz.migoo.core;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.config.CaseKeys;
import xyz.migoo.exception.AssertionFailure;
import xyz.migoo.exception.ExecuteError;
import xyz.migoo.exception.SkippedRun;
import xyz.migoo.http.Request;
import xyz.migoo.http.Response;
import xyz.migoo.utils.TypeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author xiaomi
 * @date 2018/7/24 20:37
 */
public class TestCase extends junit.framework.TestCase{

    private TestSuite testSuite;
    private JSONObject testCase;
    private Task task;

    TestCase(Task task, JSONObject testCase, TestSuite testSuite){
        this.task = task;
        this.testCase = testCase;
        this.testSuite = testSuite;
    }

    @Override
    public void runTest() {
        try {
            Boolean aBoolean = TypeUtil.booleanOf(testCase.get(CaseKeys.CASE_IGNORE));
            if (aBoolean != null && aBoolean) {
                throw new SkippedRun(testCase.getString(CaseKeys.CASE_TITLE));
            }
            this.task.run(this.testCase, this);
        } catch (SkippedRun skipped){
            this.testSuite.addCaseResult(new CaseResult().name(testCase.getString(CaseKeys.CASE_TITLE))
                    .validates(testCase.getJSONArray(CaseKeys.VALIDATE)).skipped().throwable(skipped));
        }
    }

    void addCaseResult(CaseResult result){
        this.testSuite.addCaseResult(result);
    }
}
