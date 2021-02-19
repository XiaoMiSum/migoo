/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining
 *  * a copy of this software and associated documentation files (the
 *  * 'Software'), to deal in the Software without restriction, including
 *  * without limitation the rights to use, copy, modify, merge, publish,
 *  * distribute, sublicense, and/or sell copies of the Software, and to
 *  * permit persons to whom the Software is furnished to do so, subject to
 *  * the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be
 *  * included in all copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 *  * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 *  * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 *  * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 *  * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *
 */

package function.xyz.migoo;

import core.xyz.migoo.function.CompoundParameter;
import core.xyz.migoo.function.Function;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;

public class RandomString implements Function {

    /**
     * 获取随机字符串，支持三个参数，且三个参数都允许为空
     * 参数：
     *      length: 返回的随机字符串长度，允许为空，默认：10
     *      string: 基础字符串，允许为空，如果传了该参数，则从该参数中获取字符作为种子生成随机字符串
     *      upper: 是否将生成的字符串转为大写，允许为空，默认：false
     *
     */
    @Override
    public String execute(CompoundParameter parameters) throws Exception {
        if (parameters.isEmpty()){
            throw new Exception("parameters con not be null");
        }
        BigDecimal length = parameters.getBigDecimal("length");
        if (length == null){
            length = new BigDecimal("10");
        }
        String charsToUse = parameters.getString("string").trim();
        String str = charsToUse.isEmpty() ? RandomStringUtils.randomAlphabetic(length.intValue())
                : RandomStringUtils.random(length.intValue(), charsToUse);
        return parameters.getBooleanValue("upper") ? str.toUpperCase() : str;
    }
}
