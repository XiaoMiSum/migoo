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
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.gherkin.model.Feature;
import com.aventstack.extentreports.gherkin.model.Scenario;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import core.xyz.migoo.report.Reporter;
import core.xyz.migoo.report.Result;
import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.variable.MiGooVariables;
import org.apache.commons.lang3.StringUtils;
import protocol.xyz.migoo.http.sampler.HTTPSampleResult;
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

    private final ExtentReports extent;
    private ReportLevel reportLevel;

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
        reportLevel = ReportLevel.of(getReportLevel(result, 4));
        writeResult(extent, result, 1);
        flush(result);
    }

    private void writeResult(Object feature, Result result, int level) {
        if (result instanceof SampleResult sr) {
            // 当前测试报告是取样器级别 或 测试用例节点，则设置所有第一节点为全局
            var isGlobal = reportLevel.isSampler() || reportLevel.isTestcase();
            writeVariables(feature, result.getVariables(), isGlobal, true);
            writeConfigElements(feature, result.getConfigElementResults(), isGlobal, true);
            writeProcessors(feature, result.getPreprocessorResults(), "前置", isGlobal, true);
            if (Objects.nonNull(sr.getTestClass())) {
                writeSampler(feature, sr, true, isGlobal);
            }
            writeValidators(feature, sr);
            writeThrowable(feature, result);
            writeExtractors(feature, sr.getExtractorResults(), true);
            writeProcessors(feature, result.getPostprocessorResults(), "后置", isGlobal, true);
            return;
        }
        if (result.hasConfigurer()) {
            var isGlobal = level == 1 && (reportLevel.isSuite() || reportLevel.isSet() || reportLevel.isTestcase());
            var isLocality = level == 2 && (reportLevel.isSuite() || reportLevel.isSet());
            var name = isGlobal ? "全局配置" : "局部配置";
            var node = isGlobal || isLocality ? write(feature, name) : (ExtentTest) feature;
            writeVariables(node, result.getVariables(), isGlobal, false);
            writeConfigElements(node, result.getConfigElementResults(), isGlobal, false);
            writeProcessors(node, result.getPreprocessorResults(), "前置", isGlobal, false);
            writeProcessors(node, result.getPostprocessorResults(), "后置", isGlobal, false);
            node.getModel().setStatus(INFO);
        }
        writeThrowable(feature, result);
        if (Objects.nonNull(result.getSubResults()) && !result.getSubResults().isEmpty()) {
            for (var r : result.getSubResults()) {
                var node = write(feature, r.getTitle());
                writeResult(node, r, level + 1);
                if (reportLevel.isSuite() || reportLevel.isSet()) {
                    node.getModel().setStatus(r.isSuccessful() ? PASS : FAIL);
                }
            }
        }
    }

    private void writeVariables(Object feature, MiGooVariables variables, boolean isGlobal, boolean isSampler) {
        if (Objects.nonNull(variables) && !variables.getProperty().isEmpty()) {
            // test 为 1级节点 或者 2级节点
            if (reportLevel.isSuite() && isSampler) {
                write(feature, "变量定义", true);
                write(feature, variables.getProperty().toString(), true);
            } else {
                var test = write(feature, "变量定义", INFO);
                var node2 = reportLevel.isSampler() ? write(test, "变量定义", INFO) : test;
                // 如果是全局配置 需要创建一层占位符作为变量内容的容器
                var node = isGlobal ? write(node2, "-") : test;
                write(node, variables.getProperty().toString(), true);
                node2.getModel().setStatus(INFO);
                if (reportLevel.isSampler()) {
                    test.getModel().setStatus(INFO);
                }
            }
        }
    }

    private void writeConfigElements(Object feature, List<SampleResult> results, boolean isGlobal, boolean isSampler) {
        if (Objects.nonNull(results) && !results.isEmpty()) {
            var node = write(feature, "配置元件", reportLevel.isSuite() && isSampler);
            if (reportLevel.isSampler()) {
                // 测试报告级别为取样器，遍历打印配置元件类型 和 配置元件内容
                results.forEach(item -> {
                    // 给每一个配置元件增加一层节点展示TestClass 加一层占位符作为配置元件内容的容器
                    var node2 = write(write(node, item.getTestClass(), INFO), "-", INFO);
                    write(node2, item.getSamplerData(), true, INFO);
                    writeThrowable(node2, item);
                });
            } else if (reportLevel.isTestcase()) {
                var node2 = isGlobal ? write(node, "-") : node;
                results.forEach(item -> {
                    write(node2, item.getSamplerData(), true);
                    writeThrowable(node2, item);
                });
                if (!isGlobal) {
                    node2.getModel().setStatus(INFO);
                }
            } else {
                results.forEach(item -> write(isGlobal ? write(node, "-") : node, item.getSamplerData(), true));
            }
            node.getModel().setStatus(INFO);
        }
    }

    private void writeProcessors(Object feature, List<SampleResult> results, String prefix, boolean isGlobal, boolean isSampler) {
        if (Objects.nonNull(results) && !results.isEmpty()) {
            var node1 = write(feature, prefix + "处理器", reportLevel.isSuite() && isSampler);
            // 测试报告级别为取样器，遍历打印处理器类型 和 处理器元件内容
            results.forEach(item -> {
                var node2 = writeSampler(node1, item, false, isGlobal);
                writeThrowable(node2, item);
                // 提取器挂到Node2 下面
                writeExtractors(node2, item.getExtractorResults(), false);
                if (reportLevel.isSampler()) {
                    node2.getModel().setStatus(INFO);
                }
            });
        }
    }

    private void writeExtractors(Object feature, List<SampleResult> results, boolean isSampler) {
        if (Objects.nonNull(results) && !results.isEmpty()) {
            if (reportLevel.isSampler()) {
                // 测试报告级别为 Sampler，则再创建一个Node用作打印详情的容器
                var node = write(feature, "提取器" + (isSampler ? "" : "："));
                results.forEach(result -> {
                    if (!isSampler) {
                        write(node, result.getTestClass(), true);
                        write(node, result.getSamplerData(), true);
                    } else {
                        // 给每一个提取器增加一层节点展示TestClass
                        var node2 = write(node, result.getTestClass());
                        // 加一层占位符作为配置元件内容的容器
                        write(write(node2, "-"), result.getSamplerData(), true);
                        node2.getModel().setStatus(INFO);
                        node.getModel().setStatus(INFO);
                    }
                });
            } else if (reportLevel.isTestcase()) {
                var node = write(feature, "提取器" + (isSampler ? "" : "："), !isSampler);
                var json = new JSONArray();
                results.forEach(item -> json.add(JSON.parseObject(item.getSamplerData())));
                // 如果是取样器提取，则创建一个占位符Node用作打印详情的容器
                write(!isSampler ? node : write(node, " - "), json.toJSONString(), true);
                if (isSampler) {
                    node.getModel().setStatus(INFO);
                }
            } else {
                var json = new JSONArray();
                results.forEach(item -> json.add(JSON.parseObject(item.getSamplerData())));
                write(write(feature, "提取器：", reportLevel.isSuite() || !isSampler), json.toJSONString(), true);
            }
        }
    }

    private void writeThrowable(Object feature, Result result) {
        if (Objects.nonNull(result.getThrowable())) {
            var f = feature instanceof ExtentReports r ? write(r, result.getTitle(), FAIL) : (ExtentTest) feature;
            f.fail(result.getThrowable());
        }
    }

    private ExtentTest writeSampler(Object feature, SampleResult result, boolean isSampler, boolean isGlobal) {
        var f = feature instanceof ExtentReports r ? write(r, result.getTitle(), INFO) : feature;
        // 如果 测试报告级别为 Suite 且 isGlobal == true , 则 f为 2级节点
        // 如果 测试报告级别为 Set 且 (isGlobal == true 或  isSampler == true) , 则 f为 2级节点
        // 如果 测试报告级别为 Testcase 或 Sampler 时, 则 f 为2级节点
        var isNodeLevel2 = (reportLevel.isSuite() && isGlobal) || (reportLevel.isSet() && (isSampler || isGlobal)) ||
                (reportLevel.isSampler() || reportLevel.isTestcase());
        // 当 f = 2级节点时，需要创建三级节点
        var status = result.isSuccessful() ? PASS : FAIL;
        var node = write(f, (isSampler ? "取样器：" : "处理器：") + result.getTestClass(), !isNodeLevel2, INFO);
        var isNodeLevel3 = !(reportLevel.isTestcase() || reportLevel.isSampler()) || (reportLevel.isTestcase() && !isSampler);
        if (StringUtils.isNotBlank(result.getUrl())) {
            write(node, "请求地址：" + result.getUrl(), isNodeLevel3, status);
        }
        if (result instanceof HTTPSampleResult h) {
            if (Objects.nonNull(h.getMethod()) && !h.getMethod().isEmpty()) {
                write(node, "请求方法：" + h.getMethod(), isNodeLevel3, status);
            }
            if (Objects.nonNull(h.getCookies()) && !h.getCookies().isEmpty()) {
                write(write(node, "Cookies：", isNodeLevel3), toJSONString(h.getCookies()), true, status);
            }
            if (Objects.nonNull(h.getRequestHeaders()) && !h.getRequestHeaders().isEmpty()) {
                write(write(node, "请求头：", isNodeLevel3), toJSONString(h.getRequestHeaders()), true, status);
            }
            if (StringUtils.isNotBlank(h.getQueryString())) {
                write(write(node, "Query 参数：", isNodeLevel3), h.getQueryString(), true, status);
            }
        }
        if (StringUtils.isNotBlank(result.getSamplerData())) {
            write(write(node, "请求数据：", isNodeLevel3), result.getSamplerData(), true, status);
        }
        if (result instanceof HTTPSampleResult h) {
            if (Objects.nonNull(h.getResponseHeaders()) && !h.getResponseHeaders().isEmpty()) {
                write(write(node, "响应头：", isNodeLevel3), toJSONString(h.getResponseHeaders()), true, status);
            }
        }
        if (StringUtils.isNotBlank(result.getResponseDataAsString())) {
            write(write(node, "响应数据：", isNodeLevel3), result.getResponseDataAsString(), true, status);
        }
        if (reportLevel.isSampler()) {
            node.getModel().setStatus(INFO);
        }
        if (reportLevel.isTestcase() || reportLevel.isSampler()) {
            ((ExtentTest) f).getModel().setStatus(result.isSuccessful() ? PASS : FAIL);
        }
        return node;
    }

    private void writeValidators(Object feature, SampleResult result) {
        var results = result.getAssertionResults();
        if (Objects.nonNull(results) && !results.isEmpty()) {
            if (reportLevel.isSampler() || reportLevel.isTestcase()) {
                // 先以Node打印验证器类型，如果 测试报告级别为 测试报告，则直接以此Node用作打印详情的容器
                // 如果测试报告级别为 取样器，则再创建一个Node用作打印详情的容器
                var node = write(feature, "验证器");
                results.forEach(item -> {
                    var n = write(node, item.getName(), item.isSuccessful() ? PASS : FAIL);
                    write(reportLevel.isSampler() ? write(n, "-") : n, item.getContent(), true, item.isSuccessful() ? PASS : FAIL);
                });
                node.getModel().setStatus(result.isSuccessful() ? PASS : FAIL);
                return;
            }
            // 如果测试报告级别为 Suite，则以 feature 作为打印详情的容器(write 返回 原feature)，否则创建一个新的Node作为打印详情的容器
            var node = write(feature, "验证器：", reportLevel.isSuite());
            results.forEach(item -> write(node, item.getContent(), true, item.isSuccessful() ? PASS : FAIL));
        }
    }

    private int getReportLevel(Result result, int level) {
        if (result instanceof SampleResult) {
            return level;
        }
        level--;
        if (result.getSubResults() != null && !result.getSubResults().isEmpty()) {
            var sr = result.getSubResults().getFirst();
            level = sr instanceof SampleResult ? level : getReportLevel(sr, level);
        }
        return level;
    }

    private ExtentTest write(Object feature, String record, Status... status) {
        return write(feature, record, false, status);
    }

    private ExtentTest write(Object feature, String record, boolean isLog, Status... status) {
        ExtentTest node;
        if (feature instanceof ExtentReports report) {
            node = report.createTest(Feature.class, record);
        } else {
            var t = (ExtentTest) feature;
            if (isLog) {
                var s = Objects.nonNull(status) && status.length > 0 ? status[0] : PASS;
                return JSON.isValid(record) ? t.log(s, MarkupHelper.createJsonCodeBlock(JSON.parse(record))) :
                        isXml(record) ? t.log(s, MarkupHelper.createCodeBlock(record, CodeLanguage.XML)) : t.log(s, record);
            }
            node = t.createNode(Scenario.class, record);
            if (Objects.nonNull(status) && status.length > 0) {
                t.getModel().setStatus(status[0]);
            }
        }
        node.getModel().setStatus(Objects.nonNull(status) && status.length > 0 ? status[0] : PASS);
        return node;
    }

    private boolean isXml(String text) {
        text = text.trim();
        return text.startsWith("<") && text.endsWith(">");
    }

    private String toJSONString(JSONArray objects) {
        var strings = new JSONArray(objects.size());
        for (var i = 0; i < objects.size(); i++) {
            var item = objects.getJSONObject(i).firstEntry();
            strings.add(item.getKey() + ": " + item.getValue());
        }
        return strings.toJSONString();
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