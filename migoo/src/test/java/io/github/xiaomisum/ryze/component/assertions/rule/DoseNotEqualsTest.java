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

package io.github.xiaomisum.ryze.component.assertions.rule;

import io.github.xiaomisum.ryze.component.assertion.rule.DoseNotEquals;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaomi
 * Created in 2021/10/13 17:15
 */
public class DoseNotEqualsTest {

    DoseNotEquals doseNotEquals = new DoseNotEquals();

    @Test
    public void test4String() {
        // "" 空字符串 等同于 null 结果 false
        assert !doseNotEquals.assertThat("", null);
        // "   " 空白字符串 等同于 null 结果 false
        assert !doseNotEquals.assertThat("    ", null);
        // "1" == "1" 结果 false
        assert !doseNotEquals.assertThat("1", "1");
        // "1" == 1 结果 false  非字符串先转换为字符串再进行对比
        assert !doseNotEquals.assertThat("1", 1);
        // 1 == "1" 结果 false  非字符串先转换为字符串再进行对比
        assert !doseNotEquals.assertThat(1, "1");
        // "null" == null 结果 false
        assert !doseNotEquals.assertThat("null", null);

        // 1 != 2 结果 true
        assert doseNotEquals.assertThat("1", "2");
        // A != a 结果 true
        assert doseNotEquals.assertThat("A", "a");
    }

    @Test
    public void test4Number() {
        // 1 == 1 结果 false
        assert !doseNotEquals.assertThat(1, 1);
        // 1.0 == 1.00 结果 false 数值对比
        assert !doseNotEquals.assertThat(1.0, 1.00);
        assert !doseNotEquals.assertThat(new BigDecimal("1.0"), new BigDecimal("1.00"));
        // 数字对比，null 当作 0 结果 false
        assert !doseNotEquals.assertThat(new BigDecimal("0"), null);
        assert !doseNotEquals.assertThat(0, null);
        assert !doseNotEquals.assertThat(0L, null);
        assert !doseNotEquals.assertThat(0.00F, null);
        assert !doseNotEquals.assertThat(0.00D, null);
        assert !doseNotEquals.assertThat(null, new BigDecimal("0.00"));
        assert !doseNotEquals.assertThat(null, 0);
        assert !doseNotEquals.assertThat(null, 0L);
        assert !doseNotEquals.assertThat(null, 0.00F);
        assert !doseNotEquals.assertThat(null, 0.00D);

        // 1 != 2 结果 true
        assert doseNotEquals.assertThat(1, 2);
    }


    @Test
    public void test4Map() {
        Map<String, String> actual = new HashMap<>();
        actual.put("1", "1");
        Map<String, String> expected = new HashMap<>();
        expected.put("1", "1");
        // Map 内容一致 结果 false
        assert !doseNotEquals.assertThat(actual, expected);
        Map<String, Object> expected2 = new HashMap<>();
        expected2.put("1", "1");
        // Map 内容一致 结果 false
        assert !doseNotEquals.assertThat(actual, expected2);
        // 空Map == null 结果 false
        assert !doseNotEquals.assertThat(new HashMap<>(), null);
        Map<String, Object> expected3 = new HashMap<>();
        expected3.put("1", Integer.valueOf("1"));
        // Map 内容不一致 结果 true
        assert doseNotEquals.assertThat(actual, expected3);
        expected.put("2", "2");
        assert doseNotEquals.assertThat(actual, expected);
    }

    @Test
    public void test4List() {
        List<String> actual = new ArrayList<>();
        actual.add("1");
        List<String> expected = new ArrayList<>();
        expected.add("1");
        // List 内容一致 结果 false
        assert !doseNotEquals.assertThat(actual, expected);
        List<Object> expected2 = new ArrayList<>();
        expected2.add("1");
        // List 内容一致 结果 false
        assert !doseNotEquals.assertThat(actual, expected2);
        List<Object> expected3 = new ArrayList<>();
        expected3.add(Integer.valueOf("1"));
        // 空List == null 结果 false
        assert !doseNotEquals.assertThat(new ArrayList<>(), null);
        // List 内容不一致 结果 true
        assert doseNotEquals.assertThat(actual, expected3);
        expected.add("2");
        assert doseNotEquals.assertThat(actual, expected);
    }

    @Test
    public void test4OtherObject() {
        // 类型不一致   结果 true
        assert doseNotEquals.assertThat(new Object(), new HashMap<>());
        // 类型不一致 但长度都是 0  结果 false
        assert !doseNotEquals.assertThat(new ArrayList<>(), new HashMap<>());
        // 不同对象  结果 true
        assert doseNotEquals.assertThat(new Object(), new Object());
    }
}
