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

package function.xyz.migoo;

import core.xyz.migoo.function.Args;
import core.xyz.migoo.function.KwArgs;
import core.xyz.migoo.function.LsArgs;
import core.xyz.migoo.variable.MiGooVariables;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaomi
 * Created in 2021/10/24 11:30
 */
public class JsonReadTest {

    @Test
    public void test4String1() {
        // json 参数为 字符串 使用变量替换
        MiGooVariables variables = new MiGooVariables();
        variables.put("map", "{\"key1\": 1, \"key2\": 2}");

        KwArgs args = new KwArgs(variables);
        args.put("json=${map}");
        args.put("path=$.key2");
        Object value = new JsonRead().execute(args);
        assert value.toString().equals("2");
    }

    @Test
    public void test4String2() {
        // json 参数为 字符串 可直接使用 LsArgs
        LsArgs args = new LsArgs(null);
        args.add("{\"key1\": 1, \"key2\": 2}");
        args.add("$.key2");
        Object value = new JsonRead().execute(args);
        assert value.toString().equals("2");
    }

    @Test
    public void test4Map1() {
        Map<String, Object> map = new HashMap<>(16);
        map.put("key1", "1");
        map.put("key2", "2");
        MiGooVariables variables = new MiGooVariables();
        variables.put("map", map);

        KwArgs args = new KwArgs(variables);
        args.put("json=${map}");
        args.put("path=$.key2");
        Object value = new JsonRead().execute(args);
        assert value.toString().equals("2");
    }

    @Test
    public void test4Map2() {
        LsArgs args = new LsArgs(null);
        Map<String, Object> map = new HashMap<>(16);
        map.put("key1", "1");
        map.put("key2", "2");
        args.add(map);
        args.add("$.key2");
        Object value = new JsonRead().execute(args);
        assert value.toString().equals("2");
    }

    @Test
    public void test4List1() {
        List<Object> list = new ArrayList<>();
        list.add("1");
        list.add("{\"key1\": 1, \"key2\": 2}");
        MiGooVariables variables = new MiGooVariables();
        variables.put("list", list);

        KwArgs args = new KwArgs(variables);
        args.put("json=${list}");
        args.put("path=$[0]");
        Object value = new JsonRead().execute(args);
        assert value.toString().equals("1");
    }

    @Test
    public void test4List2() {
        LsArgs args = new LsArgs(null);
        List<Object> list = new ArrayList<>();
        list.add("1");
        list.add("{\"key1\": 1, \"key2\": 2}");
        args.add(list);
        args.add("$[0]");
        Object value = new JsonRead().execute(args);
        assert value.toString().equals("1");
    }

    @Test
    public void test4Object1() {
        MiGooVariables variables = new MiGooVariables();
        variables.put("map", new TestObj("1", "2"));

        KwArgs args = new KwArgs(variables);
        args.put("json=${map}");
        args.put("path=$.key2");
        Object value = new JsonRead().execute(args);
        assert value.toString().equals("2");
    }

    @Test
    public void test4Object2() {
        LsArgs args = new LsArgs(null);
        args.add(new TestObj("1", "2"));
        args.add("$.key2");
        Object value = new JsonRead().execute(args);
        assert value.toString().equals("2");
    }

    @Test
    public void test4Exception1() {
        var i = 0;
        try {
            Args args = new KwArgs(null);
            Object value = new JsonRead().execute(args);
        } catch (Exception e) {
            assert "args is empty or invalid args. require args 'json','path'".equals(e.getMessage());
            i++;
        }
        assert i == 1;
    }

    @Test
    public void test4Exception2() {
        var i = 0;
        try {
            Args args = new LsArgs(null);
            Object value = new JsonRead().execute(args);
        } catch (Exception e) {
            assert "args is empty or invalid args. require args 'json','path'".equals(e.getMessage());
            i++;
        }
        assert i == 1;
    }

    @Test
    public void test4Exception3() {
        var i = 0;
        try {
            KwArgs args = new KwArgs(null);
            Object value = new JsonRead().execute(args);
        } catch (Exception e) {
            assert "args is empty or invalid args. require args 'json','path'".equals(e.getMessage());
            i++;
        }
        assert i == 1;
    }

    @Test
    public void test4Exception4() {
        var i = 0;
        try {
            LsArgs args = new LsArgs(null);
            Object value = new JsonRead().execute(args);
        } catch (Exception e) {
            assert "args is empty or invalid args. require args 'json','path'".equals(e.getMessage());
            i++;
        }
        assert i == 1;
    }

    @Test
    public void test4Exception6() {
        var i = 0;
        try {
            LsArgs args = new LsArgs(null);
            args.add(null);
            args.add("$.key2");
            Object value = new JsonRead().execute(args);
        } catch (Exception e) {
            assert "json or jsonpath con not be null".equals(e.getMessage());
            i++;
        }
        assert i == 1;
    }

    @Test
    public void test4Exception7() {
        try {
            KwArgs args = new KwArgs(null);
            args.put("json=");
            args.put("path=$.key2");
            Object value = new JsonRead().execute(args);
        } catch (Exception e) {
            assert "json or jsonpath con not be null".equals(e.getMessage());
        }
    }

    @Test
    public void test4Exception8() {
        try {
            LsArgs args = new LsArgs(null);
            args.add("");
            args.add("$.key2");
            Object value = new JsonRead().execute(args);
        } catch (Exception e) {
            assert "json or jsonpath con not be null".equals(e.getMessage());
        }
    }

    @Test
    public void test4Exception9() {
        try {
            MiGooVariables variables = new MiGooVariables();
            variables.put("map", new TestObj("1", "2"));
            KwArgs args = new KwArgs(variables);
            args.put("json=${map}");
            Object value = new JsonRead().execute(args);
        } catch (Exception e) {
            assert "args is empty or invalid args. require args 'json','path'".equals(e.getMessage());
        }
    }

    @Test
    public void test4Exception10() {
        try {
            MiGooVariables variables = new MiGooVariables();
            variables.put("map", new TestObj("1", "2"));
            KwArgs args = new KwArgs(variables);
            args.put("json=${map}");
            args.put("path=");
            Object value = new JsonRead().execute(args);
        } catch (Exception e) {
            assert "json or jsonpath con not be null".equals(e.getMessage());
        }
    }

    private record TestObj(String key1, String key2) {

    }

}
