package coder.xyz.migoo;

public class Extractors extends El {

    Extractors(String testClass, String referenceName, String field, Integer matchNo) {
        super(testClass);
        p("variable_name", referenceName);
        p("field", field);
        p("match_num", matchNo);
    }

    /**
     * json 提取器
     *
     * @param referenceName 提取结果引用名称
     * @param jsonPath      json path
     * @return json 提取器
     */
    public static Extractors json(String referenceName, String jsonPath) {
        return new Extractors("JSONExtractor", referenceName, jsonPath, null);
    }

    /**
     * regex 提取器
     *
     * @param referenceName 提取结果引用名称
     * @param regex         正则表达式
     * @param matchNo       匹配到多个结果时 取第几个
     * @return regex 提取器
     */
    public static Extractors regex(String referenceName, String regex, Integer matchNo) {
        return new Extractors("RegexExtractor", referenceName, regex, matchNo);
    }

    /**
     * result 提取器
     *
     * @param referenceName 提取结果引用名称
     * @return result 提取器
     */
    public static Extractors result(String referenceName) {
        return new Extractors("ResultExtractor", referenceName, null, null);
    }
}
