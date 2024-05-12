package coder.xyz.migoo.suite

import coder.xyz.migoo.Configurer
import coder.xyz.migoo.Processor
import coder.xyz.migoo.Sampler

class Suite {

    static ISuite newSuite(String title, Map<String, Object> variables = null, ISuite[] children,
                           Configurer[] configurers = null, Processor[] preprocessors = null, Processor[] postprocessors = null) {
        return new Testsuite(title, variables, children, configurers, preprocessors, postprocessors)
    }

    static ISuite newTestcase(String title, Map<String, Object> variables = null, Sampler[] children,
                              Configurer[] configurers = null, Processor[] preprocessors = null, Processor[] postprocessors = null) {
        return new Testcase(title, variables, children, configurers, preprocessors, postprocessors)
    }
}
