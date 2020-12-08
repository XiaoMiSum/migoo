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

package xyz.migoo.test.example;

import components.xyz.migoo.plugins.mybatis.Mybatis;
import core.xyz.migoo.functions.CompoundVariable;
import core.xyz.migoo.functions.Function;
import core.xyz.migoo.functions.FunctionException;

/**
 * @author xiaomi
 * @date 2020/12/8 19:40
 */
public class MybatisFunc implements Function {
    @Override
    public Object execute(CompoundVariable parameters) throws FunctionException {
        Mybatis mybatis = parameters.getCurrentVars().getPlugin(Mybatis.class);
        TestMapper mapper = mybatis.mapper(TestMapper.class);
        Integer total = mapper.count();
        System.out.println("mybatis: " + total);
        return total;
    }

    @Override
    public String funcKey() {
        return "MybatisFunc";
    }
}
