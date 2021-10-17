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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.samplers.SampleResult;
import core.xyz.migoo.variables.MiGooVariables;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author xiaomi
 * Created in 2021/10/17 17:23
 */
public class JSONExtractorTest {

    private JSONExtractor extractor;
    private static final SampleResult SAMPLE_RESULT = new SampleResult("test");
    private static final JSONObject JSON = new JSONObject();
    private JSONArray list;

    @BeforeAll
    public static void beforeAll() {
        JSON.put("key1", 1);
        JSON.put("key2", 2);
        SAMPLE_RESULT.setUrl("11111");
    }

    @BeforeEach
    public void beforeEach() {
        extractor = new JSONExtractor();
        extractor.setVariables(new MiGooVariables());
        list = new JSONArray();
    }

    @Test
    public void test4Map1() {
        SAMPLE_RESULT.setResponseData(JSON.toJSONString());
        extractor.setProperty("field", "$.key2");
        extractor.setProperty("variable_name", "vaa");
        SampleResult result = extractor.process(SAMPLE_RESULT);
        assert extractor.get("value").equals(2);
        assert extractor.getVariables().get("vaa").equals(2);
        assert result.getSamplerData().equals(extractor.getProperty().toString());
        assert result.getTitle().equals("JSONExtractor");
    }

    @Test
    public void test4Map2() {
        SAMPLE_RESULT.setResponseData(JSON.toJSONString());
        extractor.setProperty("field", "$.key3");
        extractor.setProperty("variable_name", "vaa");
        SampleResult result = extractor.process(SAMPLE_RESULT);
        // json PATH无法匹配到值 取默认值
        assert extractor.get("value").equals("def_value");
        assert extractor.getVariables().get("vaa").equals("def_value");
        assert result.getSamplerData().equals(extractor.getProperty().toString());
        assert result.getTitle().equals("JSONExtractor");
    }

    @Test
    public void test4List1() {
        this.add();
        SAMPLE_RESULT.setResponseData(list.toJSONString());
        extractor.setProperty("field", "$[1].key2");
        extractor.setProperty("variable_name", "vaa");
        SampleResult result = extractor.process(SAMPLE_RESULT);
        assert extractor.get("value").equals("b");
        assert extractor.getVariables().get("vaa").equals("b");
        assert result.getSamplerData().equals(extractor.getProperty().toString());
        assert result.getTitle().equals("JSONExtractor");
    }

    @Test
    public void test4List2() {
        this.add();
        SAMPLE_RESULT.setResponseData(list.toJSONString());
        extractor.setProperty("field", "$.key2");
        extractor.setProperty("match_num", "1");
        extractor.setProperty("variable_name", "vaa");
        SampleResult result = extractor.process(SAMPLE_RESULT);
        assert extractor.get("value").equals("b");
        assert extractor.getVariables().get("vaa").equals("b");
        assert result.getSamplerData().equals(extractor.getProperty().toString());
        assert result.getTitle().equals("JSONExtractor");
    }

    @Test
    public void test4List3() {
        this.add();
        SAMPLE_RESULT.setResponseData(list.toJSONString());
        extractor.setProperty("field", "$.key2");
        extractor.setProperty("match_num", -1);
        extractor.setProperty("variable_name", "vaa");
        SampleResult result = extractor.process(SAMPLE_RESULT);
        assert extractor.get("value").equals(2);
        assert extractor.getVariables().get("vaa").equals(2);
        assert result.getSamplerData().equals(extractor.getProperty().toString());
        assert result.getTitle().equals("JSONExtractor");
    }

    @Test
    public void test4List4() {
        this.add();
        SAMPLE_RESULT.setResponseData(list.toJSONString());
        extractor.setProperty("field", "$[1].key3");
        extractor.setProperty("variable_name", "vaa");
        SampleResult result = extractor.process(SAMPLE_RESULT);
        // json PATH无法匹配到值 取默认值
        assert extractor.get("value").equals("def_value");
        assert extractor.getVariables().get("vaa").equals("def_value");
        assert result.getSamplerData().equals(extractor.getProperty().toString());
        assert result.getTitle().equals("JSONExtractor");
    }


    private void add() {
        JSONObject json = new JSONObject();
        json.put("key1", "a");
        json.put("key2", "b");
        list.add(JSON);
        list.add(json);

    }
}
