/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

package components.xyz.migoo.assertions;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.assertions.Assertion;
import core.xyz.migoo.assertions.AssertionResult;
import core.xyz.migoo.samplers.SampleResult;
import core.xyz.migoo.testelement.TestElement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import protocol.xyz.migoo.http.sampler.HTTPSampleResult;

/**
 * @author xiaomi
 * Created in 2021/10/16 16:35
 */
public class HTTPResponseAssertionTest {

    private Assertion assertion;

    private SampleResult sampleResult;

    private static final JSONObject JSON = new JSONObject();
    private static final JSONArray ARRAY = new JSONArray();

    @BeforeAll
    public static void beforeAll() {
        JSON.put("key1", 1);
        JSON.put("key2", 2);
        JSONObject header1 = new JSONObject();
        header1.put("header1", "1");
        JSONObject header2 = new JSONObject();
        header2.put("header1", "2");
        JSONObject header3 = new JSONObject();
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
        ((HTTPSampleResult) sampleResult).setResponseHeaders(ARRAY.toJSONString());
    }

    @Test
    public void test4AssertContext1() {
        ((TestElement) assertion).setProperty("field", "context");
        ((TestElement) assertion).setProperty("expected", JSON.toJSONString());
        AssertionResult result = assertion.getResult(sampleResult);
        assert result.isSuccessful();
    }

    @Test
    public void test4AssertContext2() {
        ((TestElement) assertion).setProperty("field", null);
        ((TestElement) assertion).setProperty("expected", JSON.toJSONString());
        AssertionResult result = assertion.getResult(sampleResult);
        assert result.isSuccessful();
    }

    @Test
    public void test4AssertContext3() {
        ((TestElement) assertion).setProperty("expected", JSON.toJSONString());
        AssertionResult result = assertion.getResult(sampleResult);
        assert result.isSuccessful();
    }

    @Test
    public void test4AssertStatus1() {
        // "line", "status", "code", "statuscode", "statusline", "status_code", "status_line"
        ((TestElement) assertion).setProperty("field", "status");
        ((TestElement) assertion).setProperty("expected", 200);
        AssertionResult result = assertion.getResult(sampleResult);
        assert result.isSuccessful();
    }

    @Test
    public void test4AssertStatus2() {
        // "line", "status", "code", "statuscode", "statusline", "status_code", "status_line"
        ((TestElement) assertion).setProperty("field", "statusline");
        ((TestElement) assertion).setProperty("expected", 200);
        AssertionResult result = assertion.getResult(sampleResult);
        assert result.isSuccessful();
    }

    @Test
    public void test4AssertHeader1() {
        // "line", "status", "code", "statuscode", "statusline", "status_code", "status_line"
        ((TestElement) assertion).setProperty("field", "header[1].header1");
        ((TestElement) assertion).setProperty("expected", "2");
        ((TestElement) assertion).setProperty("rule", "==");
        AssertionResult result = assertion.getResult(sampleResult);
        assert result.isSuccessful();
    }

    @Test
    public void test4AssertHeader2() {
        // "line", "status", "code", "statuscode", "statusline", "status_code", "status_line"
        ((TestElement) assertion).setProperty("field", "header[2].header3");
        ((TestElement) assertion).setProperty("expected", "3");
        ((TestElement) assertion).setProperty("rule", "==");
        AssertionResult result = assertion.getResult(sampleResult);
        assert result.isSuccessful();
    }

    @Test
    public void test4SampleResult() {
        sampleResult = new SampleResult("test");
        AssertionResult result = assertion.getResult(sampleResult);
        assert "HTTPAssertion".equals(result.getName());
        assert result.isSuccessful();
        assert result.getContext().equals(String.format("unsupported %s, assert default true", sampleResult.getClass().getSimpleName()));
    }

    @Test
    public void test4ResultFalse() {
        ((TestElement) assertion).setProperty("field", "statusline");
        ((TestElement) assertion).setProperty("expected", 200);
        ((TestElement) assertion).setProperty("rule", ">");
        AssertionResult result = assertion.getResult(sampleResult);
        assert !result.isSuccessful();
        assert result.getContext().equals(String.format("expected value '%s', but found '%s', rule: '%s'", 200, ((HTTPSampleResult) sampleResult).getResponseCode(), ">"));
    }
}
