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
import core.xyz.migoo.samplers.SampleResult;
import core.xyz.migoo.testelement.TestElement;
import core.xyz.migoo.variables.MiGooVariables;

import java.util.ArrayList;

public class LoopEngine extends AbstractTestEngine {

    public LoopEngine(TestPlan plan) {
        super(plan);
    }

    @Override
    public SampleResult run() {
        SampleResult result = new SampleResult(plan.getPropertyAsString(TITLE));
        result.setSubResults(new ArrayList<>());
        result.sampleStart();
        try {
            super.convertVariable();
            super.preprocess(result.getSubResults());
            for (TestElement element : getChildTestElements()) {
                TestPlan testPlan = (TestPlan) element;
                TestEngine engine = testPlan.level() == 0 ? new LoopEngine(testPlan) : new StandardEngine(testPlan, getVariables());
                engine.mergeVariable(getVariables());
                SampleResult subResult = engine.run();
                result.getSubResults().add(subResult);
                if (!subResult.isSuccessful()) {
                    result.setSuccessful(false);
                    if ((engine instanceof StandardEngine)) {
                        break;
                    }
                }
            }
            super.postprocess(result.getSubResults());
        } catch (Exception e) {
            result.setSuccessful(false);
            result.setResponseData(e);
        } finally {
            super.testEnded();
        }
        result.sampleEnd();
        return result;
    }
}