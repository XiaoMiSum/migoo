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
    public void test4String() {
        Args args = new Args(null);
        args.add("{\"key1\": 1, \"key2\": 2}");
        args.add("$.key2");
        Object value = new JsonRead().execute(args);
        assert value.toString().equals("2");
    }

    @Test
    public void test4Map() {
        Args args = new Args(null);
        Map<String, Object> map = new HashMap<>(16);
        map.put("key1", "1");
        map.put("key2", "2");
        args.add(map);
        args.add("$.key2");
        Object value = new JsonRead().execute(args);
        assert value.toString().equals("2");
    }

    @Test
    public void test4List() {
        Args args = new Args(null);
        List<Object> list = new ArrayList<>();
        list.add("1");
        list.add("{\"key1\": 1, \"key2\": 2}");
        args.add(list);
        args.add("$[0]");
        Object value = new JsonRead().execute(args);
        assert value.toString().equals("1");
    }

    @Test
    public void test4Object() {
        Args args = new Args(null);
        args.add(new TestObj("1", "2"));
        args.add("$.key2");
        Object value = new JsonRead().execute(args);
        assert value.toString().equals("2");
    }

    @Test
    public void test4Exception1() {
        try {
            Args args = new Args(null);
            Object value = new JsonRead().execute(args);
        } catch (Exception e) {
            assert "args is empty or invalid args.".equals(e.getMessage());
        }
    }

    @Test
    public void test4Exception2() {
        try {
            Args args = new Args(null);
            args.add(new TestObj("1", "2"));
            Object value = new JsonRead().execute(args);
        } catch (Exception e) {
            assert "args is empty or invalid args.".equals(e.getMessage());
        }
    }

    @Test
    public void test4Exception3() {
        try {
            Args args = new Args(null);
            args.add("");
            args.add("11");
            Object value = new JsonRead().execute(args);
        } catch (Exception e) {
            assert "json or jsonpath con not be null".equals(e.getMessage());
        }
    }

    @Test
    public void test4Exception4() {
        try {
            Args args = new Args(null);
            args.add(null);
            args.add("11");
            Object value = new JsonRead().execute(args);
        } catch (Exception e) {
            assert "json or jsonpath con not be null".equals(e.getMessage());
        }
    }

    @Test
    public void test4Exception5() {
        try {
            Args args = new Args(null);
            args.add("11");
            args.add("");
            Object value = new JsonRead().execute(args);
        } catch (Exception e) {
            assert "json or jsonpath con not be null".equals(e.getMessage());
        }
    }

    @Test
    public void test4Exception6() {
        try {
            Args args = new Args(null);
            args.add("11");
            args.add(null);
            Object value = new JsonRead().execute(args);
        } catch (Exception e) {
            assert "json or jsonpath con not be null".equals(e.getMessage());
        }
    }

    private static class TestObj {

        private final String key1;
        private final String key2;

        public TestObj(String key1, String key2) {
            this.key1 = key1;
            this.key2 = key2;
        }

        public String getKey1() {
            return key1;
        }

        public String getKey2() {
            return key2;
        }
    }

}
