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

package core.xyz.migoo.testelement;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import component.xyz.migoo.assertion.HTTPResponseAssertion;
import component.xyz.migoo.assertion.JSONAssertion;
import component.xyz.migoo.assertion.ResultAssertion;
import component.xyz.migoo.extractor.JSONExtractor;
import component.xyz.migoo.extractor.RegexExtractor;
import component.xyz.migoo.extractor.ResultExtractor;
import component.xyz.migoo.timer.SyncTimer;
import core.xyz.migoo.variable.MiGooVariables;
import org.junit.jupiter.api.Test;
import protocol.xyz.migoo.http.config.HttpDefaults;
import protocol.xyz.migoo.http.processer.HttpPostProcessor;
import protocol.xyz.migoo.http.processer.HttpPreProcessor;
import protocol.xyz.migoo.http.sampler.HttpSampler;
import protocol.xyz.migoo.jdbc.config.DataSourceElement;
import protocol.xyz.migoo.jdbc.processor.JDBCPostProcessor;
import protocol.xyz.migoo.jdbc.processor.JDBCPreProcessor;
import protocol.xyz.migoo.jdbc.sampler.JDBCSampler;
import protocol.xyz.migoo.redis.config.RedisSourceElement;
import protocol.xyz.migoo.redis.processor.RedisPostProcessor;
import protocol.xyz.migoo.redis.processor.RedisPreProcessor;
import protocol.xyz.migoo.redis.sampler.RedisSampler;

import java.util.Map;

/**
 * @author xiaomi
 * Created in 2021/10/18 15:29
 */
public class TestElementServiceTest {

    @Test
    public void testReportService1() {
        TestElementService.addService(new TestElement() {
            @Override
            public MiGooVariables getVariables() {
                return null;
            }

            @Override
            public void setVariables(MiGooVariables variables) {

            }


            @Override
            public void setProperties(Map<String, Object> props) {

            }

            @Override
            public void setProperty(String key, Object value) {

            }

            @Override
            public MiGooProperty getProperty() {
                return null;
            }

            @Override
            public Object removeProperty(String key) {
                return null;
            }

            @Override
            public byte[] getPropertyAsByteArray(String key) {
                return new byte[0];
            }

            @Override
            public boolean getPropertyAsBoolean(String key) {
                return false;
            }

            @Override
            public long getPropertyAsLong(String key) {
                return 0;
            }

            @Override
            public int getPropertyAsInt(String key) {
                return 0;
            }

            @Override
            public float getPropertyAsFloat(String key) {
                return 0;
            }

            @Override
            public double getPropertyAsDouble(String key) {
                return 0;
            }

            @Override
            public String getPropertyAsString(String key) {
                return null;
            }

            @Override
            public JSONObject getPropertyAsJSONObject(String key) {
                return null;
            }

            @Override
            public JSONArray getPropertyAsJSONArray(String key) {
                return null;
            }

            @Override
            public MiGooProperty getPropertyAsMiGooProperty(String key) {
                return null;
            }

            @Override
            public Object get(String key) {
                return null;
            }

            @Override
            public Object get(String key, Object defaultValue) {
                return null;
            }
        }.getClass());
    }

    @Test
    public void testReportService2() {
        assert TestElementService.getService("DataSourceElement") instanceof DataSourceElement;
        assert TestElementService.getService("DatasourceElement") instanceof DataSourceElement;
        assert TestElementService.getService("jdbcdatasource") instanceof DataSourceElement;
        assert TestElementService.getService("Jdbcdatasource") instanceof DataSourceElement;
        assert TestElementService.getService("jdbc_datasource") instanceof DataSourceElement;
        assert TestElementService.getService("Jdbc_datasource") instanceof DataSourceElement;

        assert TestElementService.getService("JDBCPreProcessor") instanceof JDBCPreProcessor;
        assert TestElementService.getService("jDBCPreProcessor") instanceof JDBCPreProcessor;

        assert TestElementService.getService("JDBCPostProcessor") instanceof JDBCPostProcessor;
        assert TestElementService.getService("jDBCPostProcessor") instanceof JDBCPostProcessor;

        assert TestElementService.getService("JDBCSampler") instanceof JDBCSampler;
        assert TestElementService.getService("jDBCSampler") instanceof JDBCSampler;

        assert TestElementService.getService("RedisSourceElement") instanceof RedisSourceElement;
        assert TestElementService.getService("redissourceElement") instanceof RedisSourceElement;
        assert TestElementService.getService("RedisDataSource") instanceof RedisSourceElement;
        assert TestElementService.getService("redisDataSource") instanceof RedisSourceElement;
        assert TestElementService.getService("Redis_DataSource") instanceof RedisSourceElement;
        assert TestElementService.getService("redis_DataSource") instanceof RedisSourceElement;

        assert TestElementService.getService("RedisPreProcessor") instanceof RedisPreProcessor;
        assert TestElementService.getService("redisPreProcessor") instanceof RedisPreProcessor;

        assert TestElementService.getService("RedisPostProcessor") instanceof RedisPostProcessor;
        assert TestElementService.getService("redisPostProcessor") instanceof RedisPostProcessor;

        assert TestElementService.getService("RedisSampler") instanceof RedisSampler;
        assert TestElementService.getService("redisSampler") instanceof RedisSampler;

        assert TestElementService.getService("httpDefaults") instanceof HttpDefaults;
        assert TestElementService.getService("HttpDefaults") instanceof HttpDefaults;
        assert TestElementService.getService("httpDefault") instanceof HttpDefaults;
        assert TestElementService.getService("HttpDefault") instanceof HttpDefaults;
        assert TestElementService.getService("http_default") instanceof HttpDefaults;
        assert TestElementService.getService("Http_default") instanceof HttpDefaults;
        assert TestElementService.getService("Http_Defaults") instanceof HttpDefaults;
        assert TestElementService.getService("httpDefaults") instanceof HttpDefaults;

        assert TestElementService.getService("HttpPreProcessor") instanceof HttpPreProcessor;
        assert TestElementService.getService("httpPreProcessor") instanceof HttpPreProcessor;

        assert TestElementService.getService("HttpPostProcessor") instanceof HttpPostProcessor;
        assert TestElementService.getService("httpPostProcessor") instanceof HttpPostProcessor;

        assert TestElementService.getService("HttpSampler") instanceof HttpSampler;
        assert TestElementService.getService("httpSampler") instanceof HttpSampler;

        assert TestElementService.getService("JSONAssertion") instanceof JSONAssertion;
        assert TestElementService.getService("JsonAssertion") instanceof JSONAssertion;
        assert TestElementService.getService("json_assertion") instanceof JSONAssertion;
        assert TestElementService.getService("jSON_assertion") instanceof JSONAssertion;

        assert TestElementService.getService("HTTPResponseAssertion") instanceof HTTPResponseAssertion;
        assert TestElementService.getService("HttpResponseAssertion") instanceof HTTPResponseAssertion;
        assert TestElementService.getService("HTTP_Assertion") instanceof HTTPResponseAssertion;
        assert TestElementService.getService("HTTPAssertion") instanceof HTTPResponseAssertion;
        assert TestElementService.getService("HttpAssertion") instanceof HTTPResponseAssertion;

        assert TestElementService.getService("ResultAssertion") instanceof ResultAssertion;
        assert TestElementService.getService("resultassertion") instanceof ResultAssertion;
        assert TestElementService.getService("Result_Assertion") instanceof ResultAssertion;
        assert TestElementService.getService("result_assertion") instanceof ResultAssertion;

        assert TestElementService.getService("JSONExtractor") instanceof JSONExtractor;
        assert TestElementService.getService("jsonExtractor") instanceof JSONExtractor;
        assert TestElementService.getService("json_extractor") instanceof JSONExtractor;
        assert TestElementService.getService("JSON_extractor") instanceof JSONExtractor;

        assert TestElementService.getService("RegexExtractor") instanceof RegexExtractor;
        assert TestElementService.getService("regexExtractor") instanceof RegexExtractor;
        assert TestElementService.getService("Regex_extractor") instanceof RegexExtractor;
        assert TestElementService.getService("regex_extractor") instanceof RegexExtractor;

        assert TestElementService.getService("ResultExtractor") instanceof ResultExtractor;
        assert TestElementService.getService("resultExtractor") instanceof ResultExtractor;
        assert TestElementService.getService("result_extractor") instanceof ResultExtractor;
        assert TestElementService.getService("Result_extractor") instanceof ResultExtractor;

        assert TestElementService.getService("SyncTimer") instanceof SyncTimer;
        assert TestElementService.getService("syncTimer") instanceof SyncTimer;
        assert TestElementService.getService("Timer") instanceof SyncTimer;
        assert TestElementService.getService("timer") instanceof SyncTimer;
        assert TestElementService.getService("sync_timer") instanceof SyncTimer;
        assert TestElementService.getService("Sync_timer") instanceof SyncTimer;
        assert TestElementService.getService("def_timer") instanceof SyncTimer;
        assert TestElementService.getService("Def_timer") instanceof SyncTimer;
        assert TestElementService.getService("defTimer") instanceof SyncTimer;
        assert TestElementService.getService("deftimer") instanceof SyncTimer;

    }

    @Test
    public void testReportService3() {
        try {
            TestElementService.getService("standardreport1");
        } catch (Exception e) {
            assert e.getMessage().equals("No matching test element: " + "standardreport1");
        }
    }
}
