package core.xyz.migoo;


import core.xyz.migoo.context.Context;
import core.xyz.migoo.context.SessionContext;
import core.xyz.migoo.report.Result;
import core.xyz.migoo.testelement.TestElementConfigure;
import core.xyz.migoo.testelement.TestElementConstantsInterface;
import core.xyz.migoo.testelement1.TestElement;
import core.xyz.migoo.variable.MiGooVariables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    /**
     * Session 当前执行上下文链
     */
    private List<Context> contextChain = new ArrayList<>();
    private ContextWrapper contextWrapper;

    SessionRunner() {
        initContextChain();
    }

    public static SessionRunner getSession() {
        SessionRunner sessionRunner = HOLDER.get();
        if (sessionRunner == null) {
            throw new IllegalStateException("SessionRunner 未设置，请先调用 setSession 方法，或使用 TestNG 组件的 @GrootSupport 等特性");
        }
        return sessionRunner;
    }

    public static void setSession(SessionRunner sessionRunner) {
        HOLDER.set(sessionRunner);
    }

    public static void removeSession() {
        HOLDER.remove();
    }

    private void initContextChain() {
        // 会话上下文（运行用例）
        contextChain.add(sessionContext);

        // 会话上下文默认值：添加一个空的变量配置
        // SessionRunner 可能直接运行某个 Sampler，而不是 TestCase，比如 Groovy/Java 用例
        // 示例：SessionRunner 连续运行多个 Http 请求，Http 请求中设置和读取 Session 变量
        TestElementConfigure testElementConfig = new TestElementConfigure();
        testElementConfig.put(TestElementConstantsInterface.VALIDATORS, new MiGooVariables());
        sessionContext.setConfigGroup(testElementConfig);

        contextWrapper = new ContextWrapper(this);
    }

    /**
     * Session 上下文配置，如变量配置、Http 配置等。
     */
    public void config(TestElementConfigure config) {
        var oldConfig = (TestElementConfigure) sessionContext.getConfigGroup();
        sessionContext.setConfigGroup(oldConfig.merge(config));
    }


    /**
     * 运行任意的测试元件，如 TestCase 或 Sampler
     * <p>
     * 默认校验：执行前校验测试元件数据是否合法
     *
     * @see #run(TestElement, boolean)
     */
    public <T extends Result> T run(TestElement<T> testElement) {
        return run(testElement, true);
    }

    /**
     * 运行任意的测试元件，如 TestCase 或 Sampler
     *
     * @param testElement 测试元件
     * @param <T>         测试元件对应的执行结果类
     * @param validate    是否校验测试元件数据
     * @return 测试元件的执行结果
     */
    public <T extends Result> T run(TestElement<T> testElement, boolean validate) {
        if (validate) {
            ValidateResult validateResult = testElement.validate();
            if (!validateResult.isValid()) {
                throw new RuntimeException(validateResult.getReason());
            }
        }
        return testElement.run(this);
    }


    /**
     * 获取 TestCaseRunner 当前的上下文链，依次为：全局上下文、环境上下文、用例上下文。
     *
     * @return TestCaseRunner 当前的上下文链
     */
    public List<Context> getContextChain() {
        return contextChain;
    }

    public void setContextChain(List<Context> contextChain) {
        this.contextChain = contextChain;
    }

    public ContextWrapper getContextWrapper() {
        return contextWrapper;
    }

    public void setContextWrapper(ContextWrapper contextWrapper) {
        this.contextWrapper = contextWrapper;
    }

    public Map<String, Object> getStorage() {
        return storage;
    }

}
