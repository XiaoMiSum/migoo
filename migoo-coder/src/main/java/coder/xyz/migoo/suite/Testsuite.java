package coder.xyz.migoo.suite;


import coder.xyz.migoo.Configurers;
import coder.xyz.migoo.El;
import coder.xyz.migoo.Processors;

import java.util.Map;

public class Testsuite extends El implements ISuite {

    protected Testsuite(String title, Map<String, Object> variables, Object[] children,
                        Configurers[] configurers, Processors.Pre[] preprocessors, Processors.Post[] postprocessors) {
        p("title", title);
        p("variables", variables);
        p("configelements", configurers);
        p("children", children);
        p("preprocessors", preprocessors);
        p("postprocessors", postprocessors);
    }

    /**
     * 设置测试集合的配置元件
     *
     * @param configurers 前置处理器
     * @return 当前测试集合
     */
    public Testsuite configurers(Configurers... configurers) {
        p("configelements", configurers);
        return this;
    }

    /**
     * 设置测试集合的前置处理器
     *
     * @param preprocessors 前置处理器
     * @return 当前测试集合
     */
    public Testsuite preprocessors(Processors.Pre... preprocessors) {
        p("preprocessors", preprocessors);
        return this;
    }

    /**
     * 设置测试集合的后置处理器
     *
     * @param postprocessors 后置处理器
     * @return 当前测试集合
     */
    public Testsuite postprocessors(Processors.Post... postprocessors) {
        p("postprocessors", postprocessors);
        return this;
    }
}
