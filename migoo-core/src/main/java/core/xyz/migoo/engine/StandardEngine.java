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

import core.xyz.migoo.assertion.Assertion;
import core.xyz.migoo.assertion.VerifyResult;
import core.xyz.migoo.extractor.Extractor;
import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.testelement.TestElement;
import core.xyz.migoo.testelement.TestElementService;
import protocol.xyz.migoo.http.sampler.HTTPSampleResult;
import protocol.xyz.migoo.http.sampler.HttpSampler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import static core.xyz.migoo.testelement.AbstractTestElement.*;

/**
 * 标准引擎，用于执行最小的一个测试用例
 *
 * @author xiaomi
 */
public class StandardEngine extends AbstractTestEngine {

    private final Vector<TestElement> validators = new Vector<>(10);
    private final Vector<TestElement> extractors = new Vector<>(10);

    public StandardEngine(Testplan plan) {
        super(plan);
        addChildrenElements(VALIDATORS, validators);
        addChildrenElements(EXTRACTORS, extractors);
    }

    @Override
    public SampleResult runTest() {
        TestElement sampler = TestElementService.getService(plan.getString(TEST_CLASS));
        TestElementService.prepare(sampler, plan.getJSONObject(CONFIG), plan.getVariables());
        SampleResult result = sampler instanceof HttpSampler ? new HTTPSampleResult(plan.getString(TITLE)) :
                new SampleResult(plan.getString(TITLE));
        try {
            prepare(result);
            preprocess(result);
            execute(sampler, result);
            verify(result);
            extract(result);
            postprocess(result);
        } catch (Exception e) {
            result.setThrowable(e);
        } finally {
            close();
        }
        return result;
    }

    private void verify(SampleResult result) {
        if (!result.isSuccessful()) {
            return;
        }
        Iterator<TestElement> iterators = validators.iterator();
        result.setAssertionResults(new ArrayList<>(validators.size()));
        while (iterators.hasNext()) {
            TestElement element = iterators.next();
            if (element instanceof Assertion) {
                VerifyResult aResult = ((Assertion) element).getResult(result);
                result.setSuccessful(!result.isSuccessful() ? result.isSuccessful() : aResult.isSuccessful());
                result.getAssertionResults().add(aResult);
                iterators.remove();
            }
        }
    }

    private void execute(TestElement sampler, SampleResult result) {
        TestElementService.testStarted(sampler);
        result.setSamplerData((SampleResult) TestElementService.runTest(sampler));
        TestElementService.testEnded(sampler);
    }

    private void extract(SampleResult result) {
        if (!result.isSuccessful() || extractors.isEmpty()) {
            return;
        }
        List<SampleResult> ex = new ArrayList<>(extractors.size());
        Iterator<TestElement> iterators = extractors.iterator();
        while (iterators.hasNext()) {
            TestElement element = iterators.next();
            if (element instanceof Extractor) {
                ex.add((SampleResult) TestElementService.runTest(element, result));
                iterators.remove();
            }
        }
        result.setExtractorResults(ex);
    }
}