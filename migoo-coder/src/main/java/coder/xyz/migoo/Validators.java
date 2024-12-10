package coder.xyz.migoo;

public class Validators extends El {

    private Validators(String testClass, String field, String rule, Object... expected) {
        super(testClass);
        p("field", field);
        p("rule", rule);
        p("expected", (Object) expected);
    }

    /**
     * Http 断言，默认验证内容 body，默认验证规则 相等
     *
     * @param expected 期望值（支持多个）
     * @return 验证器配置
     */
    public static Validators response(Object... expected) {
        return new Validators("HTTPAssertion", "body", "==", expected);
    }

    /**
     * Http 断言， 默认验证规则 相等
     *
     * @param field    验证内容
     * @param expected 期望值（支持多个）
     * @return 验证器配置
     */
    public static Validators response(String field, Object... expected) {
        return new Validators("HTTPAssertion", field, "==", expected);
    }

    /**
     * Http 断言
     *
     * @param field    验证内容
     * @param rule     验证规则
     * @param expected 期望值（支持多个）
     * @return 验证器配置
     */
    public static Validators response(String field, String rule, Object... expected) {
        return new Validators("HTTPAssertion", field, rule, expected);
    }

    /**
     * json 断言， 默认验证规则 相等
     *
     * @param jsonPath 验证内容
     * @param expected 期望值（支持多个）
     * @return 验证器配置
     */
    public static Validators json(String jsonPath, Object... expected) {
        return new Validators("JSONAssertion", jsonPath, "==", expected);
    }

    /**
     * json 断言， 默认验证规则 相等
     *
     * @param jsonPath 验证内容
     * @param rule     验证规则
     * @param expected 期望值（支持多个）
     * @return 验证器配置
     */
    public static Validators json(String jsonPath, String rule, Object... expected) {
        return new Validators("JSONAssertion", jsonPath, rule, expected);
    }

    /**
     * 结果 断言，默认验证规则 相等
     *
     * @param expected 期望值（支持多个）
     * @return 验证器配置
     */
    public static Validators result(Object... expected) {
        return new Validators("ResultAssertion", null, "==", expected);
    }

    /**
     * 结果 断言，默认验证规则 相等
     *
     * @param rule     验证规则
     * @param expected 期望值（支持多个）
     * @return 验证器配置
     */
    public static Validators result(String rule, Object... expected) {
        return new Validators("ResultAssertion", null, rule, expected);
    }
}
