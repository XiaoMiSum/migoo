package coder.xyz.migoo.suite

class Suite {

    static ISuite newSuite(String title, Map variables = null, Object[] children,
                           Object[] configurers = null, Object[] preprocessors = null, Object[] postprocessors = null) {
        return new Testsuite(title, variables, children, configurers, preprocessors, postprocessors)
    }

    static ISuite newTestcase(String title, Map variables = null, Object[] children,
                              Object[] configurers = null, Object[] preprocessors = null, Object[] postprocessors = null) {
        return new Testcase(title, variables, children, configurers, preprocessors, postprocessors)
    }
}
