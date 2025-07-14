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

import core.xyz.migoo.processor.PostProcessor;
import core.xyz.migoo.processor.PreProcessor;
import core.xyz.migoo.report.Result;
import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.testelement.TestElement;
import core.xyz.migoo.testelement.TestElementService;
import core.xyz.migoo.variable.MiGooVariables;

import java.util.ArrayList;
import java.util.List;

import static core.xyz.migoo.testelement.AbstractTestElement.*;

/**
 * @author xiaomi
 */
public abstract class AbstractTestEngine implements TestEngine {

    protected final MiGooContext context;
    private final Result result;

    public AbstractTestEngine(MiGooContext context, Result result) {
        this.context = context;
        this.result = result;
    }

    public AbstractTestEngine(MiGooContext context, Result result, MiGooVariables parentVars) {
        // 合并父节点变量
        context.getVariables().mergeVariable(parentVars);
        this.context = context;
        this.result = result;
    }

    @Override
    public Result runTest() {
        result.setVariables(context.getVariables());
        result.sampleStart();
        // 全局变量计算 - 替换变量集本身使用的函数&变量
        context.getVariables().convertVariable();
        try {
            // 准备配置元件
            prepareConfigurations();
            // 执行前置处理器
            runPreprocessors();
            // 执行子节点
            runTest(result);
            // 执行后置处理器
            runPostprocessors();
        } catch (Exception e) {
            result.setThrowable(e);
        } finally {
            testEnded();
        }
        return result;
    }

    protected abstract void runTest(Result result);

    private void prepareConfigurations() {
        var results = new ArrayList<SampleResult>(context.getConfigurations().size());
        for (TestElement element : context.getConfigurations()) {
            var result = new SampleResult(element.getPropertyAsString(TEST_CLASS));
            result.sampleStart();
            result.setTestClass(element.getClass());
            try {
                TestElementService.testStarted(element);
            } catch (Exception e) {
                result.setThrowable(e);
                this.result.setSuccessful(false);
                break;
            } finally {
                element.getProperty().remove(VARIABLES);
                result.setSamplerData(element.getProperty().toString());
                result.sampleEnd();
                results.add(result);
            }
        }
        result.setConfigElementResults(results);
    }

    private void runPreprocessors() {
        if (!result.isSuccessful()) {
            return;
        }
        runProcessors(context.getPreprocessors().stream().filter(item -> item instanceof PreProcessor).toList(), true);
    }

    private void runPostprocessors() {
        if (!result.isSuccessful()) {
            return;
        }
        runProcessors(context.getPostprocessors().stream().filter(item -> item instanceof PostProcessor).toList(), false);
    }

    private void testEnded() {
        result.sampleEnd();
        for (TestElement element : context.getConfigurations()) {
            TestElementService.testEnded(element);
        }
    }

    private void runProcessors(List<TestElement> elements, boolean isPre) {
        var results = new ArrayList<SampleResult>();
        for (TestElement element : elements) {
            var result = new SampleResult(element.getPropertyAsString(TITLE));
            try {
                TestElementService.testStarted(element);
                result = TestElementService.runTest(element);
                result.setSubResults(new ArrayList<>());
                if (!result.isSuccessful() && isPre) {
                    this.result.setSuccessful(false);
                    break;
                }
                if (element.getProperty().containsKey(EXTRACTORS) && result.isSuccessful()) {
                    List<SampleResult> extractorResults = new ArrayList<>();
                    for (int i = 0; i < element.getPropertyAsJSONArray(EXTRACTORS).size(); i++) {
                        var plan = (Testplan) element.getPropertyAsJSONArray(EXTRACTORS).get(i);
                        var el = TestElementService.getService(plan.getString(TEST_CLASS));
                        TestElementService.prepare(el, plan, element.getVariables());
                        extractorResults.add(TestElementService.runTest(el, result));
                    }
                    result.setExtractorResults(extractorResults);
                }
                TestElementService.testEnded(element);
            } catch (Exception e) {
                this.result.setSuccessful(false);
                element.getProperty().remove(VARIABLES);
                if (element.getProperty().containsKey(EXTRACTORS)) {
                    element.getPropertyAsJSONArray(EXTRACTORS).forEach(item -> ((Testplan) item).remove(VARIABLES));
                }
                result.setSamplerData(element.getProperty().toString());
                result.setTestClass(element.getClass());
                result.setThrowable(e);
                result.sampleEnd();
                break;
            } finally {
                results.add(result);
            }
        }
        if (isPre) {
            result.setPreprocessorResults(results);
        } else {
            result.setPostprocessorResults(results);
        }
    }
}