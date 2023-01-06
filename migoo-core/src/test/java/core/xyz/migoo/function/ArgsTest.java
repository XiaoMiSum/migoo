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

package core.xyz.migoo.function;

import core.xyz.migoo.variable.MiGooVariables;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaomi
 * Created in 2021/10/18 13:37
 */
public class ArgsTest {

    @Test
    public void testArgs1() {
        MiGooVariables variables = new MiGooVariables();
        Args args = Args.newArgs("1,haha,{},[], xixi  ", variables);
        assert args.getCurrentVars().equals(variables);
        assert args.getBooleanValue(0);
        assert args.getNumber(0).compareTo(BigDecimal.ONE) == 0;
        assert args.getString(1).equals("haha");
        // 字符串 去除前后空格
        assert args.getString(4).equals("xixi");
        assert args.getJSONObject(2) != null;
        assert args.getJSONArray(3) != null;
    }

    @Test
    public void testArgs2() {
        MiGooVariables variables = new MiGooVariables();
        Map<String, Object> map = new HashMap<>(16);
        map.put("test1", "test1");
        map.put("test2", "test2");
        variables.put("map", map);
        Args args = Args.newArgs("1,haha,${map},[]", variables);
        assert args.getCurrentVars().equals(variables);
        assert args.getBooleanValue(0);
        assert args.getNumber(0).compareTo(BigDecimal.ONE) == 0;
        assert args.getString(1).equals("haha");
        assert args.getJSONObject(2).equals(map);
        assert args.getJSONArray(3) != null;
    }

    @Test
    public void testArgs3() {
        Args args = Args.newArgs("1", new MiGooVariables());
        assert args.getBooleanValue(0);
        assert args.getNumber(0).compareTo(BigDecimal.ONE) == 0;
        assert args.getString(0).equals("1");
        // 超出范围 字符串 解析为""， boolean 解析为 false，其他类型解析为 null
        assert args.getString(1).equals("");
        assert !args.getBooleanValue(1);
        assert args.getNumber(2) == null;
        assert args.getJSONObject(2) == null;
        assert args.getJSONArray(3) == null;
    }
}
