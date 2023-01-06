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

package component.xyz.migoo.assertions.rule;

import component.xyz.migoo.assertion.rule.DoseNotContains;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaomi
 * Created in 2021/10/13 16:21
 */
public class DoseNotContainsTest {

    DoseNotContains doseNotContains = new DoseNotContains();

    @Test
    public void test4String() {
        // 1234 包含 1 结果 false
        assert !doseNotContains.assertThat("1234", "1");
        // 1234 包含 1(Integer，expected 非字符串将转化为字符串) 结果 false
        assert !doseNotContains.assertThat("1234", 1);
        // 1111 不包含 2 结果 true
        assert doseNotContains.assertThat("1111", "2");
    }

    @Test
    public void test4Map() {
        Map<String, Object> actual = new HashMap<>(16);
        actual.put("key1", "1");
        // actual.value 包含 1 结果 false
        assert !doseNotContains.assertThat(actual, "1");
        // actual.key 包含 key1 结果 false
        assert !doseNotContains.assertThat(actual, "key1");
        // actual.value & key 不包含 2 结果 true
        assert doseNotContains.assertThat(actual, "2");
        // actual.value 不包含 1(类型不一致) 结果 true
        assert doseNotContains.assertThat(actual, 1);
    }

    @Test
    public void test4List() {
        List<String> actual = new ArrayList<>();
        actual.add("1");
        actual.add("3");
        // actual.item 包含 1 结果 false
        assert !doseNotContains.assertThat(actual, "1");
        // actual.item 不包含 2 结果 true
        assert doseNotContains.assertThat(actual, "2");
        // actual.item 不包含 1(类型不一致) 结果 true
        assert doseNotContains.assertThat(actual, 1);
    }

    @Test
    public void test4OtherType() {
        // 期望值类型不是 String\Map\List 默认 true
        assert doseNotContains.assertThat(13, "1");
    }
}
