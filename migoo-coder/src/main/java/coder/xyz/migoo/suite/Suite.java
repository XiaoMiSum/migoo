package coder.xyz.migoo.suite;

import coder.xyz.migoo.Configurers;
import coder.xyz.migoo.Processors;
import coder.xyz.migoo.Samplers;

import java.util.HashMap;
import java.util.Map;

public class Suite {

    /**
     * 最简单的测试集合
     *
     * @param title    描述
     * @param children 子集合 或 测试用例
     * @return 测试集合
     */
    public static ISuite newSuite(String title, ISuite[] children) {
        return new Testsuite(title, new HashMap<>(), children, new Configurers[]{}, new Processors[]{}, new Processors[]{});
    }

    /**
     * 有公共变量的测试集合
     *
     * @param title     描述
     * @param variables 变量
     * @param children  子集合 或 测试用例
     * @return 测试集合
     */
    public static ISuite newSuite(String title, Map<String, Object> variables, ISuite[] children) {
        return new Testsuite(title, variables, children, new Configurers[]{}, new Processors[]{}, new Processors[]{});
    }

    /**
     * 有公共配置的测试集合
     *
     * @param title       描述
     * @param variables   变量
     * @param children    子集合 或 测试用例
     * @param configurers 配置
     * @return 测试集合
     */
    public static ISuite newSuite(String title, Map<String, Object> variables, ISuite[] children, Configurers[] configurers) {
        return new Testsuite(title, variables, children, configurers, new Processors[]{}, new Processors[]{});
    }

    /**
     * 完整的测试集合
     *
     * @param title          描述
     * @param variables      变量
     * @param children       子集合 或 测试用例
     * @param configurers    配置
     * @param preprocessors  前置处理器
     * @param postprocessors 后置处理器
     * @return 测试集合
     */
    public static ISuite newSuite(String title, Map<String, Object> variables, ISuite[] children,
                                  Configurers[] configurers, Processors[] preprocessors, Processors[] postprocessors) {
        return new Testsuite(title, variables, children, configurers, preprocessors, postprocessors);
    }

    /**
     * 最简单的测试用例
     *
     * @param title    描述
     * @param children 测试步骤
     * @return 测试用例
     */
    public static ISuite newTestcase(String title, Samplers[] children) {
        return new Testcase(title, new HashMap<>(), children, new Configurers[]{}, new Processors[]{}, new Processors[]{});
    }

    /**
     * 有公共变量的测试用例
     *
     * @param title     描述
     * @param variables 变量
     * @param children  测试步骤
     * @return 测试用例
     */
    public static ISuite newTestcase(String title, Map<String, Object> variables, Samplers[] children) {
        return new Testcase(title, variables, children, new Configurers[]{}, new Processors[]{}, new Processors[]{});
    }

    /**
     * 有公共配置的测试用例
     *
     * @param title       描述
     * @param variables   变量
     * @param children    测试步骤
     * @param configurers 配置
     * @return 测试用例
     */
    public static ISuite newTestcase(String title, Map<String, Object> variables, Samplers[] children,
                                     Configurers[] configurers) {
        return new Testcase(title, variables, children, configurers, new Processors[]{}, new Processors[]{});
    }

    /**
     * 完整的测试用例
     *
     * @param title          描述
     * @param variables      变量
     * @param children       测试步骤
     * @param configurers    配置
     * @param preprocessors  前置处理器
     * @param postprocessors 后置处理器
     * @return 测试用例
     */
    public static ISuite newTestcase(String title, Map<String, Object> variables, Samplers[] children,
                                     Configurers[] configurers, Processors[] preprocessors, Processors[] postprocessors) {
        return new Testcase(title, variables, children, configurers, preprocessors, postprocessors);
    }
}
