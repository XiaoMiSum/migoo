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

import component.xyz.migoo.assertion.rule.Regex;
import org.junit.jupiter.api.Test;

/**
 * @author xiaomi
 * Created in 2021/10/16 16:25
 */
public class RegexTest {

    Regex regex = new Regex();

    @Test
    public void testRegex() {
        // 112233 == 112233 true
        assert regex.assertThat("112233", "112233");
        // \d+ 匹配一个或多个数字 true
        assert regex.assertThat("112233", "\\d+");
        // \d+ 匹配一个或多个数字 true
        assert regex.assertThat("1", "\\d+");
        // \d 匹配一个数字 12344 不完全匹配 false
        assert !regex.assertThat("12344", "\\d");
        // \d+ 匹配一个或多个数字 1ass 不完全匹配
        assert !regex.assertThat("1ass", "\\d+");
    }
}
