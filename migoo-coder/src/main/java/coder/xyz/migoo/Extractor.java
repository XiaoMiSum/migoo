/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2024.  Lorem XiaoMiSum (mi_xiao@qq.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * 'Software'), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package coder.xyz.migoo;

public class Extractor extends El {

    Extractor(String testClass, String referenceName) {
        this(testClass, referenceName, null);
    }

    Extractor(String testClass, String referenceName, String field) {
        this(testClass, referenceName, field, null);
    }

    Extractor(String testClass, String referenceName, String field, Integer matchNo) {
        super(testClass);
        p("variable_name", referenceName);
        p("field", field);
        p("match_num", matchNo);
    }

    public static Extractor withJSONExtractor(String referenceName, String field) {
        return withJSONExtractor(referenceName, field, null);
    }

    public static Extractor withJSONExtractor(String referenceName, String field, Integer matchNo) {
        return new Extractor("JSONExtractor", referenceName, field, matchNo);
    }

    public static Extractor withRegexExtractor(String referenceName, String regex) {
        return withRegexExtractor(referenceName, regex, null);
    }

    public static Extractor withRegexExtractor(String referenceName, String regex, Integer matchNo) {
        return new Extractor("RegexExtractor", referenceName, regex, matchNo);
    }

    public static Extractor withResultExtractor(String referenceName) {
        return new Extractor("ResultExtractor", referenceName);
    }
}
