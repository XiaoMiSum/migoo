package coder.xyz.migoo.suite

class Suite {

    static ISuite newSuite(String title, Map variables = null, List children,
                           List configurers = null, List preprocessors = null, List postprocessors = null) {
        return new Testsuite(title, variables, children, configurers, preprocessors, postprocessors)
    }

    static ISuite newTestcase(String title, Map variables = null, List children,
                              List configurers = null, List preprocessors = null, List postprocessors = null) {
        return new Testcase(title, variables, children, configurers, preprocessors, postprocessors)
    }

    static void main(String[] args) {
        newSuite("", [:], [], [], [], [])
    }
}
