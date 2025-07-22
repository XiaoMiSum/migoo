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

import core.xyz.migoo.ApplicationConfig;
import core.xyz.migoo.context.ContextWrapper;

/**
 * @author xiaomi
 */
public interface Function {

    static Object execute(ContextWrapper context, String fName, String parameter) {
        var function = ApplicationConfig.getFunctionKeyMap().get(fName);
        if (function == null) {
            throw new RuntimeException("没有匹配的函数: " + fName);
        }
        var args = Args.newArgs(context, parameter);
        return function.execute(args);
    }

    /**
     * 扩展函数执行，返回生成的数据
     *
     * @param args 扩展函数参数
     * @return 生成的数据
     */
    Object execute(Args args);

}
