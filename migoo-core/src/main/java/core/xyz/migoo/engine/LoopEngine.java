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

import java.util.ArrayList;

import static core.xyz.migoo.testelement.AbstractTestElement.CHILD;
import static core.xyz.migoo.testelement.AbstractTestElement.TITLE;

/**
 * @author xiaomi
 */
public class LoopEngine extends AbstractTestEngine {

    public LoopEngine(Testplan plan) {
        super(plan);
    }

    @Override
    public Result runTest() {
        Result result = new Result(plan.getString(TITLE));
        result.setVariables(plan.getVariables());
        result.sampleStart();
        try {
            prepare(result);
            preprocess(result);
            execute(result);
            postprocess(result);
        } catch (Exception e) {
            result.setThrowable(e);
        } finally {
            result.sampleEnd();
            close();
        }
        return result;
    }

    protected void execute(Result result) {
        result.setSubResults(new ArrayList<>());
        for (Object element : plan.getJSONArray(CHILD)) {
            Testplan plan = (Testplan) element;
            Result sResult = TestEngineService.runTest(plan, this.plan.getVariables());
            result.getSubResults().add(sResult);
            result.setSuccessful(!result.isSuccessful() ? result.isSuccessful() : sResult.isSuccessful());
            if (plan.isStandardSampler()) {
                // 标准引擎 需要将变量传递到子测试元素里面，并且如果测试结果失败的，后续也没必要再执行了
                this.plan.getVariables().mergeVariable(plan.getVariables());
                if (!result.isSuccessful()) {
                    break;
                }
            }
        }
    }
}