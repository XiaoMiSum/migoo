/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018. Lorem XiaoMiSum (mi_xiao@qq.com)
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining
 *  * a copy of this software and associated documentation files (the
 *  * 'Software'), to deal in the Software without restriction, including
 *  * without limitation the rights to use, copy, modify, merge, publish,
 *  * distribute, sublicense, and/or sell copies of the Software, and to
 *  * permit persons to whom the Software is furnished to do so, subject to
 *  * the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be
 *  * included in all copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 *  * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 *  * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 *  * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 *  * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package components.xyz.migoo.extractor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONPath;
import core.xyz.migoo.extractor.AbstractExtractor;
import core.xyz.migoo.samplers.SampleResult;
import core.xyz.migoo.testelement.Alias;

import java.util.List;

/**
 * @author xiaomi
 */
@Alias(aliasList = {"JSONExtractor", "json_extractor"})
public class JSONExtractor extends AbstractExtractor {

    private static final long serialVersionUID = -5527704814281313196L;

    @Override
    public SampleResult process(SampleResult result) {
        SampleResult extractorResult = new SampleResult("JSONExtractor");
        String jsonStr = result.getResponseDataAsString();
        JSON json = (JSON) JSON.parse(jsonStr);
        if (json instanceof List && get(MATCH_NUM) != null) {
            int matchNum = getPropertyAsInt(MATCH_NUM) < 0 ? 0 : getPropertyAsInt(MATCH_NUM);
            jsonStr = ((JSONArray) json).get(matchNum).toString();
        }
        String path = getPropertyAsString(FIELD);
        Object value = JSONPath.read(jsonStr, path);
        getVariables().put(getPropertyAsString(VARIABLE_NAME), value == null ? "def_value" : value);
        getProperty().put("value", value == null ? "def_value" : value);
        extractorResult.setSamplerData(getProperty().toString());
        return extractorResult;
    }
}