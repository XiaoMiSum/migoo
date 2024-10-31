package coder.xyz.migoo

class Extractor extends El {

    Extractor(String testClass, String referenceName, String field = null, Integer matchNo = null) {
        super(testClass)
        p("variable_name", referenceName)
        p("field", field)
        p("match_num", matchNo)
    }

    static Extractor json(String referenceName, String jsonPath) {
        return new Extractor("JSONExtractor", referenceName, jsonPath)
    }

    static Extractor regex(String referenceName, String regex, Integer matchNo = null) {
        return new Extractor("RegexExtractor", referenceName, regex, matchNo)
    }

    static Extractor result(String referenceName) {
        return new Extractor("ResultExtractor", referenceName)
    }
}
