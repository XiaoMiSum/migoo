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
import core.xyz.migoo.variable.MiGooVariables;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author xiaomi
 */
public class LoopEngine extends AbstractTestEngine {

    public LoopEngine(MiGooContext context) {
        super(context, new Result(context.getId(), context.getTitle()));
    }

    public LoopEngine(MiGooContext context, MiGooVariables other) {
        super(context, new Result(context.getId(), context.getTitle()), other);
    }

    @Override
    protected void runTest(Result result) {
        if (!result.isSuccessful()) {
            return; // 父级结果为 false，则子级结果全部跳过
        }
        result.setSubResults(new ArrayList<>());
        for (var child : context.getChildren()) {
            var engine = Objects.isNull(child.getSampler()) ? new LoopEngine(child, context.getVariables())
                    : new StandardEngine(child, context.getVariables());
            var sResult = engine.runTest();
            result.getSubResults().add(sResult);
            // 如果 父级结果为 true，则使用子级的结果替换父级结果，否则保持父级结果
            result.setSuccessful(result.isSuccessful() ? sResult.isSuccessful() : result.isSuccessful());
            if (engine instanceof StandardEngine && result.isSuccessful()) {
                // 标准引擎 将子节点里面的变量合并到当前父节点中，以便传递给其他子节点
                context.getVariables().mergeVariable(child.getVariables());
            }
            if (!sResult.isSuccessful() && Objects.nonNull(child.getSampler())) {
                break; // 如果子级结果为 false，且子级为取样器时则无需继续执行剩余的子级取样器
            }
        }
    }
}