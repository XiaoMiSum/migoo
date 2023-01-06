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

import core.xyz.migoo.processor.Processor;
import core.xyz.migoo.report.Result;
import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.testelement.MiGooProperty;
import core.xyz.migoo.testelement.TestElement;
import core.xyz.migoo.testelement.TestElementService;
import core.xyz.migoo.variable.MiGooVariables;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import static core.xyz.migoo.testelement.AbstractTestElement.*;

/**
 * @author xiaomi
 */
public abstract class AbstractTestEngine implements TestEngine {

    protected final Testplan plan;

    protected final Vector<TestElement> configElements = new Vector<>(10);

    protected final Vector<TestElement> preprocessors = new Vector<>(10);

    protected final Vector<TestElement> postprocessors = new Vector<>(10);


    public AbstractTestEngine(Testplan plan) {
        this.plan = plan;
        this.plan.getVariables().convertVariable();
        addChildrenElements(CONFIG_ELEMENTS, configElements);
        addChildrenElements(PREPROCESSORS, preprocessors);
        addChildrenElements(POSTPROCESSORS, postprocessors);
    }

    protected void addChildrenElements(String key, Vector<TestElement> elements) {
        if (Objects.nonNull(plan.get(key))) {
            for (Object element : plan.getJSONArray(key)) {
                TestElement el = TestElementService.getService(((Testplan) element).getString(TEST_CLASS));
                TestElementService.prepare(el, ((Testplan) element), plan.getVariables());
                elements.add(el);
            }
        }
    }

    @Override
    public void mergeVariable(MiGooVariables other) {
        plan.getVariables().mergeVariable(other);
    }

    protected void preprocess(Result result) {
        // 前置处理器执行
        List<SampleResult> results = process(preprocessors);
        result.setPreprocessorResults(results);
    }

    protected void postprocess(Result result) {
        // 后置处理器执行
        List<SampleResult> results = process(postprocessors);
        result.setPostprocessorResults(results);
    }

    protected void prepare(Result result) {
        List<SampleResult> results = new ArrayList<>(configElements.size());
        for (TestElement element : configElements) {
            TestElementService.testStarted(element);
            SampleResult sr = new SampleResult(element.getPropertyAsString(TEST_CLASS));
            sr.sampleStart();
            sr.setTestClass(element.getClass());
            MiGooProperty property = new MiGooProperty(element.getProperty());
            property.remove(VARIABLES);
            property.remove(TEST_CLASS);
            sr.setSamplerData(property.toString());
            sr.sampleEnd();
            results.add(sr);
        }
        if (!results.isEmpty()) {
            result.setConfigElementResults(results);
        }
    }

    protected void close() {
        for (TestElement element : configElements) {
            TestElementService.testEnded(element);
        }
    }

    protected List<SampleResult> process(Vector<TestElement> elements) {
        List<SampleResult> results = new ArrayList<>();
        for (TestElement element : elements) {
            if (element instanceof Processor) {
                TestElementService.testStarted(element);
                SampleResult result = (SampleResult) TestElementService.runTest(element);
                result.setSubResults(new ArrayList<>());
                if (element.getProperty().containsKey(EXTRACTORS)) {
                    List<SampleResult> ex = new ArrayList<>();
                    for (int i = 0; i < element.getPropertyAsJSONArray(EXTRACTORS).size(); i++) {
                        Testplan plan = (Testplan) element.getPropertyAsJSONArray(EXTRACTORS).get(i);
                        TestElement el = TestElementService.getService(plan.getString(TEST_CLASS));
                        TestElementService.prepare(el, plan, element.getVariables());
                        ex.add((SampleResult) TestElementService.runTest(el, result));
                    }
                    result.setExtractorResults(ex);
                }
                TestElementService.testEnded(element);
                results.add(result);
            }
        }
        return results;
    }
}