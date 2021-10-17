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

package components.xyz.migoo.extractor;

import core.xyz.migoo.samplers.SampleResult;
import core.xyz.migoo.variables.MiGooVariables;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author xiaomi
 * Created in 2021/10/17 17:23
 */
public class RegexExtractorTest {

    private RegexExtractor extractor;
    private static final SampleResult SAMPLE_RESULT = new SampleResult("test");

    @BeforeAll
    public static void beforeAll() {
        SAMPLE_RESULT.setUrl("11111");
        SAMPLE_RESULT.setResponseData("\"aabb\":\"1\",\"aabb\":\"2\"");
    }

    @BeforeEach
    public void beforeEach() {
        extractor = new RegexExtractor();
        extractor.setVariables(new MiGooVariables());
        extractor.setProperty("variable_name", "vaa");
    }

    @Test
    public void test4RegexExtractor1() {
        extractor.setProperty("field", "\"aabb\":\"(\\d+)\"");
        SampleResult result = extractor.process(SAMPLE_RESULT);
        // 未指定匹配位置，默认取匹配到的第0个
        assert extractor.get("value").toString().equals("\"aabb\":\"1\"");
        assert extractor.getVariables().get("vaa").toString().equals("\"aabb\":\"1\"");
        assert result.getSamplerData().equals(extractor.getProperty().toString());
        assert result.getTitle().equals("RegexExtractor");
    }

    @Test
    public void test4RegexExtractor2() {
        extractor.setProperty("field", "\"aabb\":\"(\\d+)\"");
        extractor.setProperty("match_num", "-1");
        SampleResult result = extractor.process(SAMPLE_RESULT);
        // 指定位置 < 0，默认取匹配到的第0个
        assert extractor.get("value").toString().equals("\"aabb\":\"1\"");
        assert extractor.getVariables().get("vaa").toString().equals("\"aabb\":\"1\"");
        assert result.getSamplerData().equals(extractor.getProperty().toString());
        assert result.getTitle().equals("RegexExtractor");
    }

    @Test
    public void test4RegexExtractor3() {
        extractor.setProperty("field", "\"aabb\":\"(\\d+)\"");
        extractor.setProperty("match_num", "0");
        SampleResult result = extractor.process(SAMPLE_RESULT);
        // 指定位置 >= 0，取指定的匹配位置的值
        assert extractor.get("value").toString().equals("\"aabb\":\"1\"");
        assert extractor.getVariables().get("vaa").toString().equals("\"aabb\":\"1\"");
        assert result.getSamplerData().equals(extractor.getProperty().toString());
        assert result.getTitle().equals("RegexExtractor");
    }

    @Test
    public void test4RegexExtractor4() {
        extractor.setProperty("field", "\"aabb\":\"(\\d+)\"");
        extractor.setProperty("match_num", "2");
        SampleResult result = extractor.process(SAMPLE_RESULT);
        // 指定位置 >= 0，取指定的匹配位置的值
        assert extractor.get("value").toString().equals("2");
        assert extractor.getVariables().get("vaa").toString().equals("2");
        assert result.getSamplerData().equals(extractor.getProperty().toString());
        assert result.getTitle().equals("RegexExtractor");
    }

    @Test
    public void test4RegexExtractor5() {
        extractor.setProperty("field", "\"aabb\":\"\\d+\"");
        extractor.setProperty("match_num", "2");
        SampleResult result = extractor.process(SAMPLE_RESULT);
        // 正则表达式无()，match_num 不生效
        assert extractor.get("value").toString().equals("\"aabb\":\"1\"");
        assert extractor.getVariables().get("vaa").toString().equals("\"aabb\":\"1\"");
        assert result.getSamplerData().equals(extractor.getProperty().toString());
        assert result.getTitle().equals("RegexExtractor");
    }
}
