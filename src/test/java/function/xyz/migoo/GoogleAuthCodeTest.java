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

/**
 * @author xiaomi
 * Created in 2021/10/24 11:13
 */
public class GoogleAuthCodeTest {

    @Test
    public void testGoogleAuthCode1() {
        int i = 0;
        try {
            new GoogleAuthCode().execute(new Args(null));
        } catch (Exception e) {
            assert "secretKey con not be null".equals(e.getMessage());
            i++;
        }
        assert i > 0;
    }

    @Test
    public void testGoogleAuthCode2() {
        int i = 0;
        try {
            Args args = new Args(null);
            args.add("");
            new GoogleAuthCode().execute(args);
        } catch (Exception e) {
            assert "secretKey con not be null".equals(e.getMessage());
            i++;
        }
        assert i > 0;
    }

    @Test
    public void testGoogleAuthCode3() {
        Args args = new Args(null);
        args.add("aeqeqweqwe");
        assert new GoogleAuthCode().execute(args) != null;
    }
}
