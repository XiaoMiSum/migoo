package coder.xyz.migoo.suite

import coder.xyz.migoo.Configurer
import coder.xyz.migoo.El
import coder.xyz.migoo.Processor

class Testsuite extends El implements ISuite {

    protected Testsuite(String title, Map<String, Object> variables, ISuite[] children, Configurer[] configurers,
                        Processor[] preprocessors, Processor[] postprocessors) {
        p("title", title)
        p("variables", variables)
        p("configelements", configurers)
        p("children", children)
        p("preprocessors", preprocessors)
        p("postprocessors", postprocessors)
    }
}
