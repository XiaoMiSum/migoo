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

import com.alibaba.fastjson2.JSONPath;
import core.xyz.migoo.extractor.AbstractExtractor;
import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.testelement.Alias;

import java.util.List;
import java.util.Objects;

/**
 * @author xiaomi
 */
@Alias({"JSONExtractor", "json_extractor"})
public class JSONExtractor extends AbstractExtractor {

    @Override
    public SampleResult process(SampleResult result) {
        String jsonStr = result.getResponseDataAsString();
        String path = getPropertyAsString(FIELD);
        Object value = JSONPath.extract(jsonStr, path);
        if (value instanceof List && Objects.nonNull(get(MATCH_NUM))) {
            int matchNum = getPropertyAsInt(MATCH_NUM);
            value = ((List<?>) value).get(Math.max(matchNum, 0));
        }
        getVariables().put(getPropertyAsString(VARIABLE_NAME), Objects.isNull(value) ? "def_value" : value);
        getProperty().put("value", Objects.isNull(value) ? "def_value" : value);
        return getResult(new SampleResult("JSONExtractor"));
    }
}