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

import com.alibaba.fastjson2.JSONObject;
import core.xyz.migoo.engine.TestEngine;
import core.xyz.migoo.engine.Testplan;
import core.xyz.migoo.report.Reporter;
import core.xyz.migoo.report.Result;
import picocli.CommandLine;
import util.xyz.migoo.loader.Loader;
import xyz.migoo.report.StandardReporter;

import java.util.Map;

import static xyz.migoo.Constants.REPORT_CLASS;
import static xyz.migoo.Constants.REPORT_ENABLE;

/**
 * @author xiaomi
 */
public class MiGoo {

    static {
        var config = Loader.toJavaObject("props.migoo.yml", true, JSONObject.class);
        config.forEach((key, value) -> System.setProperty(key, String.valueOf(value)));
        printLogo();
    }

    private final JSONObject testcase;

    public MiGoo(Map<String, Object> testcase) {
        this.testcase = new JSONObject(testcase);
    }

    public static void main(String[] args) {
        System.exit(new CommandLine(new Cli()).execute(args));
    }

    private static void printLogo() {
        System.out.println("                _                          ");
        System.out.println("     _ __ ___  (_)  __ _   ___    ___      ");
        System.out.println("    | '_ ` _ \\ | | / _` | / _ \\  / _ \\     ");
        System.out.println("    | | | | | || || (_| || (_) || (_) |    ");
        System.out.println("    |_| |_| |_||_| \\__, | \\___/  \\___/     ");
        System.out.println("                   |___/     " + System.getProperty("migoo.version"));
        System.out.println("    GitHub: https://github.com/XiaoMiSum/migoo");
    }

    public static Result start(String filePath) {
        var testcase = Loader.toJavaObject(filePath, JSONObject.class);
        return start(testcase);
    }

    public static Result start(String filePath, boolean isClassPath) {
        var testcase = Loader.toJavaObject(filePath, isClassPath, JSONObject.class);
        return start(testcase);
    }

    public static Result start(Map<String, Object> testcase) {
        return new MiGoo(testcase).runTest();
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
}