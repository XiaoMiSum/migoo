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

import core.xyz.migoo.assertions.Assertion;
import core.xyz.migoo.assertions.AssertionResult;
import core.xyz.migoo.engine.AbstractTestEngine;
import core.xyz.migoo.engine.TestPlan;
import core.xyz.migoo.extractor.Extractor;
import core.xyz.migoo.samplers.SampleResult;
import core.xyz.migoo.samplers.Sampler;
import core.xyz.migoo.testelement.TestElement;
import core.xyz.migoo.testelement.TestStateListener;
import core.xyz.migoo.variables.MiGooVariables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 标准引擎，用于执行最小的一个测试用例
 */
public class StandardEngine extends AbstractTestEngine {

    private MiGooVariables superVariables;

    public StandardEngine(TestPlan plan) {
        super(plan);
    }

    public StandardEngine(TestPlan plan, MiGooVariables variables) {
        super(plan);
        this.superVariables = variables;
    }

    private TestElement getSampler() {
        return plan.traverseInto().getSampler();
    }

    @Override
    public SampleResult run() {
        SampleResult result = null;
        List<SampleResult> subResults = new ArrayList<>();
        try {
            this.testStart(subResults);
            result = this.sampler();
            this.testEnd(result, subResults);
        } catch (Exception e) {
            result = result == null ? SampleResult.Failed(plan.getPropertyAsString(TITLE)) : result;
            result.setThrowable(e);
        } finally {
            super.testEnded();
        }
        result.setSubResults(subResults);
        superVariables.mergeVariable(plan.getVariables());
        return result;
    }

    private void testStart(List<SampleResult> subResults){
        super.convertVariable();
        super.preprocess(subResults);
    }

    private void testEnd(SampleResult result, List<SampleResult> subResults){
        this.assertResult(result);
        this.extractResult(result);
        super.postprocess(subResults);
    }

    private SampleResult sampler() {
        TestElement ele = this.getSampler();
        if (ele instanceof TestStateListener) {
            ((TestStateListener) ele).testStarted();
        }
        return ((Sampler) ele).sample();
    }

    private void assertResult(SampleResult result) {
        if (!result.isSuccessful()) {
            return;
        }
        Iterator<TestElement> iterators = getChildTestElements().iterator();
        result.setAssertionResults(new ArrayList<>());
        while (iterators.hasNext()) {
            TestElement element = iterators.next();
            if (element instanceof Assertion) {
                AssertionResult aResult = ((Assertion) element).getResult(result);
                // 如果有断言失败，则设置当前取样器的结果为失败
                if (!aResult.isSuccessful()) {
                    result.setSuccessful(false);
                }
                result.getAssertionResults().add(aResult);
                iterators.remove();
            }
        }
    }

    private void extractResult(SampleResult result) {
        if (!result.isSuccessful()) {
            return;
        }
        for (TestElement element : getChildTestElements()) {
            if (element instanceof Extractor) {
                ((Extractor) element).process(result);
            }
        }
    }
}