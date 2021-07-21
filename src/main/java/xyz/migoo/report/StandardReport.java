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

package xyz.migoo.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import core.xyz.migoo.assertions.AssertionResult;
import core.xyz.migoo.report.Report;
import core.xyz.migoo.report.Result;
import core.xyz.migoo.samplers.SampleResult;
import core.xyz.migoo.testelement.AbstractTestElement;
import protocol.xyz.migoo.http.sampler.HTTPSampleResult;
import org.apache.commons.text.translate.CharSequenceTranslator;
import xyz.migoo.report.util.DateUtils;

import java.util.List;

import static xyz.migoo.MiGoo.SYSTEM;

/**
 * @author xiaomi
 */
public class StandardReport extends AbstractTestElement implements Report {

    @Override
    public void generateReport(Result result) {
        ExtentReports extent = new ExtentReports();
        testStarted(result, extent);
        if (result instanceof SampleResult) {
            generateExtentReport((SampleResult) result, extent.createTest(result.getTitle()), -1);
        } else {
            if (result.isException()) {
                ExtentTest feature = extent.createTest(result.getTitle());
                feature.getModel().setStartTime(DateUtils.toDate(result.getStartTime()));
                this.write(feature, result.getThrowable());
                feature.getModel().setEndTime(DateUtils.toDate(result.getEndTime()));
            } else {
                if (result.getSubResults() != null) {
                    for (int i = 0; i < result.getSubResults().size(); i++) {
                        Result subResult = result.getSubResults().get(i);
                        generateExtentReport(subResult, extent.createTest(subResult.getTitle()), 1);
                    }
                }
            }
        }
        extent.flush();
    }

    private void generateExtentReport(Result result, ExtentTest feature, int level) {
        feature.getModel().setStartTime(DateUtils.toDate(result.getStartTime()));
        if (result.isException()) {
            this.write(feature, result.getThrowable());
        } else {
            this.write(feature, result, level).write(result.getPreprocessorResults(), feature, "Preprocessor")
                    .write(result.getPostprocessorResults(), feature, "Postprocessor")
                    .write(feature, result.isSuccessful() ? Status.PASS : Status.FAIL);
            if (result.getSubResults() != null) {
                for (int i = 0; i < result.getSubResults().size(); i++) {
                    Result subResult = result.getSubResults().get(i);
                    if (subResult instanceof SampleResult) {
                        generateExtentReport((SampleResult) subResult, feature, i);
                    } else {
                        generateExtentReport(subResult, feature.createNode(subResult.getTitle()), level + 1);
                    }
                }
            }
        }
        feature.getModel().setEndTime(DateUtils.toDate(result.getEndTime()));
    }

    private void generateExtentReport(SampleResult result, ExtentTest feature, int index) {
        feature.getModel().setStartTime(DateUtils.toDate(result.getStartTime()));
        if (index > -1) {
            this.write(feature, MarkupHelper.createLabel(String.format("Step-%s: %s", index, result.getTitle()), ExtentColor.WHITE));
        }
        this.write(result.getPreprocessorResults(), feature, "Preprocessor")
                .write(result.getPostprocessorResults(), feature, "Postprocessor")
                .write(feature, MarkupHelper.createLabel(String.format("Sampler: %s", result.getTestClass()), ExtentColor.WHITE))
                .write(feature, result.getUrl())
                .write(feature, 0, result)
                .write(feature, 1, result)
                .write(feature, -1, result)
                .write(feature, result.getSamplerData())
                .write(feature, getResponseString(result.getResponseDataAsString().trim()))
                .write(feature, result.getThrowable())
                .write(feature, result.isSuccessful() ? Status.PASS : Status.FAIL);
        if (result.getAssertionResults() != null) {
            for (AssertionResult subResult : result.getAssertionResults()) {
                feature.log(subResult.isSuccessful() ? Status.PASS : Status.FAIL, subResult.getContext());
            }
        }
        feature.getModel().setEndTime(DateUtils.toDate(result.getEndTime()));
    }

    private StandardReport write(List<SampleResult> results, ExtentTest feature, String desc) {
        if (results != null && results.size() > 0) {
            for (int i = 0; i < results.size(); i++) {
                SampleResult result = results.get(i);
                String title = result.getTitle() == null ? result.getTestClass() : result.getTitle();
                this.write(feature, MarkupHelper.createLabel(String.format("%s-%s: %s", desc, i, title), ExtentColor.WHITE))
                        .write(feature, result.getUrl())
                        .write(feature, 0, result)
                        .write(feature, 1, result)
                        .write(feature, -1, result)
                        .write(feature, result.getSamplerData())
                        .write(feature, getResponseString(result.getResponseDataAsString().trim()))
                        .write(feature, result.getThrowable());
                for (Result subResult : result.getSubResults()) {
                    this.write(feature, subResult.getTitle() + " - " + ((SampleResult) subResult).getSamplerData());
                }
            }
        }
        return this;
    }

    private StandardReport write(ExtentTest feature, Object content) {
        if (content instanceof String && !((String) content).isEmpty()) {
            feature.info((String) content);
        } else if (content instanceof Throwable) {
            feature.fail((Throwable) content);
        } else if (content instanceof Markup) {
            feature.info((Markup) content);
        } else if (content instanceof Status) {
            feature.getModel().setStatus((Status) content);
        }
        return this;
    }

    private StandardReport write(ExtentTest feature, int count, Result result) {
        if (count == 0 && result instanceof HTTPSampleResult) {
            this.write(feature, ((HTTPSampleResult) result).getCookies());
        } else if (count == 1 && result instanceof HTTPSampleResult) {
            this.write(feature, ((HTTPSampleResult) result).getRequestHeaders());
        } else if (count == -1 && result instanceof HTTPSampleResult) {
            this.write(feature, ((HTTPSampleResult) result).getQueryString());
        }
        return this;
    }

    private StandardReport write(ExtentTest feature, Result result, int level) {
        if (level == 2 && result.getVariables().getProperty().size() > 0) {
            this.write(feature, MarkupHelper.createLabel("Testcase global variables", ExtentColor.WHITE))
                    .write(feature, result.getVariables().getProperty().toString());
        }
        return this;
    }

    private String getResponseString(String text) {
        if (text.startsWith("<") && text.endsWith(">")) {
            return CharSequenceTranslator.escapeHtml4(text);
        }
        return text;
    }

    private void testStarted(Result result, ExtentReports extent) {
        Object outputDirectoryName = get(OUTPUT_DIRECTORY_NAME, "./out-put");
        Object title = get(TITLE, "migoo");
        ExtentSparkReporter reporter = new ExtentSparkReporter(outputDirectoryName + "/index.html");
        reporter.config().setDocumentTitle(title + " Report - Generated by migoo");
        reporter.config().setReportName(title + " Reports</span></a></li>\n" +
                "<li><a href='https://github.com/XiaoMiSum/migoo' target=\"_blank\"><span>" +
                "<img src=\"https://img.shields.io/badge/migoo-xiaomi-yellow.svg?style=social&amp;logo=github\">");
        reporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
        reporter.config().setTheme(Theme.DARK);
        reporter.config().enableOfflineMode(getPropertyAsBoolean(ENABLE_OFFLINE_MODE));
        reporter.config().setTimelineEnabled(false);
        reporter.config().setEncoding("UTF-8");

        extent.attachReporter(reporter);
        extent.getReport().setStartTime(DateUtils.toDate(result.getStartTime()));
        extent.getReport().setEndTime(DateUtils.toDate(result.getEndTime()));
        SYSTEM.forEach((key, value) -> extent.setSystemInfo(key, String.valueOf(value)));
    }
}