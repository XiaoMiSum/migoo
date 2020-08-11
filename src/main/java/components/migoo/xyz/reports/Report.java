/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
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
 *
 */

package components.migoo.xyz.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import core.xyz.migoo.IResult;
import core.xyz.migoo.ISuiteResult;
import core.xyz.migoo.ITestResult;
import core.xyz.migoo.TestChecker;
import core.xyz.migoo.http.MiGooRequest;
import core.xyz.migoo.report.IReport;
import core.xyz.migoo.utils.DateUtil;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.migoo.simplehttp.Request;
import xyz.migoo.simplehttp.Response;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author xiaomi
 * @date 2020/7/31 13:47
 */
public class Report implements IReport {

    private static final Logger logger = LoggerFactory.getLogger("migoo");

    public static void log(String msg) {
        logger.info(msg);
    }

    public static void log(String msg, Object... args) {
        logger.info(msg, args);
    }

    public static void log(String msg, Throwable t) {
        logger.info(msg, t);
    }

    private ExtentReports extent;

    private void initReport(IResult result, String outputDirectoryName) {
        ExtentSparkReporter reporter = new ExtentSparkReporter(outputDirectoryName + "/index.html");
        reporter.config().setDocumentTitle(result.getTestName() + " Reports - Created by MiGoo");
        reporter.config().setReportName(result.getTestName() + " Reports"
                + "</span></a></li>\n<li><a href='https://github.com/XiaoMiSum/MiGoo' target=\"_blank\"><span class=\"badge badge-primary\">migoo - github");
        reporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
        reporter.config().setEncoding("UTF-8");
        reporter.config().setTheme(Theme.DARK);
        reporter.config().enableOfflineMode(true);
        reporter.config().setTimelineEnabled(false);
        extent = new ExtentReports();
        extent.attachReporter(reporter);
    }

    @Override
    public void generateReport(IResult result, String outputDirectoryName) {
        this.initReport(result, outputDirectoryName);
        for (IResult iSuiteResult : ((ISuiteResult) result).getTestResults()) {
            ISuiteResult suiteResult = (ISuiteResult) iSuiteResult;
            ExtentTest feature = extent.createTest(iSuiteResult.getTestName(),
                    String.format("用例总数：%s，成功：%s，失败：%s，错误：%s，跳过：%s", suiteResult.size(),
                            suiteResult.getSuccessCount(), suiteResult.getFailureCount(), suiteResult.getErrorCount(),
                            suiteResult.getSkipCount()));
            feature.getModel().setStartTime(iSuiteResult.getStartTime());
            feature.getModel().setEndTime(iSuiteResult.getEndTime());
            for (IResult testResult : suiteResult.getTestResults()) {
                ITestResult iTestResult = (ITestResult) testResult;
                ExtentTest node = feature.createNode(testResult.getTestName());
                node.getModel().setStartTime(testResult.getStartTime());
                node.getModel().setEndTime(testResult.getEndTime());
                if (iTestResult.getRequest() != null) {
                    node.info(iTestResult.getRequest().uriNotContainsParam());
                    if (iTestResult.getRequest().headers() != null || iTestResult.getRequest().headers().length > 0) {
                        node.info(Arrays.toString(iTestResult.getRequest().headers()));
                    }
                    if (!iTestResult.getRequest().body().isEmpty()) {
                        node.info(iTestResult.getRequest().body());
                    }
                    if (!iTestResult.getRequest().data().isEmpty()) {
                        node.info(iTestResult.getRequest().data());
                    }
                    if (!iTestResult.getRequest().query().isEmpty()) {
                        node.info(iTestResult.getRequest().query());
                    }
                    if (!iTestResult.getResponse().text().isEmpty()) {
                        node.info(iTestResult.getResponse().text());
                    }
                }
                if (!testResult.isError()) {
                    for (TestChecker checker : iTestResult.getCheckers()) {
                        Markup m = MarkupHelper.createCodeBlock(checker.toString(), CodeLanguage.JSON);
                        node = checker.isSuccess() ? node.pass(m) : checker.isSkipped() ? node.skip(m)
                                : checker.isFailure() && checker.getThrowable() != null ? node.fail(m).fail(checker.getThrowable())
                                : node.fail(m);

                    }
                }
            }
        }
        extent.flush();
    }

    private String getDetailTable(Request request, Response response) {
        String format = "\n<table class=\"table table-sm\">\n<tbody>\n%s%s%s%s\n</tbody>\n</table>\n";
        String url = "<tr class=\"event-row\"><td><span>Url</span></td>\n" +
                "        <td>\n" +
                "          <span>" + request.uriNotContainsParam() + "</span>" +
                "        </td></tr>\n";
        String headers = request.headers() == null || request.headers().length == 0 ? "" :
                "<tr class=\"event-row\"><td><span>Headers</span></td>\n" +
                        "        <td>\n" +
                        "          <span>" + Arrays.toString(request.headers()) + "</span>" +
                        "        </td></tr>\n";
        String body = !request.body().isEmpty() ? "<tr class=\"event-row\"><td><span>Body</span></td>\n" +
                "        <td>\n" +
                "          <span>" + request.body() + "</span>" +
                "        </td></tr>\n" :
                !request.query().isEmpty() ? "<tr class=\"event-row\"><td><span>Query</span></td>\n" +
                        "        <td>\n" +
                        "          <span>" + request.query() + "</span>" +
                        "        </td></tr>\n" :
                        !request.data().isEmpty() ? "<tr class=\"event-row\"><td><span>Data</span></td>\n" +
                                "        <td>\n" +
                                "          <span>" + request.data() + "</span>" +
                                "        </td></tr>\n" : "";
        String responseText = !response.text().isEmpty() ? "<tr class=\"event-row\"><td><span>Response</span></td>\n" +
                "        <td>\n" +
                "          <span>" + response.text() + "</span>" +
                "        </td></tr>\n" : "";
        return String.format(format, url, headers, body, responseText);
    }

    @Override
    public void sendReport(Map<String, Object> config, String project, String outputDirectoryName) {
        if (config != null) {
            HtmlEmail email = new HtmlEmail();
            email.setAuthentication((String) config.get("user"), (String) config.get("password"));
            email.setHostName((String) config.get("imaphost"));
            email.setCharset("UTF-8");
            String subject = project + " api test reports " + DateUtil.format(DateUtil.YYYY_MM_DD_HH_MM_SS);
            File zip = zipFile(outputDirectoryName, "reports-" + DateUtil.TODAY_DATE);
            try {
                email.setFrom((String) config.get("user"));
                for (String to : (List<String>) config.get("tolist")) {
                    email.addTo(to);
                }
                email.setSubject(subject);
                email.setMsg("附件为本次 '" + project + "项目' 的接口测试报告，请查收!" +
                        "\n\n" +
                        "这是由 <a href = \"https://github.com/XiaoMiSum/MiGoo\">migoo接口测试工具</a> 发送的一封邮件!");
                email.attach(zip);
                email.send();
            } catch (EmailException e) {
                Report.log("email send error.", e);
            }
            zip.delete();
        }
    }

    private File zipFile(String path, String fileName) {
        Project project = new Project();
        FileSet fileSet = new FileSet();
        fileSet.setExcludes("*.zip");
        fileSet.setDir(new File(path));
        Zip zip = new Zip();
        zip.setProject(project);
        zip.setDestFile(new File(path + fileName + ".zip"));
        zip.addFileset(fileSet);
        zip.execute();
        return new File(path + fileName + ".zip");
    }
}
