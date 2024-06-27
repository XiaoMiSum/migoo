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
 * Created in 2021/10/24 11:51
 */
public class RandomStringTest {

    @Test
    public void testRandomString1() {
        var value = new RandomString().execute(new KwArgs(null));
        assert value.length() == 10;
    }

    @Test
    public void testRandomString2() {
        var value = new RandomString().execute(new LsArgs(null));
        assert value.length() == 10;
    }

    @Test
    public void testRandomString3() {
        KwArgs args = new KwArgs(null);
        args.put("length=20");
        String value = new RandomString().execute(args);
        assert value.length() == 20;
    }

    @Test
    public void testRandomString4() {
        LsArgs args = new LsArgs(null);
        args.add(20);
        String value = new RandomString().execute(args);
        assert value.length() == 20;
    }

    @Test
    public void testRandomString5() {
        KwArgs args = new KwArgs(null);
        args.put("length=20");
        args.put("string=ab");
        String value = new RandomString().execute(args);
        assert value.contains("a") || value.contains("b");
    }

    @Test
    public void testRandomString6() {
        LsArgs args = new LsArgs(null);
        args.add(20);
        args.add("ab");
        String value = new RandomString().execute(args);
        assert value.contains("a") || value.contains("b");
    }

    @Test
    public void testRandomString7() {
        KwArgs args = new KwArgs(null);
        args.put("length=20");
        args.put("string=ab");
        args.put("upper=true");
        String value = new RandomString().execute(args);
        assert value.contains("A") || value.contains("B");
    }

    @Test
    public void testRandomString8() {
        LsArgs args = new LsArgs(null);
        args.add(20);
        args.add("ab");
        args.add(true);
        String value = new RandomString().execute(args);
        assert value.contains("A") || value.contains("B");
    }
}
