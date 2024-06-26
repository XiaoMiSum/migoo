/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

package xyz.migoo;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import core.xyz.migoo.engine.TestEngine;
import core.xyz.migoo.engine.Testplan;
import core.xyz.migoo.report.Reporter;
import core.xyz.migoo.report.Result;
import util.xyz.migoo.loader.Loader;
import xyz.migoo.report.StandardReporter;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static xyz.migoo.Constants.REPORT_CLASS;
import static xyz.migoo.Constants.REPORT_ENABLE;

/**
 * @author xiaomi
 */
public class MiGoo {

    private static final Pattern FILE_PATTERN = Pattern.compile("^@F\\((.+)+\\)");

    static {
        JSONObject config = Loader.toJavaObject("props.migoo.yml", true, JSONObject.class);
        config.forEach((key, value) -> System.setProperty(key, String.valueOf(value)));
    }

    private final JSONObject testcase;

    public MiGoo(Map<String, Object> testcase) {
        this.testcase = this.prepare(testcase);
    }

    public static Result start(String filePath) {
        JSONObject testcase = Loader.toJavaObject(filePath, JSONObject.class);
        return start(testcase);
    }

    public static Result start(String filePath, boolean isClassPath) {
        JSONObject testcase = Loader.toJavaObject(filePath, isClassPath, JSONObject.class);
        return start(testcase);
    }

    public static Result start(Map<String, Object> testcase) {
        return new MiGoo(testcase).runTest();
    }

    private JSONObject prepare(Map<?, ?> x) {
        JSONObject json = new JSONObject(x.size());
        x.keySet().forEach(key -> {
            String keyString = (String) key;
            Object value = x.get(key);
            if (value instanceof Map<?, ?>) {
                json.put(keyString, prepare((Map<?, ?>) value));
            } else if (value instanceof List<?>) {
                json.put(keyString, prepare((List<?>) value));
            } else if (value instanceof String) {
                json.put(keyString, prepare((String) value));
            } else {
                json.put(keyString, value);
            }
        });
        return json;
    }

    private JSONArray prepare(List<?> x) {
        JSONArray xs = new JSONArray(x.size());
        x.forEach(item -> {
            if (item instanceof List<?>) {
                xs.add(prepare((List<?>) item));
            } else if (item instanceof Map) {
                xs.add(prepare((Map<?, ?>) item));
            } else if (item instanceof String) {
                xs.add(prepare((String) item));
            } else {
                xs.add(item);
            }
        });
        return xs;
    }

    private Result runTest() {
        Reporter reporter;
        try {
            reporter = Boolean.parseBoolean(System.getProperty(REPORT_ENABLE, "true")) ?
                    (Reporter) Class.forName(System.getProperty(REPORT_CLASS, "xyz.migoo.report.StandardReporter"))
                            .getConstructor().newInstance() : null;
        } catch (Exception e) {
            reporter = new StandardReporter();
        }
        return TestEngine.runTest(new Testplan(testcase), reporter);
    }


    private Object prepare(String value) {
        Matcher matcher = FILE_PATTERN.matcher(value);
        if (matcher.find()) {
            Object result = Loader.toJSON(matcher.group(1));
            return result instanceof List<?> ? prepare((List<?>) result) :
                    result instanceof Map<?, ?> ? prepare((Map<?, ?>) result) : result;
        }
        return value;
    }
}