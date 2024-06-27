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

import core.xyz.migoo.function.KwArgs;
import core.xyz.migoo.function.LsArgs;
import org.junit.jupiter.api.Test;

/**
 * @author xiaomi
 * Created in 2021/11/3 15:50
 */
public class UrlDecodeTest {

    @Test
    public void testUrlDecode1() {
        LsArgs args = new LsArgs(null);
        args.add("%25E4%25BD%25A0%25E5%25A5%25BD");
        assert "你好".equals(new UrlDecode().execute(args));
    }

    @Test
    public void testUrlDecode2() {
        KwArgs args = new KwArgs(null);
        args.put("content=%E4%BD%A0%E5%A5%BD");
        assert "你好".equals(new UrlDecode().execute(args));
    }
}
