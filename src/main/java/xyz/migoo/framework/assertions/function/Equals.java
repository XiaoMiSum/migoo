/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
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


package xyz.migoo.framework.assertions.function;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author xiaomi
 * @date 2019-08-13 22:17
 */
@Alias(aliasList = {"==", "===", "eq", "equal", "equals", "is"})
public class Equals extends AbstractAssertFunction {

    @Override
    public boolean assertTrue(Map<String, Object> data) {
        Object actual = data.get("actual");
        Object expect = data.get("expect");
        String str1 = objectToString(actual);
        String str2 = objectToString(expect);
        if (actual instanceof Number || expect instanceof Number){
            str1 = "null".equals(str1) ? "0" : str1;
            str2 = "null".equals(str2) ? "0" : str2;
            return new BigDecimal(str1).compareTo(new BigDecimal(str2)) == 0;
        }
        return str1.equals(str2);
    }
}
