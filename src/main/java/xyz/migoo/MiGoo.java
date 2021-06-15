/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018. Lorem XiaoMiSum (mi_xiao@qq.com)
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining
 *  * a copy of this software and associated documentation files (the
 *  * 'Software'), to deal in the Software without restriction, including
 *  * without limitation the rights to use, copy, modify, merge, publish,
 *  * distribute, sublicense, and/or sell copies of the Software, and to
 *  * permit persons to whom the Software is furnished to do so, subject to
 *  * the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be
 *  * included in all copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 *  * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 *  * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 *  * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 *  * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package xyz.migoo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.engine.TestEngine;
import core.xyz.migoo.engine.TestPlan;
import core.xyz.migoo.report.Report;
import core.xyz.migoo.report.ReportService;
import core.xyz.migoo.report.Result;
import core.xyz.migoo.testelement.TestElement;
import xyz.migoo.engine.LoopEngine;
import xyz.migoo.engine.StandardEngine;
import xyz.migoo.readers.ReaderException;
import xyz.migoo.readers.ReaderFactory;
import xyz.migoo.report.StandardReport;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static core.xyz.migoo.testelement.AbstractTestElement.*;

public class MiGoo {

    private static final Pattern FILE_PATTERN = Pattern.compile("^@F(.+)+");

    private final JSONObject testcase;

    private final boolean generateReport;

    public MiGoo(JSONObject testcase) {
        this(testcase, true);
    }

    public MiGoo(JSONObject testcase, boolean generateReport) {
        this.testcase = initTestcase(testcase);
        this.generateReport = generateReport;
    }

    private JSONObject initTestcase(JSONObject testcase) {
        JSONObject newCase = new JSONObject(true);
        for (String key : testcase.keySet()) {
            Object value = testcase.get(key);
            if (value instanceof JSONArray) {
                newCase.put(key, initTestcase((JSONArray) value));
            } else if (value instanceof JSONObject) {
                newCase.put(key, initTestcase((JSONObject) value));
            } else {
                newCase.put(key, value instanceof String ? initTestcase((String) value) : value);
            }
        }
        return newCase;
    }

    private JSONArray initTestcase(JSONArray value) {
        JSONArray array = new JSONArray();
        for (Object object : value) {
            if (object instanceof JSONArray) {
                array.add(initTestcase((JSONArray) object));
            } else if (object instanceof JSONObject) {
                array.add(initTestcase((JSONObject) object));
            } else {
                array.add(object instanceof String ? initTestcase((String) object) : object);
            }
        }
        return array;
    }

    private Object initTestcase(String value) {
        try {
            Matcher matcher = FILE_PATTERN.matcher(value);
            if (matcher.find()) {
                Object result = ReaderFactory.getReader(matcher.group(1)).read();
                return result instanceof JSONArray ? initTestcase((JSONArray) result) :
                        result instanceof JSONObject ? initTestcase((JSONObject) result) : result;
            }
        } catch (ReaderException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return value;
    }

    public Result run() {
        TestPlan plan = new TestPlan(testcase);
        TestEngine engine = plan.level() == 0 ? new LoopEngine(plan) : new StandardEngine(plan);
        Result result = engine.run();
        if (generateReport) {
            this.generateReport((JSONObject) plan.get(REPORT_ELEMENT, new JSONObject()), result);
        }
        return result;
    }

    private void generateReport(JSONObject config, Result result) {
        config.put(TITLE, result.getTitle());
        config.putIfAbsent(TEST_CLASS, StandardReport.class.getSimpleName().toLowerCase());
        Report report = ReportService.getService(config.getString(TEST_CLASS));
        if (report instanceof TestElement) {
            ((StandardReport) report).setProperties(config);
        }
        report.generateReport(result);
    }

    public static final Map<String, Object> SYSTEM = new HashMap<>(10);

    static {
        SYSTEM.put("os.name", System.getProperty("os.name"));
        SYSTEM.put("java.runtime.name", System.getProperty("java.runtime.name"));
        SYSTEM.put("java.version", System.getProperty("java.version"));
        SYSTEM.put("java.vm.name", System.getProperty("java.vm.name"));
        try {
            JSONObject config = (JSONObject) ReaderFactory.getReader("classpath://props.migoo.yml").read();
            config.forEach(SYSTEM::put);
        } catch (ReaderException ignored) {

        }
    }

    public static void main(String[] args) {
        try {
            JSONObject yaml =  (JSONObject) ReaderFactory.getReader(args[0]).read();
            boolean generateReport = args.length < 2 || Boolean.parseBoolean(args[1]);
            new MiGoo(yaml, generateReport).run();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}