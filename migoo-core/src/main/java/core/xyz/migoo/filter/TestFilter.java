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

package core.xyz.migoo.filter;

import core.xyz.migoo.context.ContextWrapper;

/**
 * @author xiaomi
 */
public interface TestFilter {

    /**
     * 计算当前测试元件对象是否需要应用该过滤器
     *
     * @param ctx 测试上下文
     * @return 如果对 testElement 使用当前过滤器则为 true，否则为 false。
     */
    default boolean match(ContextWrapper ctx) {
        return false;
    }

    /**
     * 执行测试元件时调用
     *
     * @param ctx   测试上下文
     * @param chain 运行过滤器链
     */
    default void doRun(ContextWrapper ctx, RunFilterChain chain) {
        chain.doRun(ctx);
    }

    /**
     * 执行测试元件具体功能逻辑时调用（前后置处理器不在内）
     *
     * @param ctx   测试上下文
     * @param chain 执行过滤器链
     */
    default void doExecute(ContextWrapper ctx, ExecuteFilterChain chain) {
        chain.doExecute(ctx);
    }

    /**
     * 执行某次循环时调用
     *
     * @param ctx   测试上下文
     * @param chain 执行子步骤过滤器链
     */
    default void doExecuteSubSteps(ContextWrapper ctx, ExecuteSubStepsFilterChain chain) {
        chain.doExecuteSubSteps(ctx);
    }

    /**
     * 执行取样逻辑时调用（一般为协议请求）
     *
     * @param ctx   测试上下文
     * @param chain 取样过滤器链
     */
    default void doSample(ContextWrapper ctx, SampleFilterChain chain) {
        chain.doSample(ctx);
    }

}
