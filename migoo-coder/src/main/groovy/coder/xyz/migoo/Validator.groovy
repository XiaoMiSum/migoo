package coder.xyz.migoo

class Validator extends El {

    private Validator(String testClass, String field, String rule, Object... expected) {
        super(testClass)
        p("field", field)
        p("rule", rule)
        p("expected", (Object) expected)
    }

    static Validator response(String field = "status", String rule = "==", Object... expected) {
        return new Validator("HTTPAssertion", field, rule, expected)
    }

    static Validator json(String jsonPath, String rule = "==", Object... expected) {
        return new Validator("JSONAssertion", jsonPath, rule, expected)
    }

    static Validator result(String rule = "==", Object... expected) {
        return new Validator("ResultAssertion", null, rule, expected)
    }
}
