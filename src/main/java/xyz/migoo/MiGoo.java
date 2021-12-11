/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.engine.TestEngine;
import core.xyz.migoo.engine.TestPlan;
import core.xyz.migoo.report.Report;
import core.xyz.migoo.report.Result;
import xyz.migoo.engine.LoopEngine;
import xyz.migoo.engine.StandardEngine;
import xyz.migoo.readers.ReaderException;
import xyz.migoo.readers.ReaderFactory;
import xyz.migoo.report.StandardReport;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static core.xyz.migoo.report.Report.REPORT_CLASS;
import static core.xyz.migoo.report.Report.REPORT_ENABLE;

/**
 * @author xiaomi
 */
public class MiGoo {

    private static final Pattern FILE_PATTERN = Pattern.compile("^@F\\((.+)+\\)");

    static {
        try {
            JSONObject config = (JSONObject) ReaderFactory.getReader("classpath://props.migoo.yml").read();
            config.forEach((key, value) -> System.setProperty(key, String.valueOf(value)));
        } catch (Exception ignored) {
        }
    }

    private final JSONObject testcase;
    private final boolean generateReport;

    public MiGoo(JSONObject testcase) {
        this.testcase = testcase;
        generateReport = Boolean.parseBoolean(System.getProperty(REPORT_ENABLE, "true"));
    }

    public static void main(String[] args) {
        CommandLine.printLogo();
        try {
            if (args == null || args.length == 0 || "-h".equals(args[0]) || "-ext".equals(args[0])) {
                CommandLine.printHelp();
            } else {
                List<String> arrays = Arrays.asList(args);
                if (!arrays.contains("-h2m") && !arrays.contains("p2m") && !arrays.contains("-f")) {
                    CommandLine.unsupportedCommand();
                } else {
                    CommandLine.loadClasspath(!arrays.contains("-ext") ? "" : args[arrays.indexOf("-ext")],
                            !arrays.contains("-ext") ? "" : args[arrays.indexOf("-ext") + 1]);
                    CommandLine.convert2Sampler(arrays.get(0), arrays.get(1));
                    if (arrays.contains("-f")) {
                        CommandLine.run(arrays.indexOf("-f") + 1 > arrays.size() ? null : args[arrays.indexOf("-f") + 1],
                                !arrays.contains("-r") || arrays.indexOf("-r") + 1 > arrays.size() ? null : args[arrays.indexOf("-r") + 1]);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
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
            this.generateReport(result);
        }
        return result;
    }

    private void generateReport(Result result) {
        Report report;
        try {
            report = (Report) Class.forName(System.getProperty(REPORT_CLASS, "xyz.migoo.report.StandardReport")).newInstance();
        } catch (Exception e) {
            report = new StandardReport();
        }
        report.generateReport(result);
    }
}