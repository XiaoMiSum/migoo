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

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.github.xiaomisum.ryze.component.assertion.rule.IsNotEmpty;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author xiaomi
 * Created in 2021/10/16 16:06
 */
public class IsNotEmptyTest {

    IsNotEmpty isNotEmpty = new IsNotEmpty();

    @Test
    public void test4String() {
        // 空字符 false , expected 随便填，不校验
        assert !isNotEmpty.assertThat("", "");
        // 空格字符串 false
        assert !isNotEmpty.assertThat("   ", "");
        // 非空字符、非空格字符串 true
        assert isNotEmpty.assertThat("  2333 ", "");
    }

    @Test
    public void test4Map() {
        // 空 Map false, expected 随便填，不校验
        assert !isNotEmpty.assertThat(new HashMap<>(), "");
        // 非空 Map true
        assert isNotEmpty.assertThat(JSONObject.parseObject("{\"key\": 1}"), "");
    }

    @Test
    public void test4List() {
        // 空 List false , expected 随便填，不校验
        assert !isNotEmpty.assertThat(new ArrayList<>(), "");
        // 非空 List true
        assert isNotEmpty.assertThat(JSONArray.parseArray("[1]"), "");
    }

    @Test
    public void test4OtherObject() {
        // 空对象 false , expected 随便填，不校验
        assert !isNotEmpty.assertThat(null, "");
        // 非空对象 true
        assert isNotEmpty.assertThat(new Object(), "");
        // 非空对象 true
        assert isNotEmpty.assertThat(1, "");
    }
}
