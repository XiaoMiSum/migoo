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

package io.github.xiaomisum.ryze.core.context.variables;

import io.github.xiaomisum.ryze.core.config.RyzeVariables;
import io.github.xiaomisum.ryze.core.context.Context;
import io.github.xiaomisum.ryze.core.context.TestRunContext;
import io.github.xiaomisum.ryze.core.testelement.TestElementConfigureGroup;
import io.github.xiaomisum.ryze.core.testelement.TestElementConstantsInterface;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 变量包装类基础实现
 *
 * @author xiaomi
 */
public abstract class AbstractVariablesWrapper implements VariablesWrapper, TestElementConstantsInterface {

    // 上下文链
    protected List<Context> contextChain;

    // 最后一个上下文
    protected Context lastContext;

    public AbstractVariablesWrapper(List<Context> contextChain) {
        this.contextChain = contextChain;
        this.lastContext = contextChain.getLast();
    }

    public Object get(String name) {
        if (hasLastVariables()) {
            return getLastVariables().get(name);
        }
        return null;
    }

    public Object put(String name, Object value) {
        return getLastVariables().put(name, value);
    }

    public Object remove(String name) {
        if (hasLastVariables()) {
            return getLastVariables().remove(name);
        }
        return null;
    }

    @Override
    public Map<String, Object> mergeVariables() {
        var variables = new ConcurrentHashMap<String, Object>();
        // 合并上下文链中的变量，后加入上下文的变量会覆盖前面上下文的变量
        for (var context : contextChain) {
            var configureGroup = context.getConfigGroup();
            if (configureGroup != null) {
                var ctxVars = configureGroup.getVariables();
                if (ctxVars != null) {
                    variables.putAll(ctxVars);
                }
            }
        }
        return variables;
    }

    private boolean hasLastVariables() {
        if (lastContext.getConfigGroup() == null) {
            return false;
        }
        return lastContext.getConfigGroup().getVariables() != null;
    }

    public RyzeVariables getLastVariables() {
        var lastConfigureGroup = lastContext.getConfigGroup();
        if (Objects.isNull(lastConfigureGroup)) {
            lastConfigureGroup = new TestElementConfigureGroup();
            ((TestRunContext) lastContext).setConfigGroup(lastConfigureGroup);
        }
        var lastContextVariables = lastConfigureGroup.getVariables();
        if (!Objects.isNull(lastContextVariables)) {
            return lastContextVariables;
        }
        lastContextVariables = new RyzeVariables();
        lastConfigureGroup.put(VARIABLES, lastContextVariables);
        return lastContextVariables;
    }

    @Override
    public String toString() {
        return mergeVariables().toString();
    }

}
