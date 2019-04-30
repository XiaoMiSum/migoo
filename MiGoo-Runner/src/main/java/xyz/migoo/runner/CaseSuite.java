package xyz.migoo.runner;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.config.CaseKeys;
import xyz.migoo.exception.InvokeException;
import xyz.migoo.http.Response;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author xiaomi
 * @date 2018/7/24 15:55
 */
public class CaseSuite extends junit.framework.TestSuite {

    private StringBuilder testName;
    private Vector<TestSuite> testSuites;
    private int fTests = 0;
    private int eTests = 0;
    private int rTests = 0;

    public CaseSuite(List<JSONObject> caseSets) throws InvokeException {
        testName = new StringBuilder();
        testSuites = new Vector<>(10);
        for (JSONObject caseSet : caseSets){
            TestSuite suite = new TestSuite(caseSet, this);
            testName.append(caseSet.getString(CaseKeys.NAME)).append("&");
            testSuites.add(suite);
        }
    }

    public Vector<TestSuite> testSuites(){
        return testSuites;
    }

    void addFTests(){
        fTests += 1;
    }

    void addETests(){
        eTests += 1;
    }

    void addRTests(){
        rTests += 1;
    }

    int fTests(){
        return fTests;
    }

    int eTests(){
        return eTests;
    }

    int rTests(){
        return rTests;
    }

    public String name(){
        return testName.delete(testName.length() - 1, testName.length()).toString();
    }
}
