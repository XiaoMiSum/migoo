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

package io.github.xiaomisum.ryze.core.function;

import io.github.xiaomisum.ryze.core.context.ContextWrapper;

/**
 * @author xiaomi
 */
@SuppressWarnings("all")
public interface Function {
    
    String key();

    default void checkMethodArgCount(Args args, int minCnt, int maxCnt) throws RuntimeException {
        if (args.size() < minCnt || args.size() > maxCnt) {
            throw new RuntimeException("函数 " + key() + " 参数数量错误, 期望参数数量为  " + minCnt + " ~ " + maxCnt + ", 实际为 " + args.size());
        }
    }

    /**
     * 扩展函数执行，返回生成的数据
     *
     * @param args 扩展函数参数
     * @return 生成的数据
     */
    Object execute(ContextWrapper context, Args args);

}
