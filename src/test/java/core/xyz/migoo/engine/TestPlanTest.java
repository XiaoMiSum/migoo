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

package core.xyz.migoo.engine;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import components.xyz.migoo.assertions.HTTPResponseAssertion;
import components.xyz.migoo.assertions.JSONAssertion;
import components.xyz.migoo.extractor.JSONExtractor;
import core.xyz.migoo.testelement.TestElement;
import core.xyz.migoo.testelement.TestElementService;
import org.junit.jupiter.api.Test;
import protocol.xyz.migoo.http.config.HttpDefaults;
import protocol.xyz.migoo.jdbc.config.DataSourceElement;
import protocol.xyz.migoo.jdbc.processor.JDBCPostProcessor;
import protocol.xyz.migoo.jdbc.processor.JDBCPreProcessor;

/**
 * @author xiaomi
 * Created in 2021/10/18 09:54
 */
public class TestPlanTest {

    @Test
    public void testPlan1() {
        assertPlan(getPlan1());
    }

    @Test
    public void testPlan2() {
        assertPlan(getPlan2());
    }

    private void assertPlan(JSONObject json) {
        TestPlan plan = new TestPlan(json);
        assert plan.getPropertyAsString("title").equals("标准测试用例");
        assert plan.getVariables().getProperty().get("var_1").equals(getVariables().get("var_1"));
        assert plan.getConfigElements().size() == getTestElements().size();
        assert plan.getConfigElements().get(0) instanceof DataSourceElement;
        assert plan.getConfigElements().get(0).get("username").equals("root");
        assert plan.getConfigElements().get(1) instanceof HttpDefaults;
        assert plan.getConfigElements().get(1).get("method").equals("post");
        assert plan.getPreprocessors().size() == 0;
        assert plan.getPostprocessors().size() == 0;
        assert plan.getChildTestElements().size() == 1;
        for (TestElement testElement : plan.getChildTestElements()) {
            assert testElement instanceof TestPlan;
            TestPlan plan2 = (TestPlan) testElement;
            assert plan2.getPropertyAsString("title").equals("标准HTTP取样器");
            assert plan2.getVariables().getProperty().get("var_1").equals(getVariables().get("var_1"));
            assert plan2.getChildTestElements().size() == (getExtractors().size() + getValidators().size());
            for (TestElement element : plan2.getChildTestElements()) {
                if (element instanceof JSONExtractor) {
                    assert element.get("variable_name").equals("status") || element.get("variable_name").equals("message");
                } else if (element instanceof JSONAssertion) {
                    assert element.get("field").equals("$.status");
                } else if (element instanceof HTTPResponseAssertion) {
                    assert element.get("field").equals("status");
                } else {
                    assert false;
                }
            }
            assert plan2.getConfigElements().size() == 0;
            assert plan2.getPreprocessors().size() == getPreProcessors().size();
            assert plan2.getPreprocessors().get(0) instanceof JDBCPreProcessor;
            assert plan2.getPreprocessors().get(0).get("datasource").equals("JDBCDataSource_var");
            assert plan2.getPostprocessors().size() == getPostProcessors().size();
            assert plan2.getPostprocessors().get(0) instanceof JDBCPostProcessor;
            assert plan2.getPostprocessors().get(0).get("datasource").equals("JDBCDataSource_var");
            assert plan2.get("config").equals(getHttpConfig());
            assert plan2.traverseInto().getSampler().getClass()
                    .equals(TestElementService.getService(getData().getString("testclass")).getClass());
        }
    }

    private JSONObject getPlan1() {
        JSONObject yaml = new JSONObject();
        yaml.put("title", "标准测试用例");
        yaml.put("variables", getVariables());
        yaml.put("testelements", getTestElements());
        JSONArray childs = new JSONArray();
        childs.add(getData());
        yaml.put("childs", childs);
        return yaml;
    }

    private JSONObject getPlan2() {
        JSONObject yaml = new JSONObject();
        yaml.put("title", "标准测试用例");
        yaml.put("variables", getVariables());
        yaml.put("configelements", getTestElements());
        JSONArray childs = new JSONArray();
        childs.add(getData());
        yaml.put("childs", childs);
        return yaml;
    }

    private JSONArray getTestElements() {
        JSONArray testelements = new JSONArray();
        JSONObject JDBCDataSource = new JSONObject();
        JDBCDataSource.put("testclass", "JDBCDataSource");
        JDBCDataSource.put("variable_name", "JDBCDataSource_var");
        JDBCDataSource.put("url", "jdbc:mysql://127.0.0.1:3306/waaagh?characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2b8&failOverReadOnly=false");
        JDBCDataSource.put("username", "root");
        JDBCDataSource.put("password", "123456qq");
        JDBCDataSource.put("max_active", "10");
        JDBCDataSource.put("max_wait", "60000");
        JSONObject HttpDefaults = new JSONObject();
        HttpDefaults.put("testclass", "HttpDefaults");
        HttpDefaults.put("method", "post");
        HttpDefaults.put("protocol", "http");
        HttpDefaults.put("host", "migoo.xyz");
        testelements.add(JDBCDataSource);
        testelements.add(HttpDefaults);
        return testelements;
    }

    private JSONObject getData() {
        JSONObject yaml = new JSONObject();
        yaml.put("title", "标准HTTP取样器");
        yaml.put("testclass", "httpsampler");
        yaml.put("variables", getVariables());
        yaml.put("config", getHttpConfig());
        yaml.put("extractors", getExtractors());
        yaml.put("validators", getValidators());
        yaml.put("PreProcessors", getPreProcessors());
        yaml.put("PostProcessors", getPostProcessors());
        return yaml;
    }

    private JSONObject getVariables() {
        JSONObject variables = new JSONObject();
        variables.put("var_1", "1");
        variables.put("var_2", "2");
        return variables;
    }

    private JSONObject getHttpConfig() {
        JSONObject body = new JSONObject();
        body.put("userName", "migoo");
        body.put("password", "123456qq");
        body.put("sign", "__digest(${username}123456qq)");
        JSONObject config = new JSONObject();
        config.put("method", "post");
        config.put("protocol", "http");
        config.put("host", "migoo.xyz");
        config.put("api", "/api/login");
        config.put("body", body);
        return config;
    }

    private JSONArray getExtractors() {
        JSONArray extractors = new JSONArray();
        extractors.add(JSONObject.parseObject("{ \"testclass\": \"jsonextractor\", \"field\": \"$.status\", \"variable_name\": \"status\", \"match_num\": 0 }"));
        extractors.add(JSONObject.parseObject("{ \"testclass\": \"jsonextractor\", \"field\": \"$.data\", \"variable_name\": \"message\", \"match_num\": 0 }"));
        return extractors;
    }

    private JSONArray getValidators() {
        JSONArray validators = new JSONArray();
        validators.add(JSONObject.parseObject("{ \"testclass\": \"httpassertion\", \"field\": \"status\", \"expected\": 200, \"rule\": \"==\"}"));
        validators.add(JSONObject.parseObject("{ \"testclass\": \"jsonassertion\", \"field\": \"$.status\", \"expected\": 200, \"rule\": \"==\"}"));
        return validators;
    }

    private JSONArray getPreProcessors() {
        JSONArray preProcessors = new JSONArray();
        JSONObject jdbcpostprocessor = new JSONObject();
        jdbcpostprocessor.put("testclass", "jdbcpreprocessor");
        jdbcpostprocessor.put("datasource", "JDBCDataSource_var");
        jdbcpostprocessor.put("query_type", "select");
        jdbcpostprocessor.put("statement", "select * from users;");
        JSONArray extractors = new JSONArray();
        extractors.add(JSONObject.parseObject("{ \"testclass\": \"jsonextractor\", \"field\": \"$.user_name\", \"variable_name\": \"user_name\" }"));
        extractors.add(JSONObject.parseObject("{ \"testclass\": \"resultextractor\", \"variable_name\": \"result\" }"));
        extractors.add(JSONObject.parseObject("{ \"testclass\": \"regexextractor\", \"field\": \"\\\"id\\\":\\\"([0-9]+)\\\",\\\"create_\", \"variable_name\": \"r_total\", \"match_num\": 0 }"));
        jdbcpostprocessor.put("extractors", extractors);
        preProcessors.add(jdbcpostprocessor);
        return preProcessors;
    }

    private JSONArray getPostProcessors() {
        JSONArray postProcessors = new JSONArray();
        JSONObject jdbcpostprocessor = new JSONObject();
        jdbcpostprocessor.put("testclass", "jdbcpostprocessor");
        jdbcpostprocessor.put("datasource", "JDBCDataSource_var");
        jdbcpostprocessor.put("query_type", "select");
        jdbcpostprocessor.put("statement", "select * from users;");
        JSONArray extractors = new JSONArray();
        extractors.add(JSONObject.parseObject("{ \"testclass\": \"jsonextractor\", \"field\": \"$.user_name\", \"variable_name\": \"user_name\" }"));
        extractors.add(JSONObject.parseObject("{ \"testclass\": \"resultextractor\", \"variable_name\": \"result\" }"));
        extractors.add(JSONObject.parseObject("{ \"testclass\": \"regexextractor\", \"field\": \"\\\"id\\\":\\\"([0-9]+)\\\",\\\"create_\", \"variable_name\": \"r_total\", \"match_num\": 0 }"));
        jdbcpostprocessor.put("extractors", extractors);
        JSONObject syncTimer = new JSONObject();
        syncTimer.put("testclass", "def_timer");
        syncTimer.put("timeout", 5);
        postProcessors.add(jdbcpostprocessor);
        postProcessors.add(syncTimer);
        return postProcessors;
    }
}
