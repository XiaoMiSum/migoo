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

package core.xyz.migoo.sampler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author xiaomi
 * Created in 2021/10/18 14:54
 */
public class SampleResultTest {

    private SampleResult result;

    @BeforeEach
    public void beforeEach() {
        result = new SampleResult("test");
    }

    @Test
    public void testSampleResultTitle() {
        assert "test".equals(result.getTitle());
    }

    @Test
    public void testSampleResultTestClass() {
        assert result.getTestClass() == null;
        result.setTestClass("testclasss");
        assert Objects.equals(result.getTestClass(), "testclasss");
        result.setTestClass(this.getClass());
        assert Objects.equals(result.getTestClass(), this.getClass().getName());
    }

    @Test
    public void testSampleResultSamplerData1() {
        // 默认值： 空字符串
        assert Objects.equals(result.getSamplerData(), "");
        result.setSamplerData("hh");
        assert Objects.equals(result.getSamplerData(), "hh");
    }

    @Test
    public void testSampleResultSamplerData2() {
        SampleResult result2 = new SampleResult("heihei");
        result2.setUrl("url2");
        result2.setSamplerData("samplerData2");
        result2.setResponseData("responseData2");
        result2.setTestClass("class");
        result.setSamplerData(result2);
        assert result.getTitle().equals("test");
        assert Objects.equals(result.getSamplerData(), result2.getSamplerData());
        assert Arrays.equals(result.getResponseData(), result2.getResponseData());
        assert Objects.equals(result.getTestClass(), result2.getTestClass());
        assert Objects.equals(result.getUrl(), result2.getUrl());
    }

    @Test
    public void testSampleResultResponseData() {
        assert Arrays.equals(result.getResponseData(), new byte[0]);
        result.setResponseData("haha".getBytes(StandardCharsets.UTF_8));
        assert Arrays.equals(result.getResponseData(), "haha".getBytes(StandardCharsets.UTF_8));
        assert Objects.equals(result.getResponseDataAsString(), "haha");
        result.setResponseData("xixi");
        assert Objects.equals(result.getResponseDataAsString(), "xixi");
    }

    @Test
    public void testSampleResultUrl() {
        assert Objects.equals(result.getUrl(), "");
        result.setUrl("url");
        assert Objects.equals(result.getUrl(), "url");
    }

    @Test
    public void testSampleResultAssertionResults() {
        assert result.getAssertionResults() == null;
        result.setAssertionResults(new ArrayList<>());
        assert result.getAssertionResults() != null;
    }
}
