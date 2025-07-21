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

package core.xyz.migoo.filter.report;

import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.filter.RunFilterChain;
import core.xyz.migoo.filter.SampleFilterChain;
import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.testelement.TestSuiteExecutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiaomi
 * Created at 2025/7/20 13:46
 */
public class TestLogFilter implements ReportFilter {

    static final Logger log = LoggerFactory.getLogger("");

    @Override
    public void doRun(ContextWrapper context, RunFilterChain chain) {
        if (TestSuiteExecutable.class.isAssignableFrom(chain.getClass())) {
            log.info("开始测试：{}", context.getTestResult().getTitle());
        }
        chain.doRun(context);
    }

    @Override
    public void doSample(ContextWrapper context, SampleFilterChain chain) {
        chain.doSample(context);
        log.info("执行 {}：{}", chain.getClass().getSimpleName(), context.getTestResult().getTitle());
        if (context.getTestResult().isException()) {
            log.error("\n", context.getTestResult().getThrowable());
        }
        if (context.getTestResult() instanceof SampleResult result) {
            log.info("{}{}{}", "\n--------------- 请求信息 -----------------\n", result.getRequest().bytesAsString(), "\n");
            log.info("{}{}{}", "\n--------------- 响应信息 -----------------\n", result.getResponse().bytesAsString(), "\n");
        }
    }
}
