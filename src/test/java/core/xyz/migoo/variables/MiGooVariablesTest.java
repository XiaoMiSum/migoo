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

package core.xyz.migoo.variables;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mi.xiao
 * @date 2021/2/22 22:13
 */
public class MiGooVariablesTest {

    /**
     * 测试变量转换
     * 1、引用变量
     * 2、扩展函数
     * 3、字符串内有多个函数（无参）
     * 4、字符串内有多个函数（有参）
     * 5、非__开头、非)结尾
     */
    @Test
    public void testConvertVariable() {
        MiGooVariables variables = new MiGooVariables();
        variables.put("username", "migoo");
        variables.put("password", "${username}");
        variables.put("uuid", "__uuid()");
        variables.put("sign", "__random()___timestamp()");
        variables.put("f_v1", "__random(10)___timestamp()");
        variables.put("f_v2", "11__random(10)___timestamp()22");
        variables.convertVariable();
        Assertions.assertEquals("migoo", variables.get("password"));
        Assertions.assertNotEquals("__uuid()", variables.get("uuid"));
        Assertions.assertNotEquals("__random()___timestamp()", variables.get("sign"));
        Assertions.assertNotEquals("__random(10)___timestamp()", variables.get("f_v1"));
        Assertions.assertNotEquals("11__random(10)___timestamp()22", variables.get("f_v2"));
    }

    /**
     * 变量替换，字符串
     */
    @Test
    public void testConvertStringVariable() throws Exception {
        MiGooVariables variables = new MiGooVariables();
        variables.put("username", "migoo");
        variables.put("password", "123456");
        variables.put("uuid", "__uuid()");

        JSONObject data = new JSONObject();
        data.put("username", "${username}");
        data.put("password", "${password}");
        data.put("uuid", "${uuid}");
        variables.convertVariables(data);

        Assertions.assertEquals(data.get("username"), variables.get("username"));
        Assertions.assertEquals(data.get("password"), variables.get("password"));
        Assertions.assertTrue(data.getString("uuid").contains("-"));
        Assertions.assertNotEquals("__uuid()", data.get("uuid"));
    }

    /**
     * 变量替换，对象
     */
    @Test
    public void testConvertObjectVariable() throws Exception {
        List<String> list = new ArrayList<>();
        MiGooVariables variables = new MiGooVariables();
        variables.put("list", list);
        list.add("test");
        list.add("migoo");
        Map<String, String> map = new HashMap<>(2);
        map.put("test", "1");
        map.put("migoo", "2");
        variables.put("map", map);

        JSONObject data = new JSONObject();
        data.put("username", "${list}");
        data.put("map", "${map}");
        variables.convertVariables(data);
        Assertions.assertEquals(data.get("username"), variables.get("list"));
        Assertions.assertEquals(data.get("username").getClass(), variables.get("list").getClass());
        Assertions.assertEquals(data.get("map"), variables.get("map"));
        Assertions.assertEquals(data.get("map").getClass(), variables.get("map").getClass());
    }

    /**
     * Map对象变量替换
     */
    @Test
    public void testMapConvertVariable() throws Exception {
        MiGooVariables variables = new MiGooVariables();
        variables.put("test", "list");
        JSONObject data = new JSONObject();
        data.put("username", new JSONObject());
        data.getJSONObject("username").put("test1", "${test}");
        data.getJSONObject("username").put("test2", "${test}");
        variables.convertVariables(data);
        Assertions.assertEquals(data.get("username").getClass(), JSONObject.class);
        Assertions.assertEquals(data.getJSONObject("username").get("test1"), variables.get("test"));
        Assertions.assertEquals(data.getJSONObject("username").get("test2"), variables.get("test"));
    }

    /**
     * List对象变量替换
     */
    @Test
    public void testListConvertVariable() throws Exception {
        MiGooVariables variables = new MiGooVariables();
        variables.put("test", "list");
        JSONObject data = new JSONObject();
        data.put("username", new JSONArray());
        data.getJSONArray("username").add("${test}");
        data.getJSONArray("username").add("${test}");
        variables.convertVariables(data);
        Assertions.assertEquals(data.get("username").getClass(), JSONArray.class);
        Assertions.assertEquals(data.getJSONArray("username").get(0), variables.get("test"));
        Assertions.assertEquals(data.getJSONArray("username").get(1), variables.get("test"));
    }
}
