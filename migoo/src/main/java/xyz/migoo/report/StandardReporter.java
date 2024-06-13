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

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import core.xyz.migoo.assertion.VerifyResult;
import core.xyz.migoo.report.Reporter;
import core.xyz.migoo.report.Result;
import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.variable.MiGooVariables;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.translate.CharSequenceTranslator;
import protocol.xyz.migoo.http.sampler.HTTPSampleResult;
import xyz.migoo.report.util.DateUtils;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.PASS;
import static com.aventstack.extentreports.markuputils.ExtentColor.TRANSPARENT;
import static com.aventstack.extentreports.markuputils.ExtentColor.WHITE;
import static xyz.migoo.Constants.*;

/**
 * @author xiaomi
 */
public class StandardReporter implements Reporter {

    private final ExtentReports extent;

    public StandardReporter() {
        extent = new ExtentReports();
        extent.setSystemInfo("os.name", System.getProperty("os.name"));
        extent.setSystemInfo("java.runtime.name", System.getProperty("java.runtime.name"));
        extent.setSystemInfo("java.version", System.getProperty("java.version"));
        extent.setSystemInfo("java.vm.name", System.getProperty("java.vm.name"));
        extent.setSystemInfo("migoo.version", System.getProperty("migoo.version"));
    }

    @Override
    public void generateReport(Result result) {
        prepare(result);
        writeResult(extent, result);
        ended(result);
    }

    private void writeResult(Object node, Result result) {
        writeGlobals(node, result.getVariables());
        writeConfigs(node, result.getConfigElementResults());
        writeProcessors(node, result.getPreprocessorResults(), "PRE.PROCESSORS");
        if (result instanceof SampleResult sResult) {
            var sNode = node instanceof ExtentTest ? ((ExtentTest) node).createNode(result.getTitle())
                    : extent.createTest(result.getTitle());
            writeSampleResult(sNode, sResult);
        } else {
            if (result.isException()) {
                var sNode = node instanceof ExtentTest ? ((ExtentTest) node).createNode(result.getTitle())
                        : extent.createTest(result.getTitle());
                sNode.getModel().setStartTime(DateUtils.toDate(result.getStartTime()));
                writeThrowable(sNode, result.getThrowable());
                sNode.getModel().setEndTime(DateUtils.toDate(result.getEndTime()));
            } else {
                if (result.getSubResults() != null) {
                    for (Result sr : result.getSubResults()) {
                        var sNode = node instanceof ExtentTest ? ((ExtentTest) node).createNode(sr.getTitle())
                                : extent.createTest(sr.getTitle());
                        if (sr instanceof SampleResult sResult) {
                            writeSampleResult(sNode, sResult);
                        } else {
                            writeResult(sNode, sr);
                        }
                    }
                }
            }
        }
        writeProcessors(node, result.getPostprocessorResults(), "POST.PROCESSORS");
    }

    private void writeGlobals(Object f, MiGooVariables variables) {
        if (Objects.nonNull(variables) && !variables.getProperty().isEmpty()) {
            var markup = MarkupHelper.createCodeBlock(variables.toString(), CodeLanguage.JSON);
            if (f instanceof ExtentReports reports) {
                reports.createTest("GLOBAL.VARIABLES").info(markup);
            }
            if (f instanceof ExtentTest node) {
                node.createNode("VARIABLES").info(markup);
            }
        }
    }

    private void writeConfigs(Object f, List<SampleResult> results) {
        if (Objects.nonNull(results) && !results.isEmpty()) {
            var feature = f instanceof ExtentTest t ? t.createNode("CONFIG.ELEMENTS")
                    : extent.createTest("CONFIG.ELEMENTS");
            results.forEach(result -> feature.createNode(result.getTestClass())
                    .info(MarkupHelper.createCodeBlock(result.getSamplerData(), CodeLanguage.JSON)));
        }
    }

    private void writeProcessors(Object f, List<SampleResult> results, String name) {
        if (Objects.nonNull(results) && !results.isEmpty()) {
            var feature = f instanceof ExtentTest t ? t.createNode(name) : extent.createTest(name);
            results.forEach(result -> {
                var title = StringUtils.isEmpty(result.getTitle()) ? result.getTestClass() : result.getTitle();
                var node = feature.createNode(title);
                buildSampleResult(result, node);
                writeThrowable(node, result.getThrowable());
                writeExtractorResults(node, result.getExtractorResults());
            });
        }
    }

    private void writeSampleResult(ExtentTest node, SampleResult result) {
        node.getModel().setStartTime(DateUtils.toDate(result.getStartTime()));
        writeProcessors(node, result.getPreprocessorResults(), "PRE.PROCESSORS");
        buildSampleResult(result, node.createNode("TEST.CLASS: " + result.getTestClass()));
        writeExtractorResults(node, result.getExtractorResults());
        writeThrowable(node, result.getThrowable());
        node.getModel().setStatus(result.isSuccessful() ? PASS : FAIL);
        if (result.getAssertionResults() != null) {
            var v = node.createNode("TEST.VALIDATORS");
            for (var subResult : result.getAssertionResults()) {
                v.log(subResult.isSuccessful() ? PASS : FAIL, convertValidator(subResult));
            }
        }
        writeProcessors(node, result.getPostprocessorResults(), "POST.PROCESSORS");
        node.getModel().setEndTime(DateUtils.toDate(result.getEndTime()));
    }

    private void buildSampleResult(Result result, ExtentTest node) {
        if (result instanceof SampleResult sResult) {
            if (sResult instanceof HTTPSampleResult hResult) {
                node.info(MarkupHelper.createLabel("REQUEST.URL:  " + hResult.getMethod() + " " + sResult.getUrl(), TRANSPARENT));
                if (!hResult.getCookies().isEmpty()) {
                    node.info(MarkupHelper.createLabel("REQUEST.COOKIES", WHITE))
                            .info(MarkupHelper.createCodeBlock(hResult.getCookies()));
                }
                if (Objects.nonNull(hResult.getRequestHeaders()) && !hResult.getRequestHeaders().isEmpty()) {
                    var array = convertHeaders(hResult.getRequestHeaders());
                    node.createNode("REQUEST.HEADERS")
                            .info(MarkupHelper.createCodeBlock(array.toJSONString(), CodeLanguage.JSON));
                }
                if (!hResult.getQueryString().isEmpty()) {
                    node.info(MarkupHelper.createLabel("REQUEST.QUERY", WHITE))
                            .info(MarkupHelper.createCodeBlock(hResult.getQueryString(), CodeLanguage.JSON));
                }
            } else {
                node.info(MarkupHelper.createCodeBlock(sResult.getUrl()));
            }
            if (!sResult.getSamplerData().isEmpty()) {
                node.createNode("REQUEST.DATA")
                        .info(MarkupHelper.createCodeBlock(sResult.getSamplerData(), CodeLanguage.JSON));
            }
            if (sResult instanceof HTTPSampleResult hResult) {
                if (Objects.nonNull(hResult.getResponseHeaders()) && !hResult.getResponseHeaders().isEmpty()) {
                    var array = convertHeaders(hResult.getResponseHeaders());
                    node.createNode("RESPONSE.HEADERS")
                            .info(MarkupHelper.createCodeBlock(array.toJSONString(), CodeLanguage.JSON));
                }
            }
            if (!sResult.getResponseDataAsString().isEmpty()) {
                node.createNode("RESPONSE.DATA").info(JSON.isValid(sResult.getResponseDataAsString()) ?
                        MarkupHelper.createCodeBlock(sResult.getResponseDataAsString(), CodeLanguage.JSON)
                        : MarkupHelper.createCodeBlock(getResponseString(sResult.getResponseDataAsString().trim())));

            }
        }
    }

    private void writeExtractorResults(ExtentTest node, List<SampleResult> results) {
        if (Objects.nonNull(results) && !results.isEmpty()) {
            var ex = node.createNode("EXTRACTORS");
            results.forEach(result -> ex.info(MarkupHelper.createCodeBlock(result.getSamplerData(), CodeLanguage.JSON)));
        }

    }

    private JSONArray convertHeaders(List<Map<String, String>> headers) {
        var array = new JSONArray(headers.size());
        for (var requestHeader : headers) {
            for (var entry : requestHeader.entrySet()) {
                array.add(entry.getKey() + ": " + entry.getValue());
            }
        }
        return array;
    }

    private Markup convertValidator(VerifyResult result) {
        var array = new JSONArray();
        if (result.isSuccessful() && JSON.isValidObject(result.getContext())) {
            var object = JSONObject.parseObject(result.getContext());
            object.forEach((key, value) -> array.add(key + ": " + value));
        } else {
            array.add("testclass: " + result.getName());
            array.add(result.getContext());
        }
        return MarkupHelper.createCodeBlock(array.toJSONString(), CodeLanguage.JSON);
    }

    private void writeThrowable(ExtentTest feature, Throwable content) {
        if (Objects.nonNull(content)) {
            feature.fail(content);
        }
    }

    private String getResponseString(String text) {
        if (text.startsWith("<") && text.endsWith(">")) {
            return CharSequenceTranslator.escapeHtml4(text);
        }
        return text;
    }

    private void prepare(Result result) {
        var outputDirectoryName = System.getProperty(REPORT_OUTPUT, "./out-put") + "/"
                + DateUtils.nowStr() + "/" + result.getTitle();
        var reporter = new ExtentSparkReporter(outputDirectoryName + "/index.html");
        reporter.config().setDocumentTitle(result.getTitle() + " Report - Generated by migoo");
        reporter.config().setReportName(result.getTitle() + " Reports</span></a></li>\n" +
                "<li><a href='https://github.com/XiaoMiSum/migoo' target=\"_blank\"><span>" +
                "<img src=\"https://img.shields.io/badge/migoo-xiaomi-yellow.svg?style=social&amp;logo=github\">");
        reporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
        reporter.config().setTheme(Theme.valueOf(System.getProperty(REPORT_THEME, "DARK").toUpperCase(Locale.ROOT)));
        reporter.config().enableOfflineMode(Boolean.parseBoolean(System.getProperty(REPORT_OFFLINE, "true")));
        reporter.config().setTimelineEnabled(Boolean.parseBoolean(System.getProperty(TIMELINE_ENABLED, "false")));
        reporter.config().setEncoding("UTF-8");
        extent.attachReporter(reporter);
        extent.getReport().setStartTime(DateUtils.toDate(result.getStartTime()));
    }

    private void ended(Result result) {
        extent.getReport().setEndTime(DateUtils.toDate(result.getEndTime()));
        extent.flush();
    }
}