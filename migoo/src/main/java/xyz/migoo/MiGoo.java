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
import core.xyz.migoo.engine.TestEngineService;
import core.xyz.migoo.engine.Testplan;
import core.xyz.migoo.report.Report;
import core.xyz.migoo.report.Result;
import itestx.xyz.migoo.logic.ITestx;
import xyz.migoo.loader.Loader;
import xyz.migoo.report.StandardReport;

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


    public MiGoo(JSONObject testcase) {
        this.testcase = this.prepare(testcase);
    }

    public MiGoo(ITestx x) {
        this.testcase = this.prepare(x.get());
    }

    public static Result start(ITestx x) {
        return new MiGoo(x).runTest();
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
        return new MiGoo(new JSONObject(testcase)).runTest();
    }

    private JSONObject prepare(Map<String, Object> x) {
        JSONObject json = new JSONObject(x.size());
        x.keySet().forEach(key -> {
            Object value = x.get(key);
            if (value instanceof Map) {
                json.put(key, prepare((Map<String, Object>) value));
            } else if (value instanceof Iterable) {
                json.put(key, prepare((List<Object>) value));
            } else if (value instanceof ITestx) {
                json.put(key, ((ITestx) value).get());
            } else {
                json.put(key, value);
            }
        });
        return json;
    }

    private JSONArray prepare(List<Object> x) {
        JSONArray xs = new JSONArray(x.size());
        x.forEach(item -> {
            if (item instanceof ITestx) {
                xs.add(prepare(((ITestx) item).get()));
            } else if (item instanceof Map) {
                xs.add(prepare((Map<String, Object>) item));
            } else {
                xs.add(item);
            }
        });
        return xs;
    }

    private Result runTest() {
        Report report;
        try {
            boolean flag = Boolean.parseBoolean(System.getProperty(REPORT_ENABLE, "true"));
            report = flag ? (Report) Class.forName(System.getProperty(REPORT_CLASS, "xyz.migoo.report.StandardReport"))
                    .getConstructor().newInstance() : null;
        } catch (Exception e) {
            report = new StandardReport();
        }
        return TestEngineService.runTest(new Testplan(testcase), report);
    }

    private JSONObject prepare(JSONObject testcase) {
        JSONObject newCase = new JSONObject();
        for (String key : testcase.keySet()) {
            Object value = testcase.get(key);
            if (value instanceof JSONArray) {
                newCase.put(key, prepare((JSONArray) value));
            } else if (value instanceof JSONObject) {
                newCase.put(key, prepare((JSONObject) value));
            } else {
                newCase.put(key, value instanceof String ? prepare((String) value) : value);
            }
        }
        return newCase;
    }

    private JSONArray prepare(JSONArray value) {
        JSONArray array = new JSONArray();
        for (Object object : value) {
            if (object instanceof JSONArray) {
                array.add(prepare((JSONArray) object));
            } else if (object instanceof JSONObject) {
                array.add(prepare((JSONObject) object));
            } else {
                array.add(object instanceof String ? prepare((String) object) : object);
            }
        }
        return array;
    }

    private Object prepare(String value) {
        Matcher matcher = FILE_PATTERN.matcher(value);
        if (matcher.find()) {
            Object result = Loader.toJSON(matcher.group(1));
            return result instanceof JSONArray ? prepare((JSONArray) result) :
                    result instanceof JSONObject ? prepare((JSONObject) result) : result;
        }
        return value;
    }
}