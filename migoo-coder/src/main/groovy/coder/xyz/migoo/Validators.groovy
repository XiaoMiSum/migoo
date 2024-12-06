package coder.xyz.migoo

class Validators extends El {

    private Validators(String testClass, String field, String rule, Object... expected) {
        super(testClass)
        p("field", field)
        p("rule", rule)
        p("expected", (Object) expected)
    }

    static Validators response(String field = "status", String rule = "==", Object... expected) {
        return new Validators("HTTPAssertion", field, rule, expected)
    }

    static Validators json(String jsonPath, String rule = "==", Object... expected) {
        return new Validators("JSONAssertion", jsonPath, rule, expected)
    }

    static Validators result(String rule = "==", Object... expected) {
        return new Validators("ResultAssertion", null, rule, expected)
    }
}
