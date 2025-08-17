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

package io.github.xiaomisum.ryze.core;

import io.github.xiaomisum.ryze.core.config.RyzeVariables;
import io.github.xiaomisum.ryze.core.context.Context;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.context.SessionContext;
import io.github.xiaomisum.ryze.core.testelement.TestElement;
import io.github.xiaomisum.ryze.core.testelement.TestElementConfigureGroup;
import io.github.xiaomisum.ryze.core.testelement.TestElementConstantsInterface;
import io.github.xiaomisum.ryze.support.ValidateResult;

import java.util.*;

/**
 * 测试用例实际执行入口，非线程安全类，每个用例应使用各自的 SessionRunner 对象。
 */
public class SessionRunner {

    public static final ThreadLocal<SessionRunner> HOLDER = new ThreadLocal<>();
    private final SessionContext sessionContext = new SessionContext();
    /**
     * 用例执行过程中可用来进行数据存取
     */
    private final Map<String, Object> storage = new HashMap<>();
    private final Configure configure;
    /**
     * Session 当前执行上下文链
     */
    private List<Context> contextChain = new ArrayList<>();
    private ContextWrapper context;
    private boolean runInTestFrameworkSupport = false;

    SessionRunner(Configure configure) {
        this.configure = configure;
        initContextChain();
    }

    public static SessionRunner getSessionIfNoneCreateNew() {
        var sessionRunner = HOLDER.get();
        if (Objects.nonNull(sessionRunner)) {
            return sessionRunner;
        }
        sessionRunner = new SessionRunner(Configure.defaultConfigure());
        setSession(sessionRunner);
        return sessionRunner;
    }


    public static SessionRunner getSession() {
        SessionRunner sessionRunner = HOLDER.get();
        if (sessionRunner == null) {
            throw new IllegalStateException("SessionRunner 未设置，请先调用 setSession 方法设置");
        }
        return sessionRunner;
    }


    public static void setSession(SessionRunner sessionRunner) {
        HOLDER.set(sessionRunner);
    }

    public static void newSession(Configure configure) {
        HOLDER.set(new SessionRunner(configure));
    }

    public static void newTestFrameworkSession(Configure configure) {
        var session = new SessionRunner(configure);
        session.runInTestFrameworkSupport = true;
        HOLDER.set(session);
    }

    public static void removeSession() {
        HOLDER.remove();
    }

    private void initContextChain() {
        // 会话上下文（运行用例）
        contextChain.add(configure.getGlobalContext());
        contextChain.add(sessionContext);

        // 会话上下文默认值：添加一个空的变量配置
        // SessionRunner 可能直接运行某个 Sampler，而不是 TestCase，比如 Groovy/Java 用例
        // 示例：SessionRunner 连续运行多个 Http 请求，Http 请求中设置和读取 Session 变量
        TestElementConfigureGroup testElementConfig = new TestElementConfigureGroup();
        testElementConfig.put(TestElementConstantsInterface.VARIABLES, new RyzeVariables());
        sessionContext.setConfigGroup(testElementConfig);

        context = new ContextWrapper(this);
    }

    /**
     * Session 上下文配置，如变量配置、Http 配置等。
     */
    public void config(TestElementConfigureGroup config) {
        var oldConfig = (TestElementConfigureGroup) sessionContext.getConfigGroup();
        sessionContext.setConfigGroup(oldConfig.merge(config));
    }

    /**
     * 运行任意的测试元件，如 TestSuite 或 Sampler
     * <p>
     * 默认校验：执行前校验测试元件数据是否合法
     *
     * @see #runTest(TestElement, boolean)
     */
    public <T extends Result> T runTest(TestElement<T> element) {
        return runTest(element, true);
    }

    /**
     * 运行任意的测试元件，如 TestSuite 或 Sampler
     *
     * @param element  测试元件
     * @param <T>      测试元件对应的执行结果类
     * @param validate 是否校验测试元件数据
     * @return 测试元件的执行结果
     */
    public <T extends Result> T runTest(TestElement<T> element, boolean validate) {
        if (validate) {
            ValidateResult validateResult = element.validate();
            if (validateResult.isValid()) {
                throw new RuntimeException(validateResult.getReason());
            }
        }
        return element.run(this);
    }

    public List<Context> getContextChain() {
        return contextChain;
    }

    public void setContextChain(List<Context> contextChain) {
        this.contextChain = contextChain;
    }

    public ContextWrapper getContext() {
        return context;
    }

    public void setContext(ContextWrapper context) {
        this.context = context;
    }

    public Map<String, Object> getStorage() {
        return storage;
    }

    public boolean isRunInTestFrameworkSupport() {
        return runInTestFrameworkSupport;
    }

    public Configure getConfigure() {
        return configure;
    }
}
