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

package function.xyz.migoo;

import com.alibaba.fastjson2.JSONObject;
import core.xyz.migoo.function.FunctionService;
import core.xyz.migoo.function.KwArgs;
import core.xyz.migoo.function.LsArgs;
import org.junit.jupiter.api.Test;

/**
 * @author xiaomi
 * Created in 2021/10/24 11:18
 */
public class JsonTest {

    @Test
    public void testJson1() {
        JSONObject json = (JSONObject) FunctionService.execute("Json", "key1=1,key2=2");
        assert json.containsKey("key1");
        assert json.containsKey("key2");
    }

    @Test
    public void testJson2() {
        KwArgs args = new KwArgs(null);
        args.put("key1=1");
        args.put("key2=2");
        JSONObject json = new Json().execute(args);
        assert json.containsKey("key1");
        assert json.containsKey("key2");
    }

    @Test
    public void testJson3() {
        LsArgs args = new LsArgs(null);
        args.add("key1=1");
        args.add("key2=2");
        JSONObject json = new Json().execute(args);
        assert json.containsKey("key1");
        assert json.containsKey("key2");
    }
}
