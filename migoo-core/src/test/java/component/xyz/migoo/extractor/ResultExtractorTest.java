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

package component.xyz.migoo.extractor;

import com.alibaba.fastjson2.JSONArray;
import core.xyz.migoo.extractor.Extractor;
import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.testelement.MiGooProperty;
import core.xyz.migoo.variable.MiGooVariables;
import org.junit.jupiter.api.Test;

import static core.xyz.migoo.extractor.AbstractExtractor.FIELD;
import static core.xyz.migoo.extractor.AbstractExtractor.VALUE;

/**
 * @author xiaomi
 * Created in 2021/10/17 20:27
 */
public class ResultExtractorTest {

    @Test
    public void testResultExtractor() {
        ResultExtractor extractor = new ResultExtractor();
        extractor.setVariables(new MiGooVariables());
        extractor.setProperty("variable_name", "vaa");
        SampleResult sampleResult = new SampleResult("test");
        sampleResult.setResponseData("aaaa");
        SampleResult result = extractor.process(sampleResult);
        assert extractor.get("value").equals("aaaa");
        assert extractor.getVariables().get("vaa").equals("aaaa");
        assert result.getSamplerData().equals(getSamplerData(extractor.getProperty(), extractor));
        assert result.getTitle().equals("ResultExtractor");
    }

    private String getSamplerData(MiGooProperty property, Extractor extractor) {
        JSONArray items = new JSONArray();
        items.add("Extractor: " + extractor.getClass());
        items.add(FIELD + ": " + property.get(FIELD));
        items.add("Result: " + property.get(VALUE));
        return items.toJSONString();
    }
}
