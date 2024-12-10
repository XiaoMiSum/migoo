package coder.xyz.migoo;

public class Extractors extends El {

    Extractors(String testClass, String referenceName, String field, Integer matchNo) {
        super(testClass);
        p("variable_name", referenceName);
        p("field", field);
        p("match_num", matchNo);
    }

    public static Extractors json(String referenceName, String jsonPath) {
        return new Extractors("JSONExtractor", referenceName, jsonPath, null);
    }

    public static Extractors regex(String referenceName, String regex, Integer matchNo) {
        return new Extractors("RegexExtractor", referenceName, regex, matchNo);
    }

    public static Extractors result(String referenceName) {
        return new Extractors("ResultExtractor", referenceName, null, null);
    }
}
