package coder.xyz.migoo.suite

import coder.xyz.migoo.Configurer
import coder.xyz.migoo.Processor
import coder.xyz.migoo.Sampler

class Testcase extends Testsuite {

    protected Testcase(String title, Map<String, Object> variables, Sampler[] children, Configurer[] configurers,
                       Processor[] preprocessors, Processor[] postprocessors) {
        super(title, variables, null, configurers, preprocessors, postprocessors)
        super.p("children", children)
    }

}
