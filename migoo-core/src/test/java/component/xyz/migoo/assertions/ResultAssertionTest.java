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
import component.xyz.migoo.assertion.ResultAssertion;
import core.xyz.migoo.assertion.VerifyResult;
import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.testelement.TestElement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import protocol.xyz.migoo.http.sampler.HTTPSampleResult;

/**
 * @author xiaomi
 * Created in 2021/10/16 16:36
 */
public class ResultAssertionTest {

    private static final JSONObject JSON = new JSONObject();
    private ResultAssertion assertion;
    private SampleResult sampleResult;

    @BeforeAll
    public static void beforeAll() {
        JSON.put("key1", 1);
        JSON.put("key2", 2);
    }

    @BeforeEach
    public void beforeEach() {
        this.assertion = new ResultAssertion();
        sampleResult = new HTTPSampleResult("test http");
        sampleResult.setUrl("test url");
        sampleResult.setResponseData(JSON.toJSONString());
    }

    @Test
    public void testJSONAssertion() {
        ((TestElement) assertion).setProperty("expected", JSON.toJSONString());
        VerifyResult result = assertion.getResult(sampleResult);
        assert result.isSuccessful();
    }
}
