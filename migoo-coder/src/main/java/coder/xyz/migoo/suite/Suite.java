package coder.xyz.migoo.suite;

import coder.xyz.migoo.Configurers;
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
    public static ISuite newSuite(String title, ISuite... children) {
        return new Testsuite(title, new HashMap<>(), children, new Configurers[]{}, null, null);
    }

    /**
     * 有公共变量的测试集合
     *
     * @param title     描述
     * @param variables 变量
     * @param children  子集合 或 测试用例
     * @return 测试集合
     */
    public static ISuite newSuite(String title, Map<String, Object> variables, ISuite... children) {
        return new Testsuite(title, variables, children, new Configurers[]{}, null, null);
    }

    /**
     * 最简单的测试用例
     *
     * @param title    描述
     * @param children 测试步骤
     * @return 测试用例
     */
    public static ISuite newTestcase(String title, Samplers... children) {
        return new Testcase(title, new HashMap<>(), children, new Configurers[]{}, null, null);
    }

    /**
     * 有公共变量的测试用例
     *
     * @param title     描述
     * @param variables 变量
     * @param children  测试步骤
     * @return 测试用例
     */
    public static ISuite newTestcase(String title, Map<String, Object> variables, Samplers... children) {
        return new Testcase(title, variables, children, new Configurers[]{}, null, null);
    }
}
