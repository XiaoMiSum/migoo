package coder.xyz.migoo.suite


import coder.xyz.migoo.El

class Testsuite extends El implements ISuite {

    protected Testsuite(String title, Map variables, List children, List configurers, List preprocessors, List postprocessors) {
        p("title", title)
        p("variables", variables)
        p("configelements", configurers)
        p("children", children)
        p("preprocessors", preprocessors)
        p("postprocessors", postprocessors)
    }
}
