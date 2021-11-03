/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018. Lorem XiaoMiSum (mi_xiao@qq.com)
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
 */

package xyz.migoo.engine;

import core.xyz.migoo.engine.AbstractTestEngine;
import core.xyz.migoo.engine.TestEngine;
import core.xyz.migoo.engine.TestPlan;
import core.xyz.migoo.report.Result;
import core.xyz.migoo.testelement.TestElement;

import java.util.ArrayList;

/**
 * @author xiaomi
 */
public class LoopEngine extends AbstractTestEngine {

    public LoopEngine(TestPlan plan) {
        super(plan);
    }

    @Override
    public Result run() {
        Result result = new Result(plan.getPropertyAsString(TITLE));
        result.sampleStart();
        try {
            super.convertVariable();
            result.setPreprocessorResults(super.preprocess(new ArrayList<>()));
            result.setSubResults(new ArrayList<>(getChildTestElements().size()));
            for (TestElement element : getChildTestElements()) {
                TestPlan testPlan = (TestPlan) element;
                TestEngine engine = testPlan.level() == 0 ? new LoopEngine(testPlan) : new StandardEngine(testPlan);
                engine.mergeVariable(getVariables());
                Result subResult = engine.run();
                result.getSubResults().add(subResult);
                result.setSuccessful(!result.isSuccessful() ? result.isSuccessful() : subResult.isSuccessful());
                if(engine instanceof StandardEngine) {
                    // 标准引擎 需要将变量传递到子测试元素里面，并且如果测试结果失败的，后续也没必要再执行了
                    getVariables().mergeVariable(testPlan.getVariables());
                    if (!result.isSuccessful()) {
                        break;
                    }
                }
            }
            result.setPostprocessorResults(super.postprocess(new ArrayList<>()));
        } catch (Exception e) {
            result.setThrowable(e);
        } finally {
            result.sampleEnd();
            super.testEnded();
            result.setVariables(getVariables());
        }
        return result;
    }
}