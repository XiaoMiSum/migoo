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

package core.xyz.migoo.context;


import core.xyz.migoo.ApplicationConfig;
import core.xyz.migoo.SessionRunner;
import core.xyz.migoo.config.ConfigureGroup;
import core.xyz.migoo.context.variables.*;
import core.xyz.migoo.report.Result;
import core.xyz.migoo.template.TemplateEngine;
import core.xyz.migoo.testelement.TestElement;
import core.xyz.migoo.testelement.TestElementConfigure;
import support.xyz.migoo.Computable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 测试上下文包装器，提供了处理上下文链的各种方法
 *
 * @author xiaomi
 */
public class ContextWrapper {

    // 原始的上下文作用域链
    private final List<Context> rawContextChain;
    // 合并后的配置组
    private final ConfigureGroup configureGroup;
    private final AllVariablesWrapper allVariablesWrapper;
    private final LocalVariablesWrapper localVariablesWrapper;
    private final TemplateEngine templateEngine;
    // 当前上下文，即最后一个上下文对象
    private Context sessionContext;
    private GlobalContext globalContext;
    private TestSuiteContext testContext;
    private GlobalVariablesWrapper globalVariablesWrapper;
    private TestVariablesWrapper testVariablesWrapper;
    private SessionVariablesWrapper sessionVariablesWrapper;
    // Runner 相关对象
    private SessionRunner sessionRunner;
    // 测试元件信息
    private TestElement<?> testElement;
    private Result testResult;

    // ----------- 构造器 -------------

    /**
     * 用于用例执行前，比如全局变量和环境变量的表达式计算
     *
     * @param contextChain 上下文链
     */
    public ContextWrapper(List<Context> contextChain) {
        requiredNotNullAndNotEmpty(contextChain);
        // 处理上下文链
        this.rawContextChain = contextChain;
        // 合并配置组
        this.configureGroup = mergeConfigGroup();

        // 读取各级上下文
        searchNonTestStepContext();
        // 当前上下文，即最后一个上下文对象
        Context currentContext = contextChain.getLast();

        // 处理各级上下文
        if (globalContext != null) {
            this.globalVariablesWrapper = new GlobalVariablesWrapper(List.of(globalContext));
        }
        if (testContext != null) {
            this.testVariablesWrapper = new TestVariablesWrapper(List.of(testContext));
        }
        if (sessionContext != null) {
            this.sessionVariablesWrapper = new SessionVariablesWrapper(List.of(sessionContext));
        }
        this.allVariablesWrapper = new AllVariablesWrapper(Collections.unmodifiableList(rawContextChain));
        this.localVariablesWrapper = new LocalVariablesWrapper(List.of(currentContext));
        this.templateEngine = new TemplateEngine(this, ApplicationConfig.getFunctionKeyMap());
    }

    /**
     * 用于用例执行时，包装当前元件的执行信息
     *
     * @param sessionRunner 用例执行器
     */
    public ContextWrapper(SessionRunner sessionRunner) {
        this(sessionRunner.getContextChain());
        this.sessionRunner = sessionRunner;
    }

    // == 构造器辅助方法 ==

    private void requiredNotNullAndNotEmpty(List<Context> contextChain) {
        if (contextChain == null || contextChain.isEmpty()) {
            throw new UnsupportedOperationException("上下文链为空");
        }

        long nullCount = contextChain.stream().filter(Objects::isNull).count();
        if (nullCount > 0) {
            throw new UnsupportedOperationException("上下文链中存在空值");
        }
    }

    private void searchNonTestStepContext() {
        // 如果上下文链中存在多个 SessionContext，则取最后一个，即最近的一个
        rawContextChain.forEach(ctx -> {
            if (ctx instanceof GlobalContext) {
                this.globalContext = (GlobalContext) ctx;
            } else if (ctx instanceof TestSuiteContext) {
                this.testContext = (TestSuiteContext) ctx;
            } else if (ctx instanceof SessionContext) {
                this.sessionContext = ctx;
            }
        });
    }

    private ConfigureGroup mergeConfigGroup() {
        ConfigureGroup config = new TestElementConfigure();
        for (var context : rawContextChain) {
            var configureGroup = context.getConfigGroup();
            config = config.merge(configureGroup);
        }
        return config;
    }

    // ----------- ContextWrapper 对外 API -------------

    /**
     * 变量 & 函数 计算，如果 obj 不是变量&函数，则返回obj
     * <p>
     * 比如 ${var} 表示变量，__func() __func(1,2) __func(a=1.b=2) __func(${var}) __func(a=${var}) 表示函数
     * <p>
     * 支持多个变量或函数混合使用
     * <p>
     * 比如 ${var1}migoo__func()${var2}test
     * <p>
     * 不支持函数嵌套
     * 比如：__func(__func())、__func(a=__func())
     *
     * @param obj 待计算的对象
     * @return 计算后的对象 或 obj原值
     */
    public Object eval(Object obj) {
        if (obj instanceof Computable<?> computable) {
            return computable.calc(this);
        }
        return templateEngine.evaluate(obj);
    }

    public List<Context> getContextChain() {
        return rawContextChain;
    }

    public TestVariablesWrapper getTestVariablesWrapper() {
        return testVariablesWrapper;
    }

    public SessionVariablesWrapper getSessionVariablesWrapper() {
        return sessionVariablesWrapper;
    }

    public AllVariablesWrapper getAllVariablesWrapper() {
        return allVariablesWrapper;
    }

    public LocalVariablesWrapper getLocalVariablesWrapper() {
        return localVariablesWrapper;
    }

    public SessionRunner getSessionRunner() {
        return sessionRunner;
    }

    public ConfigureGroup getConfigGroup() {
        return configureGroup;
    }

    public TestElement<?> getTestElement() {
        return testElement;
    }

    public void setTestElement(TestElement<?> testElement) {
        this.testElement = testElement;
    }

    public Result getTestResult() {
        return testResult;
    }

    public void setTestResult(Result testResult) {
        this.testResult = testResult;
    }

    public GlobalContext getGlobalContext() {
        return globalContext;
    }

    public void setGlobalContext(GlobalContext globalContext) {
        this.globalContext = globalContext;
    }

    public GlobalVariablesWrapper getGlobalVariablesWrapper() {
        return globalVariablesWrapper;
    }

    public void setGlobalVariablesWrapper(GlobalVariablesWrapper globalVariablesWrapper) {
        this.globalVariablesWrapper = globalVariablesWrapper;
    }
}
