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
import core.xyz.migoo.extractor.Extractor;
import core.xyz.migoo.report.Result;
import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.testelement.TestElementService;
import core.xyz.migoo.variable.MiGooVariables;
import protocol.xyz.migoo.http.sampler.HTTPSampleResult;
import protocol.xyz.migoo.http.sampler.HttpSampler;

import java.util.ArrayList;
import java.util.List;

/**
 * 标准引擎，用于执行最小的一个测试用例
 *
 * @author xiaomi
 */
public class StandardEngine extends AbstractTestEngine {

    public StandardEngine(MiGooContext context) {
        super(context, new SampleResult(context.getTitle()));
    }

    public StandardEngine(MiGooContext context, MiGooVariables other) {
        super(context, context.getSampler() instanceof HttpSampler ?
                new HTTPSampleResult(context.getTitle()) : new SampleResult(context.getTitle()), other);
    }

    @Override
    protected void runTest(Result result) {
        TestElementService.testStarted(context.getSampler());
        ((SampleResult) result).setSamplerData((SampleResult) TestElementService.runTest(context.getSampler()));
        TestElementService.testEnded(context.getSampler());
        runAssertions((SampleResult) result);
        runExtractors((SampleResult) result);
    }


    private void runAssertions(SampleResult result) {
        if (!result.isSuccessful() || context.getValidators().isEmpty()) {
            return;
        }
        result.setAssertionResults(new ArrayList<>(context.getValidators().size()));
        context.getValidators().forEach(element -> {
            if (element instanceof Assertion) {
                element.getVariables().mergeVariable(context.getVariables());
                TestElementService.runTest(element, result);
            }
        });
    }

    private void runExtractors(SampleResult result) {
        if (!result.isSuccessful() || context.getExtractors().isEmpty()) {
            return;
        }
        List<SampleResult> ex = new ArrayList<>(context.getExtractors().size());
        context.getExtractors().forEach(element -> {
            if (element instanceof Extractor) {
                element.getVariables().mergeVariable(context.getVariables());
                ex.add((SampleResult) TestElementService.runTest(element, result));
                context.getVariables().mergeVariable(element.getVariables());
            }
        });
        result.setExtractorResults(ex);
    }
}