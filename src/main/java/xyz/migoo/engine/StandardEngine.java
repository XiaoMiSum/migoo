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
import protocol.xyz.migoo.http.sampler.HTTPSampleResult;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 标准引擎，用于执行最小的一个测试用例
 * @author xiaomi
 */
public class StandardEngine extends AbstractTestEngine {

    public StandardEngine(TestPlan plan) {
        super(plan);
    }

    private TestElement getSampler() {
        return plan.traverseInto().getSampler();
    }

    @Override
    public SampleResult run() {
        SampleResult result = new SampleResult(plan.getPropertyAsString(TITLE));
        try {
            result.setPreprocessorResults(this.testStart());
            SampleResult s = this.sampler();
            if (s instanceof HTTPSampleResult) {
                result = s;
            } else {
                result.setSamplerData(s);
            }
            result.setPostprocessorResults(this.testEnd(result));
        } catch (Exception e) {
            result.setThrowable(e);
        } finally {
            super.testEnded();
        }
        return result;
    }

    private List<SampleResult> testStart(){
        super.convertVariable();
        return super.preprocess(new ArrayList<>());
    }

    private List<SampleResult> testEnd(SampleResult result){
        this.assertResult(result);
        this.extractResult(result);
        return super.postprocess(new ArrayList<>());
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
                result.setSuccessful(!result.isSuccessful() ? result.isSuccessful() : aResult.isSuccessful());
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