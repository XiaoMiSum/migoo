package core.xyz.migoo.testelement1;

import core.xyz.migoo.Copyable;
import core.xyz.migoo.SessionRunner;
import core.xyz.migoo.Validatable;
import core.xyz.migoo.report.Result;

/**
 * 测试元件是能根据其父上下文链独立执行的一个逻辑执行单元。
 * 测试元件使用 {@link core.xyz.migoo.testelement.Alias} 注解配置关键字，
 * 若未使用该注解则通过 this.getClass().getSimpleName()获取类名用作关键字，以便识别测试集中组件的类型（代码风格中组件类型是已知的）。
 * 配置方式: test_class: http_sampler
 *
 * <p>测试元件是整个工具的核心，
 * 环境管理、变量管理、前置处理器、后置处理器、插值表达式、函数、断言、配置继承、取样器、控制器、用例引用等功能都将围绕测试元件展开。
 *
 * <p>一个测试元件可以包含多个子测试元件。
 *
 * <p>设计开发一个新的测试元件前，应当先明确该测试元件要实现的功能、需要的参数，是否和其他测试元件功能重合，实现粒度，然后再编码实现。
 *
 * <p>important! TestElement 为非线程安全类，不要在多个线程间共享 TestElement 对象。
 * 如果要在多个线程中运行同一个 TestElement 对象，请先 {@link #copy()}，每个线程使用该 TestElement 的拷贝进行运行。
 *
 * @author xiaomi
 */
@FunctionalInterface
public interface TestElement<T extends Result> extends Validatable, Copyable<TestElement<T>> {

    /**
     * 执行测试组件
     * <p>
     * 用户应避免直接调用该方法，推荐使用 {@link SessionRunner#run} 方法。
     *
     * @param session 每个测试用例使用各自的 SessionRunner
     * @return 执行结果
     */
    T run(SessionRunner session);

    /**
     * 对象拷贝，用于解决 TestElement对象的非线程安全问题
     *
     * <p>
     * 所有该接口的实现类均应当重写该方法
     * <p>
     *
     * @return 对象的拷贝
     */
    @Override
    default TestElement<T> copy() {
        // 默认认为当前类是线程安全的，否则应当重写该方法
        return this;
    }

}
