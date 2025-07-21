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

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import core.xyz.migoo.ApplicationConfig;
import core.xyz.migoo.SessionRunner;
import core.xyz.migoo.report.Reporter;
import core.xyz.migoo.report.Result;
import picocli.CommandLine;
import support.xyz.migoo.TestDataLoader;
import xyz.migoo.report.StandardReporter;

import java.util.Map;
import java.util.Objects;

import static core.xyz.migoo.testelement.TestElementConstantsInterface.TEST_CLASS;
import static xyz.migoo.Constants.REPORT_CLASS;
import static xyz.migoo.Constants.REPORT_ENABLE;

/**
 * @author xiaomi
 */
@SuppressWarnings({"unchecked"})
public class MiGoo {

    static {
        var config = TestDataLoader.toJavaObject("props.migoo.yml", true, JSONObject.class);
        config.forEach((key, value) -> System.setProperty(key, String.valueOf(value)));
        printLogo();
    }

    private final JsonTree testcase;

    public MiGoo(JsonTree testcase) {
        this.testcase = testcase;
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
        var testcase = TestDataLoader.toJavaObject(filePath, JSONObject.class);
        return start(testcase);
    }

    public static Result start(String filePath, boolean isClassPath) {
        var testcase = TestDataLoader.toJavaObject(filePath, isClassPath, JSONObject.class);
        return start(testcase);
    }

    public static Result start(Map<String, Object> testcase) {
        return start(new JsonTree(testcase));
    }

    public static Result start(JsonTree testcase) {
        SessionRunner.newSession();
        return new MiGoo(testcase).runTest();
    }

    private Result runTest() {
        var clazz = ApplicationConfig.getTestElementKeyMap().get(testcase.getString(TEST_CLASS));
        var result = SessionRunner.getSession().runTest(JSON.parseObject(testcase.toJSONString(), clazz));
        Reporter reporter = null;
        try {
            var enableReport = Boolean.parseBoolean(System.getProperty(REPORT_ENABLE, "true"));
            if (enableReport) {
                var reporterStrName = System.getProperty(REPORT_CLASS, "xyz.migoo.report.StandardReporter");
                reporter = (Reporter) Class.forName(reporterStrName).getConstructor().newInstance();
            }
        } catch (Exception e) {
            reporter = new StandardReporter();
        }
        if (Objects.nonNull(reporter)) {
            reporter.generateReport(result);
        }
        return result;
    }
}