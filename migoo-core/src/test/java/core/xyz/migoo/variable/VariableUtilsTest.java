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

package core.xyz.migoo.variable;

import org.junit.jupiter.api.Test;

/**
 * @author xiaomi
 * Created in 2021/10/18 15:56
 */
public class VariableUtilsTest {

    @Test
    public void testIsVars() {
        assert !VariableUtils.isVars("test");
        assert !VariableUtils.isVars("{test}");
        assert !VariableUtils.isVars("${test");
        assert !VariableUtils.isVars("$test");
        assert VariableUtils.isVars("${test}");
        assert VariableUtils.isVars("${test}1111");
        assert VariableUtils.isVars("${test}${1111}");
        assert VariableUtils.isVars("${test}____${1111}");
        assert VariableUtils.isVars("${test}ad无法地方w$%%^&%*{1111}");
    }

    @Test
    public void testIsFunc() {
        assert !VariableUtils.isFunc("test");
        assert !VariableUtils.isFunc("__test");
        assert !VariableUtils.isFunc("__test(");
        assert !VariableUtils.isFunc("_test()");
        assert !VariableUtils.isFunc("test()");
        assert !VariableUtils.isFunc("__#¥%#¥%()");
        assert !VariableUtils.isFunc("__方法()");
        assert VariableUtils.isFunc("__test()");
        assert VariableUtils.isFunc("__test(123123)");
        assert VariableUtils.isFunc("__test(1,2,3)");
        assert VariableUtils.isFunc("${__test()}");
        assert VariableUtils.isFunc("${__test()}");
    }

    @Test
    public void testIsKwArgs() {
        assert !VariableUtils.isKwArgs("");
        assert !VariableUtils.isKwArgs("test");
        assert !VariableUtils.isKwArgs("test,test2");
        assert VariableUtils.isKwArgs("test=1");
        assert VariableUtils.isKwArgs("test=1,");
        assert VariableUtils.isKwArgs("test=1,test2=2");
        assert VariableUtils.isKwArgs("test=1,test2=2,");
        assert VariableUtils.isKwArgs("test=1,test2=2,test3=3");
        assert VariableUtils.isKwArgs("test=${vars1},test2=2,test3=3");
        assert VariableUtils.isKwArgs("test=${vars1},test2=__func1(),test3=3");
        assert VariableUtils.isKwArgs("test=${vars1},test2=__func1(),test3=__func1()__func2(${vars1})");
    }
}
