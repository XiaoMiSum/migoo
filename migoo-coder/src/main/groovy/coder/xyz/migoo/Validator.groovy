package coder.xyz.migoo

class Validator extends El {

    Validator(String testClass, String field, String rule, Object... expected) {
        super(testClass)
        p("field", field)
        p("rule", rule)
        p("expected", expected)
    }

    static Validator withHTTPResponseValidator(String field = "status", String rule = "==", Object... expected) {
        return new Validator("HTTPAssertion", field, rule, expected)
    }

    static Validator withJSONValidator(String jsonPath, String rule = "==", Object... expected) {
        return new Validator("JSONAssertion", jsonPath, rule, expected)
    }

    static Validator withResultValidator(String rule = "==", Object... expected) {
        return new Validator("ResultAssertion", null, rule, expected)
    }
}
