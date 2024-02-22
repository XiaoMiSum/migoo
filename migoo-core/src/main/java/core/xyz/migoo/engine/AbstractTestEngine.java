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

import core.xyz.migoo.report.Result;
import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.testelement.TestElement;
import core.xyz.migoo.testelement.TestElementService;
import core.xyz.migoo.variable.MiGooVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static core.xyz.migoo.testelement.AbstractTestElement.EXTRACTORS;
import static core.xyz.migoo.testelement.AbstractTestElement.TEST_CLASS;

/**
 * @author xiaomi
 */
public abstract class AbstractTestEngine implements TestEngine {
    protected final MiGooContext context;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Result result;

    public AbstractTestEngine(MiGooContext context, Result result) {
        this.context = context;
        this.result = result;
    }

    public AbstractTestEngine(MiGooContext context, Result result, MiGooVariables other) {
        // 合并父节点变量
        context.getVariables().mergeVariable(other);
        this.context = context;
        this.result = result;
    }

    @Override
    public Result runTest() {
        logger.info("开始执行测试, ID: {}, TITLE, {}", context.getId(), context.getTitle());
        result.setVariables(context.getVariables());
        result.sampleStart();
        // 全局变量计算 - 替换变量集本身使用的函数&变量
        context.getVariables().convertVariable();
        logger.info("当前集合变量: " + context.getVariables().getProperty());
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
        List<SampleResult> results = new ArrayList<>(context.getConfigurations().size());
        context.getConfigurations().forEach(element -> {
            SampleResult sr = new SampleResult(element.getPropertyAsString(TEST_CLASS));
            sr.sampleStart();
            TestElementService.testStarted(element);
            sr.setTestClass(element.getClass());
            sr.setSamplerData(element.toString());
            sr.sampleEnd();
            results.add(sr);
        });
        result.setConfigElementResults(results);
    }

    private void runPreprocessors() {
        result.setPreprocessorResults(runProcessors(context.getPreprocessors()));
    }

    private void runPostprocessors() {
        result.setPostprocessorResults(runProcessors(context.getPostprocessors()));
    }

    private void testEnded() {
        result.sampleEnd();
        for (TestElement element : context.getConfigurations()) {
            TestElementService.testEnded(element);
        }
    }

    private List<SampleResult> runProcessors(Vector<TestElement> elements) {
        List<SampleResult> results = new ArrayList<>();
        elements.forEach(element -> {
            TestElementService.testStarted(element);
            SampleResult result = (SampleResult) TestElementService.runTest(element);
            result.setSubResults(new ArrayList<>());
            if (element.getProperty().containsKey(EXTRACTORS)) {
                List<SampleResult> extractorResults = new ArrayList<>();
                for (int i = 0; i < element.getPropertyAsJSONArray(EXTRACTORS).size(); i++) {
                    Testplan plan = (Testplan) element.getPropertyAsJSONArray(EXTRACTORS).get(i);
                    TestElement el = TestElementService.getService(plan.getString(TEST_CLASS));
                    TestElementService.prepare(el, plan, element.getVariables());
                    extractorResults.add((SampleResult) TestElementService.runTest(el, result));
                    context.getVariables().convertVariables(el.getVariables().getProperty());
                }
                result.setExtractorResults(extractorResults);
            }
            TestElementService.testEnded(element);
            results.add(result);
        });
        return results;
    }
}