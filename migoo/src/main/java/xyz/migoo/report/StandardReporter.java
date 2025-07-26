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

package xyz.migoo.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.gherkin.model.Scenario;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import core.xyz.migoo.TestStatus;
import core.xyz.migoo.report.Reporter;
import core.xyz.migoo.report.Result;
import core.xyz.migoo.testelement.TestSuiteResult;
import core.xyz.migoo.testelement.sampler.SampleResult;
import xyz.migoo.report.util.DateUtils;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.aventstack.extentreports.Status.*;
import static xyz.migoo.Constants.*;

/**
 * @author xiaomi
 */
public class StandardReporter implements Reporter {

    private ExtentReports extent;

    public StandardReporter() {

    }

    private void initialized() {
        extent = new ExtentReports();
        extent.setSystemInfo("os.name", System.getProperty("os.name"));
        extent.setSystemInfo("java.runtime.name", System.getProperty("java.runtime.name"));
        extent.setSystemInfo("java.version", System.getProperty("java.version"));
        extent.setSystemInfo("java.vm.name", System.getProperty("java.vm.name"));
        extent.setSystemInfo("migoo.version", System.getProperty("migoo.version"));
    }

    @Override
    public void generateReport(Result result) {
        initialized();
        writeResult(extent, result, 1);
        flush(result);
    }

    private void writeResult(Object feature, Result result, int tier) {
        if (result.isAnomalous()) {
            if (feature instanceof ExtentReports reports) {
                reports.createTest(result.getTitle()).fail(result.getTrack());
            } else {
                ((ExtentTest) feature).fail(result.getTrack());
            }
        }
        if (result instanceof SampleResult sampleResult) {
            writeProcessors(feature, result.getPreprocessors(), "前置", tier);
            writeSampler(feature, sampleResult, tier);
            writeProcessors(feature, result.getPostprocessors(), "后置", tier);

            return;
        }
        if (result instanceof TestSuiteResult testSuiteResult) {
            if (!result.getPreprocessors().isEmpty() || !result.getPostprocessors().isEmpty()) {
                writeProcessors(feature, result.getPreprocessors(), "前置", tier);
                writeProcessors(feature, result.getPostprocessors(), "后置", tier);
            }
            for (var child : testSuiteResult.getChildren()) {
                var node = createNode(feature, child.getTitle());
                writeResult(node, child, tier + 1);
                node.getModel().setStatus(child.getStatus() == TestStatus.failed ? FAIL : PASS);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void writeProcessors(Object feature, List<? extends Result> results, String prefix, int tier) {
        if (!results.isEmpty()) {
            var res = (List<SampleResult>) results;
            var node = createNode(feature, prefix + "处理器");
            // 测试报告级别为取样器，遍历打印处理器类型 和 处理器元件内容
            res.forEach(item -> {
                var node1 = tier == 1 ? createNode(createNode(node, item.getTitle()), "-") :
                        tier == 2 ? createNode(node, item.getTitle()) : node.info(item.getTitle());
                var content = getContent(item);
                if (!content.isEmpty()) {
                    node1.info(MarkupHelper.createCodeBlock(content));
                }
                if (item.isAnomalous()) {
                    node1.fail(item.getTrack());
                }
            });
            node.getModel().setStatus(res.stream().anyMatch(item -> item.getStatus() == TestStatus.failed) ? FAIL : INFO);
        }
    }

    private void writeSampler(Object feature, SampleResult result, int tier) {
        // suite tier = 4  set tier = 3  testcase tier = 2  sampler tier = 1
        var container = tier < 4 ? createNode(feature, result.getTitle()) : (ExtentTest) feature;
        var container2 = tier == 1 ? createNode(container, result.getTitle()) : container;
        var node = tier < 3 ? createNode(container2, "-") : tier == 3 ? container2.info("-") : container2;
        var content = getContent(result);
        if (!content.isEmpty()) {
            node.info(MarkupHelper.createCodeBlock(content));
        }
        node.getModel().setStatus(result.getStatus() == TestStatus.failed ? FAIL : PASS);
        container2.getModel().setStatus(result.getStatus() == TestStatus.failed ? FAIL : PASS);
        container.getModel().setStatus(result.getStatus() == TestStatus.failed ? FAIL : PASS);
    }

    private String getContent(SampleResult result) {
        var content = new StringBuilder();

        if (result.getRequest() != null) {
            content.append("请求信息\n").append(result.getRequest().format());
        }

        if (result.getResponse() != null && result.getResponse().bytes() != null && result.getResponse().bytes().length > 0) {
            content.append("\n").append("响应信息\n").append(result.getResponse().format());
        }

        content.append(!result.getAssertions().isEmpty() ? "\n" : "");
        result.getAssertions().forEach(assertion ->
                content.append("\n").append(assertion.getTitle()).append(assertion.getMessage())
        );

        content.append(!result.getExtractors().isEmpty() ? "\n" : "");
        result.getExtractors().forEach(extractor -> {
            content.append("\n").append(extractor.getTitle());
            if (extractor.getStatus() == TestStatus.passed) {
                content.append("  结果：").append(extractor.getValue());
            } else {
                content.append("\n").append(Objects.isNull(extractor.getException()) ? extractor.getMessage() : extractor.getException());
            }
        });
        return content.toString();
    }

    private ExtentTest createNode(Object feature, String record) {
        ExtentTest node;
        if (feature instanceof ExtentReports report) {
            node = report.createTest(Scenario.class, record);
        } else {
            node = ((ExtentTest) feature).createNode(Scenario.class, record);
        }
        return node;
    }

    private void flush(Result result) {
        var outputDirectoryName = System.getProperty(REPORT_OUTPUT, "./out-put") + "/"
                + result.getTitle() + "_" + DateUtils.nowStr();
        var reporter = new ExtentSparkReporter(outputDirectoryName + "/index.html");
        reporter.config().setDocumentTitle(result.getTitle() + " 测试报告 - Generated by migoo");
        reporter.config().setReportName(result.getTitle() + " 测试报告</span></a></li>\n" +
                "<li><a href='https://github.com/XiaoMiSum/migoo' target=\"_blank\"><span>" +
                "<img src=\"https://img.shields.io/badge/MiGoo-xiaomi-yellow.svg?style=social&amp;logo=github\">");
        reporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
        reporter.config().setTheme(Theme.valueOf(System.getProperty(REPORT_THEME, "DARK").toUpperCase(Locale.ROOT)));
        reporter.config().enableOfflineMode(Boolean.parseBoolean(System.getProperty(REPORT_OFFLINE, "true")));
        reporter.config().setTimelineEnabled(Boolean.parseBoolean(System.getProperty(TIMELINE_ENABLED, "false")));
        reporter.config().setEncoding("UTF-8");
        extent.attachReporter(reporter);
        extent.getReport().setStartTime(DateUtils.toDate(result.getStartTime()));
        extent.getReport().setEndTime(DateUtils.toDate(result.getEndTime()));
        extent.flush();
    }


}