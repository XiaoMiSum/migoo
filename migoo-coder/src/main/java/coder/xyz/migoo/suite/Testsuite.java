package coder.xyz.migoo.suite;


import coder.xyz.migoo.Configurers;
import coder.xyz.migoo.El;
import coder.xyz.migoo.Processors;

import java.util.Map;

public class Testsuite extends El implements ISuite {

    protected Testsuite(String title, Map<String, Object> variables, Object[] children,
                        Configurers[] configurers, Processors[] preprocessors, Processors[] postprocessors) {
        p("title", title);
        p("variables", variables);
        p("configelements", configurers);
        p("children", children);
        p("preprocessors", preprocessors);
        p("postprocessors", postprocessors);
    }
}
