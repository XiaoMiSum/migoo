package coder.xyz.migoo

class Extractors extends El {

    Extractors(String testClass, String referenceName, String field = null, Integer matchNo = null) {
        super(testClass)
        p("variable_name", referenceName)
        p("field", field)
        p("match_num", matchNo)
    }

    static Extractors json(String referenceName, String jsonPath) {
        return new Extractors("JSONExtractor", referenceName, jsonPath)
    }

    static Extractors regex(String referenceName, String regex, Integer matchNo = null) {
        return new Extractors("RegexExtractor", referenceName, regex, matchNo)
    }

    static Extractors result(String referenceName) {
        return new Extractors("ResultExtractor", referenceName)
    }
}
