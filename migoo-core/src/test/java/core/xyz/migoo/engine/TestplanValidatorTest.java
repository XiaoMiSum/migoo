/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2023.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

package core.xyz.migoo.engine;

import com.alibaba.fastjson2.JSONObject;
import org.junit.jupiter.api.Test;

public class TestplanValidatorTest {

    @Test
    public void testHttpSamplerJson() {
        JSONObject data = new JSONObject();
        data.put("title", "测试验证");
        JSONObject config = new JSONObject();
        data.put("config", config);
        config.put("method", "1");
        config.put("body", new JSONObject());
        data.put("testclass", "httpsampler");
        TestPlanValidator.verify(data, "httpsampler");
    }

    @Test
    public void testHttpPreprocessorJson() {
        JSONObject data = new JSONObject();
        data.put("title", "测试验证");
        data.put("method", "1");
        data.put("body", new JSONObject());
        data.put("va", new JSONObject());
        data.put("testclass", "httppreprocessor");
        TestPlanValidator.verify(data, "httppreprocessor");
    }

    @Test
    public void testHttpPostprocessorJson() {
        JSONObject data = new JSONObject();
        data.put("title", "测试验证");
        data.put("method", "1");
        data.put("body", new JSONObject());
        data.put("va", new JSONObject());
        data.put("testclass", "httppostprocessor");
        TestPlanValidator.verify(data, "httppostprocessor");
    }

    @Test
    public void testRedisSamplerJson() {
        JSONObject data = new JSONObject();
        data.put("title", "测试验证");
        JSONObject config = new JSONObject();
        data.put("config", config);
        config.put("datasource", "1");
        config.put("command", "new JSONObject()");
        config.put("send", "new JSONObject()");
        data.put("testclass", "redissampler");
        TestPlanValidator.verify(data, "redissampler");
    }

    @Test
    public void testRedisPreprocessorJson() {
        JSONObject data = new JSONObject();
        data.put("title", "测试验证");
        data.put("datasource", "1");
        data.put("command", "new JSONObject()");
        data.put("send", "new JSONObject()");
        data.put("testclass", "redispreprocessor");
        TestPlanValidator.verify(data, "redispreprocessor");
    }

    @Test
    public void testRedisPostprocessorJson() {
        JSONObject data = new JSONObject();
        data.put("title", "测试验证");
        data.put("datasource", "1");
        data.put("command", "new JSONObject()");
        data.put("send", "new JSONObject()");
        data.put("testclass", "redispreprocessor");
        TestPlanValidator.verify(data, "redispreprocessor");
    }

    @Test
    public void testJdbcSamplerJson() {
        JSONObject data = new JSONObject();
        data.put("title", "测试验证");
        JSONObject config = new JSONObject();
        data.put("config", config);
        config.put("datasource", "1");
        config.put("query_type", "insert");
        config.put("statement", "new JSONObject()");
        data.put("testclass", "jdbcsampler");
        TestPlanValidator.verify(data, "jdbcsampler");
    }

    @Test
    public void testJdbcPreprocessorJson() {
        JSONObject data = new JSONObject();
        data.put("title", "测试验证");
        data.put("datasource", "1");
        data.put("query_type", "update");
        data.put("statement", "new JSONObject()");
        data.put("testclass", "jdbcpreprocessor");
        TestPlanValidator.verify(data, "jdbcpreprocessor");
    }

    @Test
    public void testJdbcPostprocessorJson() {
        JSONObject data = new JSONObject();
        data.put("title", "测试验证");
        data.put("datasource", "1");
        data.put("query_type", "delete");
        data.put("statement", "new JSONObject()");
        data.put("testclass", "jdbcpreprocessor");
        TestPlanValidator.verify(data, "Jdbcpreprocessor");
    }
}
