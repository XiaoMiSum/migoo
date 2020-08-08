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

import com.alibaba.fastjson.JSONObject;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import core.xyz.migoo.IResult;
import core.xyz.migoo.ISuiteResult;
import core.xyz.migoo.report.IReport;
import core.xyz.migoo.utils.DateUtil;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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
        ExtentSparkReporter reporter = new ExtentSparkReporter(outputDirectoryName);
        //reporter.config().setDocumentTitle(result.getTestName() + " Reports - Created by MiGoo");
        reporter.config().setDocumentTitle(" Reports - Created by MiGoo");
        // reporter.config().setReportName(result.getTestName() + " Reports");
        reporter.config().setReportName(" Reports");
        reporter.config().setTheme(Theme.DARK);
        //reporter.config().setResourceCDN(ResourceCDN.EXTENTREPORTS);

        extent = new ExtentReports();
        extent.attachReporter(reporter);
    }

    @Override
    public void generateReport(IResult result, String outputDirectoryName) {
        this.initReport(result, outputDirectoryName);
        extent.createTest("积分发放接口")
                .createNode("参数值为负数", "Jeff returns a faulty microwave")
                .fail(new NullPointerException("hah")).warning("这是响应信息").warning("这是请求信息");
        extent.createTest("积分发放接口")
                .createNode("参数值为负数", "Jeff returns a faulty microwave")
                .fail(new NullPointerException("hah")).warning("这是响应信息").warning("这是请求信息");
        extent.createTest("积分扣减接口")
                .createNode("参数值为负数").pass("pass").warning("这是响应信息").warning("这是请求信息");

        extent.flush();

// short-hand
        extent.createTest("MyFirstTest").createNode("MyFirstChildTest").pass("details");

// description

    }

    private void createSetNode(IResult result) {
        if (result instanceof ISuiteResult) {
            for (IResult iResult : ((ISuiteResult) result).getTestResults()) {
                ExtentTest feature = extent.createTest(iResult.getTestName());
                //feature.
            }
        }
    }

    private void createCaseNode(IResult result) {

    }

    public static void main(String[] args) {
        new Report().generateReport(null, "./reports");
    }

    @Override
    public void sendReport(Map<String, Object> config, String project, String outputDirectoryName) {
        HtmlEmail email = new HtmlEmail();
        email.setAuthentication((String) config.get("user"), (String) config.get("password"));
        email.setHostName((String) config.get("imaphost"));
        email.setCharset("UTF-8");
        String path = String.format("%s/%s/", System.getProperty("user.dir"), outputDirectoryName);
        String subject = project + " api test reports " + DateUtil.format(DateUtil.YYYY_MM_DD_HH_MM_SS);
        File zip = zipFile(path, "reports-" + DateUtil.TODAY_DATE);
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
