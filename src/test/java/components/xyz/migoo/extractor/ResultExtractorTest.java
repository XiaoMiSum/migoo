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
import org.junit.jupiter.api.Test;

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
        assert result.getSamplerData().equals(extractor.getProperty().toString());
        assert result.getTitle().equals("ResultExtractor");
    }
}
