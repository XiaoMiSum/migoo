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

import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.interceptor.Handler;
import io.github.xiaomisum.ryze.core.testelement.TestContainerExecutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiaomi
 * Created at 2025/7/20 13:46
 */
public class TestContainerLogListener implements ReporterListener {

    static final Logger log = LoggerFactory.getLogger("");

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean match(ContextWrapper context) {
        return context.getTestElement() instanceof TestContainerExecutable<?, ?, ?>;
    }

    @Override
    public void preHandle(ContextWrapper context, Handler handler) {
        log.info("开始测试：{}", context.getTestResult().getTitle());
        handler.doHandle(context);
    }
}
