package coder.xyz.migoo.suite

class Testcase extends Testsuite {

    protected Testcase(String title, Map variables, Object[] children, Object[] configurers, Object[] preprocessors,
                       Object[] postprocessors) {
        super(title, variables, children, configurers, preprocessors, postprocessors)
    }

}
