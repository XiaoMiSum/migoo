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
import io.github.xiaomisum.ryze.component.assertion.rule.IsEmpty;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author xiaomi
 * Created in 2021/10/16 16:06
 */
public class IsEmptyTest {

    IsEmpty isEmpty = new IsEmpty();

    @Test
    public void test4String() {
        // 空字符 true , expected 随便填，不校验
        assert isEmpty.assertThat("", "1");
        // 空格字符串 true
        assert isEmpty.assertThat("   ", "1");
        // 非空字符、非空格字符串 false
        assert !isEmpty.assertThat("  2333 ", "1");
    }

    @Test
    public void test4Map() {
        // 空 Map true , expected 随便填，不校验
        assert isEmpty.assertThat(new HashMap<>(), "1");
        // 非空 Map false
        assert !isEmpty.assertThat(JSONObject.parseObject("{\"key\": 1}"), "1");
    }

    @Test
    public void test4List() {
        // 空 List true , expected 随便填，不校验
        assert isEmpty.assertThat(new ArrayList<>(), "1");
        // 非空 List false
        assert !isEmpty.assertThat(JSONArray.parseArray("[1]"), "1");
    }

    @Test
    public void test4OtherObject() {
        // 空对象 true , expected 随便填，不校验
        assert isEmpty.assertThat(null, "1");
        // 非空对象 false
        assert !isEmpty.assertThat(new Object(), "1");
        // 非空对象 false
        assert !isEmpty.assertThat(1, "");
    }
}
