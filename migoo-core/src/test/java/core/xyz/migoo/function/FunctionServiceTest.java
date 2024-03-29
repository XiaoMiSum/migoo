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

/**
 * @author xiaomi
 * Created in 2021/10/18 11:30
 */
public class FunctionServiceTest {

    @Test
    public void testFunction1() {
        FunctionService.addService(args -> "new func");
    }

    @Test
    public void testFunction2() {
        Object object = FunctionService.execute("random", "");
        assert object != null;
    }

    @Test
    public void testFunction3() {
        MiGooVariables variables = new MiGooVariables();
        variables.put("test", "23");
        Object object = FunctionService.execute("random", "${test}", variables);
        assert object != null && (Integer) object >= 0 && (Integer) object <= 23;
    }

    @Test
    public void testFunction4() {
        Object object = FunctionService.execute("randoM", "");
        assert object != null;
    }

    @Test
    public void testFunction5() {
        try {
            FunctionService.execute("randoM1", "");
        } catch (Exception e) {
            assert e.getMessage().equals("No matcher the function: randoM1");
        }
    }
}
