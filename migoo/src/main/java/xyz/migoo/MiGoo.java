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

import java.io.IOException;
import java.util.Map;

import static core.xyz.migoo.testelement.TestElementConstantsInterface.TEST_CLASS;

/**
 * @author xiaomi
 */
@SuppressWarnings({"unchecked"})
public class MiGoo {

    public static Configure CONFIGURE = null;

    static {

        try {
            CONFIGURE = TestDataLoader.toJavaObject("props.migoo.yml", Configure.class);
            printLogo();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        System.out.println("                   |___/     " + CONFIGURE.getVersion());
        System.out.println("    GitHub: https://github.com/XiaoMiSum/migoo");
    }

    public static Result start(String filePath) {
        try {
            var testcase = TestDataLoader.toJavaObject(filePath, JSONObject.class);
            return start(testcase);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static Result start(Map<String, Object> testcase) {
        return start(new JsonTree(testcase));
    }

    public static Result start(JsonTree testcase) {
        if (!ApplicationConfig.isRunInTestFrameworkSupport()) {
            // 如果不是在 junit 或者 testng 框架中运行，则创建一个 Session
            SessionRunner.newSession();
        }
        return new MiGoo(testcase).runTest();
    }

    private Result runTest() {
        var clazz = ApplicationConfig.getTestElementKeyMap().get(testcase.getString(TEST_CLASS));
        var result = SessionRunner.getSession().runTest(JSON.parseObject(testcase.toJSONString(), clazz));
        if (!CONFIGURE.getReport().isEnable()) {
            return result;
        }
        Reporter reporter;
        try {
            reporter = (Reporter) Class.forName(CONFIGURE.getReport().getReporter()).getConstructor().newInstance();
        } catch (Exception e) {
            reporter = new StandardReporter();
        }
        reporter.generateReport(result);
        return result;
    }
}