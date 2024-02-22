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

package core.xyz.migoo.engine;

import core.xyz.migoo.report.Report;
import core.xyz.migoo.report.Result;
import core.xyz.migoo.testelement.TestElementService;

import java.util.Objects;

import static core.xyz.migoo.testelement.AbstractTestElement.TEST_CLASS;

/**
 * 脚本引擎 用于执行 testsuite 、sampler
 *
 * @author xiaomi
 */
public interface TestEngine {

    /**
     * 脚本引擎执行测试
     *
     * @param plan   测试计划
     * @param report 测试报告生成器
     * @return 测试结果
     */
    static Result runTest(Testplan plan, Report report) {
        MiGooContext context = !plan.isSampler() ? MiGooContext.create(plan) :
                MiGooContext.create(plan, TestElementService.getService(plan.getString(TEST_CLASS)));
        TestEngine engine = plan.isSampler() ? new StandardEngine(context) : new LoopEngine(context);
        Result result = engine.runTest();
        if (Objects.nonNull(report)) {
            report.generateReport(result);
        }
        return result;
    }

    /**
     * 测试引擎执行，返回执行结果
     *
     * @return 执行结果
     */
    Result runTest();
}