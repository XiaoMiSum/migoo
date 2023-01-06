/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022.  Lorem XiaoMiSum (mi_xiao@qq.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * 'Software'), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package component.xyz.migoo.assertions;

import com.alibaba.fastjson2.JSONObject;
import component.xyz.migoo.assertion.HTTPResponseAssertion;
import core.xyz.migoo.assertion.Assertion;
import core.xyz.migoo.assertion.VerifyResult;
import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.testelement.TestElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import protocol.xyz.migoo.http.sampler.HTTPSampleResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaomi
 * Created in 2021/10/16 16:35
 */
public class HTTPResponseAssertionTest {

    private static final JSONObject JSON = new JSONObject();
    private static final List<Map<String, String>> ARRAY = new ArrayList<>();
    private Assertion assertion;
    private SampleResult sampleResult;

    @BeforeAll
    public static void beforeAll() {
        JSON.put("key1", 1);
        JSON.put("key2", 2);
        Map<String, String> header1 = new HashMap<>();
        header1.put("header1", "1");
        Map<String, String> header2 = new HashMap<>();
        header2.put("header1", "2");
        Map<String, String> header3 = new HashMap<>();
        header3.put("header3", "3");
        ARRAY.add(header1);
        ARRAY.add(header2);
        ARRAY.add(header3);
    }

    @BeforeEach
    public void beforeEach() {
        assertion = new HTTPResponseAssertion();
        sampleResult = new HTTPSampleResult("test http");
        sampleResult.setUrl("test url");
        ((HTTPSampleResult) sampleResult).setResponseMessageOK();
        ((HTTPSampleResult) sampleResult).setMethod("GET");
        ((HTTPSampleResult) sampleResult).setResponseCodeOK();
        sampleResult.setResponseData(JSON.toJSONString());
        ((HTTPSampleResult) sampleResult).setResponseHeaders(ARRAY);
    }

    @Test
    public void test4AssertContext1() {
        ((TestElement) assertion).setProperty("field", "context");
        ((TestElement) assertion).setProperty("expected", JSON.toJSONString());
        VerifyResult result = assertion.getResult(sampleResult);
        Assertions.assertTrue(result.isSuccessful(), result.getContext());
    }

    @Test
    public void test4AssertContext2() {
        ((TestElement) assertion).setProperty("field", null);
        ((TestElement) assertion).setProperty("expected", JSON.toJSONString());
        VerifyResult result = assertion.getResult(sampleResult);
        assert result.isSuccessful();
        // {"key1":1,"key2":2}
        // {"key1":1, "key2":2}
    }

    @Test
    public void test4AssertContext3() {
        ((TestElement) assertion).setProperty("expected", JSON.toJSONString());
        VerifyResult result = assertion.getResult(sampleResult);
        assert result.isSuccessful();
    }

    @Test
    public void test4AssertStatus1() {
        // "line", "status", "code", "statuscode", "statusline", "status_code", "status_line"
        ((TestElement) assertion).setProperty("field", "status");
        ((TestElement) assertion).setProperty("expected", 200);
        VerifyResult result = assertion.getResult(sampleResult);
        assert result.isSuccessful();
    }

    @Test
    public void test4AssertStatus2() {
        // "line", "status", "code", "statuscode", "statusline", "status_code", "status_line"
        ((TestElement) assertion).setProperty("field", "statusline");
        ((TestElement) assertion).setProperty("expected", 200);
        VerifyResult result = assertion.getResult(sampleResult);
        assert result.isSuccessful();
    }

    @Test
    public void test4AssertHeader1() {
        // "line", "status", "code", "statuscode", "statusline", "status_code", "status_line"
        ((TestElement) assertion).setProperty("field", "header[1].header1");
        ((TestElement) assertion).setProperty("expected", "2");
        ((TestElement) assertion).setProperty("rule", "==");
        VerifyResult result = assertion.getResult(sampleResult);
        assert result.isSuccessful();
    }

    @Test
    public void test4AssertHeader2() {
        // "line", "status", "code", "statuscode", "statusline", "status_code", "status_line"
        ((TestElement) assertion).setProperty("field", "header[2].header3");
        ((TestElement) assertion).setProperty("expected", "3");
        ((TestElement) assertion).setProperty("rule", "==");
        VerifyResult result = assertion.getResult(sampleResult);
        assert result.isSuccessful();
    }

    @Test
    public void test4SampleResult() {
        sampleResult = new SampleResult("test");
        VerifyResult result = assertion.getResult(sampleResult);
        assert "HTTPAssertion".equals(result.getName());
        assert result.isSuccessful();
        assert result.getContext().equals(String.format("unsupported %s, assert default true", sampleResult.getClass().getSimpleName()));
    }

    @Test
    public void test4ResultFalse() {
        ((TestElement) assertion).setProperty("field", "statusline");
        ((TestElement) assertion).setProperty("expected", 200);
        ((TestElement) assertion).setProperty("rule", ">");
        VerifyResult result = assertion.getResult(sampleResult);
        assert !result.isSuccessful();
    }
}
