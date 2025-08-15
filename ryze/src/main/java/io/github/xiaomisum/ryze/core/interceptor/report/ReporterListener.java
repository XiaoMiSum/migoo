/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2025.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

package io.github.xiaomisum.ryze.core.interceptor.report;

import io.github.xiaomisum.ryze.core.SessionRunner;
import io.github.xiaomisum.ryze.core.interceptor.RyzeInterceptor;
import io.github.xiaomisum.ryze.core.testelement.TestElement;
import org.slf4j.Logger;

/**
 * 测试报告监听器
 *
 * @author xiaomi
 * Created at 2025/7/20 14:15
 */
public interface ReporterListener<T extends TestElement<?>> extends RyzeInterceptor<T> {


    default void printStackTrace(Throwable throwable, Logger log) {
        if (throwable == null) {
            return;
        }
        log.error(throwable.getMessage(), throwable);
        try {
            if (SessionRunner.getSession().isRunInTestFrameworkSupport()) {
                throw throwable;
            }
        } catch (AssertionError | RuntimeException t) {
            throw t;
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
