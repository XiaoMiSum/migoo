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

package core.xyz.migoo.testelement;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaomi
 * Created in 2021/10/18 15:16
 */
public class MiGooPropertyTest {

    @Test
    public void testMiGooProperty1() {
        MiGooProperty property = new MiGooProperty("key1", "1");
        assert property.get("key1").equals("1");
    }

    @Test
    public void testMiGooProperty2() {
        Map<String, Object> map = new HashMap<>(16);
        map.put("key1", "1");
        MiGooProperty property = new MiGooProperty(map);
        assert property.get("key1").equals("1");
    }

    @Test
    public void testMiGooProperty3() {
        MiGooProperty property = new MiGooProperty("key1", "1");
        property.put("key2", null);
        assert property.get("key1").equals("1");
        assert property.get("key2") == null;
        property.putIfAbsent("key1", "1-1");
        assert !property.get("key1").equals("1-1");
        property.putIfAbsent("key2", null);
        assert property.get("key2") == null;
        property.putIfAbsent("key2", "2");
        assert property.get("key2") == "2";
    }

    @Test
    public void testMiGooProperty4() {
        MiGooProperty property = new MiGooProperty("key1", "1");
        property.put("key2", new HashMap<>());
        property.put("key3", new ArrayList<>());
        property.put("key4", new T1());
        property.put("key5", new T2());
        Map<String, Object> map = new HashMap<>(16);
        map.put("key1", "1");
        property.put("key6", map);
        System.out.println(property);
    }

    private class T1 {
        @Override
        public String toString() {
            return "T1.CLASS";
        }
    }

    private class T2 {
    }
}
