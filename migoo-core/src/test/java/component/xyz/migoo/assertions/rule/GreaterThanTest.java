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

import component.xyz.migoo.assertion.rule.GreaterThan;
import org.junit.jupiter.api.Test;

/**
 * @author xiaomi
 * Created in 2021/10/16 15:48
 */
public class GreaterThanTest {

    GreaterThan greaterThan = new GreaterThan();

    @Test
    public void test4StringNumber() {
        // 1 !> 1 结果 false
        assert !greaterThan.assertThat("1", "1");
        // 1 > 0 结果 true
        assert greaterThan.assertThat("1", "0");
        // 1 > 0 结果 true  字符串数字
        assert greaterThan.assertThat("1", 0);
        // 0 < 1 结果 false
        assert !greaterThan.assertThat("0", "1");
    }

    @Test
    public void test4Number() {
        // 1 !> 1 结果 false
        assert !greaterThan.assertThat(1, 1);
        // 1 > 0 结果 true
        assert greaterThan.assertThat(1, 0);
        // 0 < 1 结果 false
        assert !greaterThan.assertThat(0, 1);
    }
}
