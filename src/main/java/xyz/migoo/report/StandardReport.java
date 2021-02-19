/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2021. Lorem XiaoMiSum (mi_xiao@qq.com)
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

package xyz.migoo.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import core.xyz.migoo.assertions.AssertionResult;
import core.xyz.migoo.report.Report;
import core.xyz.migoo.samplers.SampleResult;
import core.xyz.migoo.testelement.AbstractTestElement;
import protocol.xyz.migoo.http.sampler.HTTPSampleResult;
import xyz.migoo.report.util.CharSequenceTranslator;
import xyz.migoo.report.util.DateUtils;

import static xyz.migoo.MiGoo.SYSTEM;

public class StandardReport extends AbstractTestElement implements Report {

    @Override
    public void generateReport(SampleResult result) {
        ExtentReports extent = new ExtentReports();
        testStarted(result, extent);
        if (result.getAssertionResults() != null && result.getAssertionResults().size() > 0) {
            generateExtentReport(result, extent.createTest(result.getTitle()));
        } else if (result.getSubResults() != null && result.getSubResults().size() > 0) {
            for (SampleResult subResult : result.getSubResults()) {
                if (subResult.getSubResults() != null || subResult.getAssertionResults() != null) {
                    generateExtentReport(subResult, extent.createTest(subResult.getTitle()));
                }
            }
        }
        extent.flush();
    }

    private void generateExtentReport(SampleResult result, ExtentTest feature) {
        feature.getModel().setStartTime(DateUtils.toDate(result.getStartTime()));
        if (result.getType() == 0 && result.getSubResults() != null) {
            for (SampleResult subResult : result.getSubResults()) {
                ExtentTest story = feature.createNode(subResult.getTitle());
                story.getModel().setStartTime(DateUtils.toDate(subResult.getStartTime()));
                generateExtentReport(subResult, story);
                story.getModel().setEndTime(DateUtils.toDate(subResult.getEndTime()));
            }
        } else {
            feature.info(result.getUrl() + " " + result.getResponseCode() + " " + result.getResponseMessage());
            if (result instanceof HTTPSampleResult && ((HTTPSampleResult) result).getCookies() != null) {
                feature.info(((HTTPSampleResult) result).getCookies());
            }
            if (result.getRequestHeaders() != null) {
                feature.info(result.getRequestHeaders());
            }
            feature.info(result.getSamplerData());
            feature.info(getResponseString(result.getResponseDataAsString()));
            if (result.getAssertionResults() != null) {
                for (AssertionResult subResult : result.getAssertionResults()) {
                    ExtentTest node = feature.createNode(subResult.getName());
                    if (subResult.isSuccessful()) {
                        node.pass(subResult.getContext());
                    } else {
                        node.fail(subResult.getFailureMessage());
                    }
                }
            }
            feature.getModel().setEndTime(DateUtils.toDate(result.getEndTime()));
        }
        feature.getModel().setEndTime(DateUtils.toDate(result.getEndTime()));
    }

    private String getResponseString(String text) {
        if (text.startsWith("<") && text.endsWith(">")) {
            return CharSequenceTranslator.escapeHtml4(text);
        }
        return text;
    }

    private void testStarted(SampleResult result, ExtentReports extent) {
        Object outputDirectoryName = get(OUTPUT_DIRECTORY_NAME, "./out-put");
        Object title = get(TITLE, "MiGoo");
        ExtentSparkReporter reporter = new ExtentSparkReporter(outputDirectoryName + "/index.html");
        reporter.config().setDocumentTitle(title + " Reports - Created by MiGoo");
        reporter.config().setReportName(title + " Reports</span></a></li>\n" +
                "<li><a href='https://github.com/XiaoMiSum/MiGoo' target=\"_blank\"><span>" +
                "<img src=\"https://img.shields.io/badge/MiGoo-By Mi.xiao-yellow.svg?style=social&amp;logo=github\">");
        reporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
        reporter.config().setTheme(Theme.DARK);
        reporter.config().enableOfflineMode((Boolean) get(ENABLE_OFFLINE_MODE, true));
        reporter.config().setTimelineEnabled(false);

        extent.attachReporter(reporter);
        extent.getReport().setStartTime(DateUtils.toDate(result.getStartTime()));
        extent.getReport().setEndTime(DateUtils.toDate(result.getEndTime()));
        SYSTEM.forEach((key, value) -> extent.setSystemInfo(key, String.valueOf(value)));
    }
}