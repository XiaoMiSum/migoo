package coder.xyz.migoo;

import coder.xyz.migoo.protocol.*;

import java.util.HashMap;
import java.util.Map;

public class Samplers extends El {

    private Samplers(String title, String testClass, Map<String, Object> variables, El config, Validators[] validators,
                     Processors[] preprocessors, Processors[] postprocessors, Extractors[] extractors) {
        super(testClass);
        p("title", title);
        p("variables", variables);
        p("config", config.properties());
        p("validators", validators);
        p("preprocessors", preprocessors);
        p("postprocessors", postprocessors);
        p("extractors", extractors);
    }

    /**
     * 最简单的取样器配置
     *
     * @param title      描述
     * @param config     http 配置
     * @param validators 验证器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, HTTP config, Validators... validators) {
        return new Samplers(title, "http_sampler", new HashMap<>(), config, validators, new Processors[]{},
                new Processors[]{}, new Extractors[]{});
    }

    /**
     * 有变量的取样器
     *
     * @param title      描述
     * @param variables  变量
     * @param config     http 配置
     * @param validators 验证器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Map<String, Object> variables, HTTP config, Validators... validators) {
        return new Samplers(title, "http_sampler", variables, config, validators, new Processors[]{},
                new Processors[]{}, new Extractors[]{});
    }

    /**
     * 有验证器和提取器的取样器
     *
     * @param title      描述
     * @param variables  变量
     * @param config     http 配置
     * @param validators 验证器
     * @param extractors 提取器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Map<String, Object> variables, HTTP config, Validators[] validators,
                                   Extractors... extractors) {
        return new Samplers(title, "http_sampler", variables, config, validators, new Processors[]{},
                new Processors[]{}, extractors);
    }

    /**
     * 完整的取样器配置
     *
     * @param title          描述
     * @param variables      变量
     * @param config         http 配置
     * @param preprocessors  前置处理器
     * @param postprocessors 后置处理器
     * @param validators     验证器
     * @param extractors     提取器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Map<String, Object> variables, HTTP config, Validators[] validators,
                                   Processors[] preprocessors, Processors[] postprocessors, Extractors... extractors) {
        return new Samplers(title, "http_sampler", variables, config, validators, preprocessors, postprocessors, extractors);
    }

    /**
     * 最简单的取样器配置
     *
     * @param title      描述
     * @param config     jdbc 配置
     * @param validators 验证器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, JDBC config, Validators... validators) {
        return new Samplers(title, "jdbc_sampler", new HashMap<>(), config, validators, new Processors[]{},
                new Processors[]{}, new Extractors[]{});
    }

    /**
     * 有变量的取样器
     *
     * @param title      描述
     * @param variables  变量
     * @param config     jdbc 配置
     * @param validators 验证器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Map<String, Object> variables, JDBC config, Validators... validators) {
        return new Samplers(title, "jdbc_sampler", variables, config, validators, new Processors[]{},
                new Processors[]{}, new Extractors[]{});
    }

    /**
     * 有验证器和提取器的取样器
     *
     * @param title      描述
     * @param variables  变量
     * @param config     jdbc 配置
     * @param validators 验证器
     * @param extractors 提取器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Map<String, Object> variables, JDBC config, Validators[] validators,
                                   Extractors... extractors) {
        return new Samplers(title, "jdbc_sampler", variables, config, validators, new Processors[]{},
                new Processors[]{}, extractors);
    }

    /**
     * 完整的取样器配置
     *
     * @param title          描述
     * @param variables      变量
     * @param config         jdbc 配置
     * @param preprocessors  前置处理器
     * @param postprocessors 后置处理器
     * @param validators     验证器
     * @param extractors     提取器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Map<String, Object> variables, JDBC config, Validators[] validators,
                                   Processors[] preprocessors, Processors[] postprocessors, Extractors... extractors) {
        return new Samplers(title, "jdbc_sampler", variables, config, validators, preprocessors, postprocessors, extractors);
    }

    /**
     * 最简单的取样器配置
     *
     * @param title      描述
     * @param config     redis 配置
     * @param validators 验证器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Redis config, Validators... validators) {
        return new Samplers(title, "redis_sampler", new HashMap<>(), config, validators, new Processors[]{},
                new Processors[]{}, new Extractors[]{});
    }

    /**
     * 有变量的取样器
     *
     * @param title      描述
     * @param variables  变量
     * @param config     redis 配置
     * @param validators 验证器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Map<String, Object> variables, Redis config, Validators... validators) {
        return new Samplers(title, "redis_sampler", variables, config, validators, new Processors[]{},
                new Processors[]{}, new Extractors[]{});
    }

    /**
     * 有验证器和提取器的取样器
     *
     * @param title      描述
     * @param variables  变量
     * @param config     redis 配置
     * @param validators 验证器
     * @param extractors 提取器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Map<String, Object> variables, Redis config, Validators[] validators,
                                   Extractors... extractors) {
        return new Samplers(title, "redis_sampler", variables, config, validators, new Processors[]{},
                new Processors[]{}, extractors);
    }

    /**
     * 完整的取样器配置
     *
     * @param title          描述
     * @param variables      变量
     * @param config         redis 配置
     * @param preprocessors  前置处理器
     * @param postprocessors 后置处理器
     * @param validators     验证器
     * @param extractors     提取器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Map<String, Object> variables, Redis config, Validators[] validators,
                                   Processors[] preprocessors, Processors[] postprocessors, Extractors... extractors) {
        return new Samplers(title, "redis_sampler", variables, config, validators, preprocessors, postprocessors, extractors);
    }

    /**
     * 最简单的取样器配置
     *
     * @param title      描述
     * @param config     dubbo 配置
     * @param validators 验证器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Dubbo config, Validators... validators) {
        return new Samplers(title, "dubbo_sampler", new HashMap<>(), config, validators, new Processors[]{},
                new Processors[]{}, new Extractors[]{});
    }

    /**
     * 有变量的取样器
     *
     * @param title      描述
     * @param variables  变量
     * @param config     dubbo 配置
     * @param validators 验证器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Map<String, Object> variables, Dubbo config, Validators... validators) {
        return new Samplers(title, "dubbo_sampler", variables, config, validators, new Processors[]{},
                new Processors[]{}, new Extractors[]{});
    }

    /**
     * 有验证器和提取器的取样器
     *
     * @param title      描述
     * @param variables  变量
     * @param config     dubbo 配置
     * @param validators 验证器
     * @param extractors 提取器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Map<String, Object> variables, Dubbo config, Validators[] validators,
                                   Extractors... extractors) {
        return new Samplers(title, "dubbo_sampler", variables, config, validators, new Processors[]{},
                new Processors[]{}, extractors);
    }

    /**
     * 完整的取样器配置
     *
     * @param title          描述
     * @param variables      变量
     * @param config         dubbo 配置
     * @param preprocessors  前置处理器
     * @param postprocessors 后置处理器
     * @param validators     验证器
     * @param extractors     提取器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Map<String, Object> variables, Dubbo config, Validators[] validators,
                                   Processors[] preprocessors, Processors[] postprocessors, Extractors... extractors) {
        return new Samplers(title, "dubbo_sampler", variables, config, validators, preprocessors, postprocessors, extractors);
    }

    /**
     * 最简单的取样器配置
     *
     * @param title      描述
     * @param config     active_mq 配置
     * @param validators 验证器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, ActiveMQ config, Validators... validators) {
        return new Samplers(title, "active_mq_sampler", new HashMap<>(), config, validators, new Processors[]{},
                new Processors[]{}, new Extractors[]{});
    }

    /**
     * 有变量的取样器
     *
     * @param title      描述
     * @param variables  变量
     * @param config     active_mq 配置
     * @param validators 验证器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Map<String, Object> variables, ActiveMQ config, Validators... validators) {
        return new Samplers(title, "active_mq_sampler", variables, config, validators, new Processors[]{},
                new Processors[]{}, new Extractors[]{});
    }

    /**
     * 有验证器和提取器的取样器
     *
     * @param title      描述
     * @param variables  变量
     * @param config     active_mq 配置
     * @param validators 验证器
     * @param extractors 提取器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Map<String, Object> variables, ActiveMQ config, Validators[] validators,
                                   Extractors... extractors) {
        return new Samplers(title, "active_mq_sampler", variables, config, validators, new Processors[]{},
                new Processors[]{}, extractors);
    }

    /**
     * 完整的取样器配置
     *
     * @param title          描述
     * @param variables      变量
     * @param config         active_mq 配置
     * @param preprocessors  前置处理器
     * @param postprocessors 后置处理器
     * @param validators     验证器
     * @param extractors     提取器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Map<String, Object> variables, ActiveMQ config, Validators[] validators,
                                   Processors[] preprocessors, Processors[] postprocessors, Extractors... extractors) {
        return new Samplers(title, "active_mq_sampler", variables, config, validators, preprocessors, postprocessors, extractors);
    }

    /**
     * 最简单的取样器配置
     *
     * @param title      描述
     * @param config     kafka配置
     * @param validators 验证器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Kafka config, Validators... validators) {
        return new Samplers(title, "kafka_sampler", new HashMap<>(), config, validators, new Processors[]{},
                new Processors[]{}, new Extractors[]{});
    }

    /**
     * 有变量的取样器
     *
     * @param title      描述
     * @param variables  变量
     * @param config     kafka配置
     * @param validators 验证器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Map<String, Object> variables, Kafka config, Validators... validators) {
        return new Samplers(title, "kafka_sampler", variables, config, validators, new Processors[]{},
                new Processors[]{}, new Extractors[]{});
    }

    /**
     * 有验证器和提取器的取样器
     *
     * @param title      描述
     * @param variables  变量
     * @param config     kafka配置
     * @param validators 验证器
     * @param extractors 提取器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Map<String, Object> variables, Kafka config, Validators[] validators,
                                   Extractors... extractors) {
        return new Samplers(title, "kafka_sampler", variables, config, validators, new Processors[]{},
                new Processors[]{}, extractors);
    }

    /**
     * 完整的取样器配置
     *
     * @param title          描述
     * @param variables      变量
     * @param config         kafka 配置
     * @param preprocessors  前置处理器
     * @param postprocessors 后置处理器
     * @param validators     验证器
     * @param extractors     提取器
     * @return 取样器配置
     */
    public static Samplers sampler(String title, Map<String, Object> variables, Kafka config, Validators[] validators,
                                   Processors[] preprocessors, Processors[] postprocessors, Extractors... extractors) {
        return new Samplers(title, "kafka_sampler", variables, config, validators, preprocessors, postprocessors, extractors);
    }

}
