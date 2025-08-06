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

package io.github.xiaomisum.ryze.core.interceptor;

import io.github.xiaomisum.ryze.core.context.ContextWrapper;

/**
 * 过滤器接口，
 *
 * @author xiaomi
 */
public interface Interceptor {

    int getOrder();

    /**
     * 计算当前测试元件对象是否需要应用该过滤器
     *
     * @param context 测试上下文
     * @return 如果对 testElement 使用当前过滤器则为 true，否则为 false。
     */
    default boolean match(ContextWrapper context) {
        return false;
    }

    /**
     * 执行测试元件时调用
     *
     * @param context 测试上下文
     * @param handler 运行过滤器链
     */
    default void preHandle(ContextWrapper context, Handler handler) {
        handler.doHandle(context);
    }


    default void postHandle(ContextWrapper context, Handler handler) {
    }
}
