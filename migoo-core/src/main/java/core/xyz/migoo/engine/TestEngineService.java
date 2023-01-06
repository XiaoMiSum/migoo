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
import core.xyz.migoo.variable.MiGooVariables;

import java.util.Objects;

/**
 * @author xiaomi
 * Created at 2022/8/24 17:44
 */
public class TestEngineService {

    public static Result runTest(Testplan plan, Report report) {
        TestEngine engine = plan.isStandardSampler() ? new StandardEngine(plan) : new LoopEngine(plan);
        Result result = engine.runTest();
        if (Objects.nonNull(report)) {
            report.generateReport(result);
        }
        return result;
    }

    public static Result runTest(Testplan plan, MiGooVariables variables) {
        TestEngine engine = plan.isStandardSampler() ? new StandardEngine(plan) : new LoopEngine(plan);
        engine.mergeVariable(variables);
        return engine.runTest();
    }
}
