/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
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
 *
 */

package core.xyz.migoo.vars;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.functions.FunctionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author xiaomi
 * @date 2019/9/1 18:51
 */
public class VarsHelperTest {

    JSONObject vars = new JSONObject();

    @BeforeEach
    public void init(){
        vars.clear();
        vars.put("user", "MiGoo");
        vars.put("password", "abb");
    }

    /**
     * 在字符串中使用变量占位符：${user}
     * @throws FunctionException
     */
    @Test
    public void testString1() throws FunctionException {
        Object source = VarsHelper.convertVariables("${user}", vars);
        Assertions.assertEquals(source, vars.get("user"));
    }

    /**
     * 在字符串中使用变量占位符：${user}${password}
     * @throws FunctionException
     */
    @Test
    public void testString2() throws FunctionException {
        Object source = VarsHelper.convertVariables("${user}${password}", vars);
        Assertions.assertEquals(source,vars.getString("user") + vars.get("password"));
    }

    /**
     * 在字符串中使用变量占位符：${user}${password}
     * @throws FunctionException
     */
    @Test
    public void testString3() throws FunctionException {
        Object source = VarsHelper.convertVariables("${user}---${password}", vars);
        Assertions.assertEquals(source,vars.get("user") + "---"+ vars.get("password"));
    }

    /**
     * 在字符串中使用变量占位符：haha${password}haha
     * @throws FunctionException
     */
    @Test
    public void testString4() throws FunctionException {
        Object source = VarsHelper.convertVariables("haha${password}haha", vars);
        Assertions.assertEquals(source,"haha" + vars.get("password") + "haha");
    }

    /**
     * 在字符串中使用变量占位符：haha${password}
     * @throws FunctionException
     */
    @Test
    public void testString5() throws FunctionException {
        Object source = VarsHelper.convertVariables("haha${password}", vars);
        Assertions.assertEquals(source, "haha" + vars.get("password"));
    }

    /**
     * 在字符串中使用变量占位符：${password}haha
     * @throws FunctionException
     */
    @Test
    public void testString6() throws FunctionException {
        Object source = VarsHelper.convertVariables("${password}haha", vars);
        Assertions.assertEquals(source,vars.get("password") + "haha");
    }

    /**
     * 测试在对象中使用变量占位符： {"user": "${user}", "password": "${password}"}
     * @throws FunctionException
     */
    @Test
    public void testJSONObject1() throws FunctionException {
        JSONObject source = new JSONObject();
        source.put("user", "${user}");
        source.put("password", "${password}");
        VarsHelper.convertVariables(source, vars);
        Assertions.assertEquals(source, vars);
    }

    /**
     * 测试在对象中使用变量占位符： {"user": "${user}", "password": "${password}","data": {"user": "${user}", "password": "${password}"}}
     * @throws FunctionException
     */
    @Test
    public void testJSONObject2() throws FunctionException {
        JSONObject source = new JSONObject();
        source.put("user", "${user}");
        source.put("password", "${password}");
        JSONObject data = new JSONObject();
        data.putAll(source);
        source.put("data", data);
        VarsHelper.convertVariables(source, vars);
        Assertions.assertTrue(source.get("data") instanceof JSONObject);
        Assertions.assertEquals(source.get("data"), vars);
    }

    /**
     * 测试在对象中使用变量占位符： {"user": "Migoo", "password": "abb","vars1": "${user}", "vars2": "${password}"}
     * @throws FunctionException
     */
    @Test
    public void testJSONObject3() throws FunctionException {
        JSONObject source = new JSONObject();
        source.putAll(vars);
        source.put("vars1", "${user}");
        source.put("vars2", "${password}");
        VarsHelper.convertVariables(source);
        Assertions.assertEquals(source.get("vars1"), source.getString("user"));
        Assertions.assertEquals(source.get("vars2"), source.getString("password"));
    }

    /**
     * 测试在对象中使用变量占位符： {"list": ["${user}", "1", "${password}", "3"]}
     * @throws FunctionException
     */
    @Test
    public void testJSONArray1() throws FunctionException {
        JSONObject source = new JSONObject();
        JSONArray list = new JSONArray();
        list.add("${user}");
        list.add("1");
        list.add("${password}");
        list.add("3");
        source.put("list", list);
        VarsHelper.convertVariables(source, vars);
        Assertions.assertTrue(source.get("list") instanceof JSONArray);
        Assertions.assertEquals(list.get(0), vars.get("user"));
        Assertions.assertEquals(list.get(1), "1");
        Assertions.assertEquals(list.get(2), vars.get("password"));
        Assertions.assertEquals(list.get(3), "3");
    }

    /**
     * 测试在对象中使用变量占位符： {"list": ["${user}", {"vars1", "${user}", "vars2": "${password}"}]}
     * @throws FunctionException
     */
    @Test
    public void testJSONArray2() throws FunctionException {
        JSONObject source = new JSONObject();
        JSONArray list = new JSONArray();
        source.put("list", list);
        list.add("${user}");
        JSONObject data = new JSONObject();
        data.put("vars1", "${user}");
        data.put("vars2", "${password}");
        list.add(data);
        VarsHelper.convertVariables(source, vars);
        Assertions.assertTrue(source.get("list") instanceof JSONArray);
        Assertions.assertEquals(list.get(0), vars.get("user"));
        Assertions.assertTrue(list.get(1) instanceof JSONObject);
        Assertions.assertEquals(data.get("vars1"), vars.get("user"));
        Assertions.assertEquals(data.get("vars2"), vars.get("password"));
    }

    /**
     * 测试变量转换，变量值是个对象
     * @throws FunctionException
     */
    @Test
    public void testVarValueIsObject() throws FunctionException {
        JSONObject object = new JSONObject();
        object.put("name", "migoo");
        object.put("version", "3.2.6");
        vars.put("object", object);
        Object o = VarsHelper.convertVariables("${object}", vars);
        Assertions.assertTrue(o instanceof JSONObject);
        Assertions.assertEquals(o, object);
    }

    /**
     * 测试变量转换，变量值是个数组
     * @throws FunctionException
     */
    @Test
    public void testVarValueIsArray() throws FunctionException {
        JSONArray array = new JSONArray();
        array.add("name");
        array.add("version");
        vars.put("list", array);
        Object o = VarsHelper.convertVariables("${list}", vars);
        Assertions.assertTrue(o instanceof JSONArray);
        Assertions.assertEquals(o, array);
    }

}
