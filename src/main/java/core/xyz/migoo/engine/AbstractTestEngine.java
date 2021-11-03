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

package core.xyz.migoo.engine;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import core.xyz.migoo.extractor.Extractor;
import core.xyz.migoo.processor.PostProcessor;
import core.xyz.migoo.processor.PreProcessor;
import core.xyz.migoo.processor.Processor;
import core.xyz.migoo.samplers.SampleResult;
import core.xyz.migoo.testelement.AbstractTestElement;
import core.xyz.migoo.testelement.TestElement;
import core.xyz.migoo.testelement.TestElementService;
import core.xyz.migoo.testelement.TestStateListener;
import core.xyz.migoo.variables.MiGooVariables;
import core.xyz.migoo.variables.VariableStateListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * @author xiaomi
 */
public abstract class AbstractTestEngine extends AbstractTestElement implements TestEngine {

    protected final TestPlan plan;

    public AbstractTestEngine(TestPlan plan) {
        this.plan = plan;
    }

    @Override
    public Vector<TestElement> getChildTestElements() {
        return plan.getChildTestElements();
    }

    @Override
    public MiGooVariables getVariables() {
        return plan.getVariables();
    }

    @Override
    public void convertVariable() {
        plan.getVariables().convertVariable();
    }

    @Override
    public void mergeVariable(MiGooVariables other) {
        plan.getVariables().mergeVariable(other);
    }

    public List<SampleResult> preprocess(List<SampleResult> subResults) {
        for (TestElement element : plan.getConfigElements()) {
            testStarted(element);
        }
        return process(subResults, plan.getPreprocessors());
    }

    private void testStarted(TestElement element) {
        if (element instanceof TestStateListener) {
            ((TestStateListener) element).testStarted();
        }
    }

    public List<SampleResult> postprocess(List<SampleResult> subResults) {
        // 5. 运行后置处理器
        return process(subResults, plan.getPostprocessors());
    }

    protected void testEnded() {
        for (TestElement element : plan.getConfigElements()) {
            testEnded(element);
        }
    }

    private void testEnded(TestElement element) {
        if (element instanceof TestStateListener) {
            ((TestStateListener) element).testEnded();
        }
    }

    private void initializeChildTestElements(TestElement element, JSONArray configs) {
        if (configs != null && configs.size() > 0) {
            for (int i = 0; i < configs.size(); i++) {
                JSONObject config = configs.getJSONObject(i);
                if (config != null && config.size() > 0) {
                    TestElement child = TestElementService.getService(config.getString(TEST_CLASS));
                    child.setProperties(config);
                    child.setVariables(element.getVariables());
                    element.addChildTestElement(child);
                }
            }
        }
    }

    private List<SampleResult> process(List<SampleResult> subResults, List<TestElement> elements) {
        for (TestElement element : elements) {
            ((VariableStateListener) element).convertVariable();
            this.initializeChildTestElements(element, element.getPropertyAsJSONArray(EXTRACTORS));
            testStarted(element);
            SampleResult processorResult = ((Processor) element).process();
            processorResult.setSubResults(new ArrayList<>(element.getChildTestElements().size()));
            for (TestElement extractor : element.getChildTestElements()) {
                if (extractor instanceof Extractor) {
                    processorResult.getSubResults().add( ((Extractor) extractor).process(processorResult));
                }
            }
            testEnded(element);
            subResults.add(processorResult);
        }
        return subResults;
    }
}