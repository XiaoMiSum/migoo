package coder.xyz.migoo.suite;

import coder.xyz.migoo.Configurers;
import coder.xyz.migoo.Processors;
import coder.xyz.migoo.Samplers;

import java.util.Map;

public class Testcase extends Testsuite {

    protected Testcase(String title, Map<String, Object> variables, Samplers[] children,
                       Configurers[] configurers, Processors.Pre[] preprocessors, Processors.Post[] postprocessors) {
        super(title, variables, children, configurers, preprocessors, postprocessors);
    }

}
