/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2021. Lorem XiaoMiSum (mi_xiao@qq.com)
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
 */

package function.xyz.migoo;

import core.xyz.migoo.function.CompoundParameter;
import core.xyz.migoo.function.Function;

import java.math.BigDecimal;

public class Random implements Function {

    /**
     * 生成（伪）随机数，支持一个参数
     * 参数：
     *      bound: 最大值，允许为空
     * 当传入的bound值小于1或空值时，则不限制返回的数据（可能为正数，也可能为负数）
     */
    @Override
    public Integer execute(CompoundParameter parameters) throws Exception {
        BigDecimal bound = parameters.getBigDecimal("bound");
        java.util.Random random = new java.util.Random();
        return bound != null && bound.intValue() > 0 ? random.nextInt(bound.intValue()) : random.nextInt();
    }
}