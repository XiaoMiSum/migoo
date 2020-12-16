/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import components.xyz.migoo.readers.ReaderFactory;
import components.xyz.migoo.readers.ReaderException;
import components.xyz.migoo.reports.Report;
import core.xyz.migoo.*;
import core.xyz.migoo.report.IReport;
import core.xyz.migoo.utils.DateUtil;
import core.xyz.migoo.utils.StringUtil;

import java.io.File;

/**
 * @author xiaomi
 * @date 2018/7/24 14:24
 */
public class Migoo {

    public static void main(String[] args) {
        String file = StringUtil.isEmpty(args[0]) ? "./case/migoo-default.yml" : args[0];
        new Migoo().run(file);
    }

    public void run(String file) {
        try {
            JSONObject data = this.processor(file);
            TestSuite suite = new TestSuite(data);
            IResult result = suite.run();
            this.report(suite.getReportConfig(), suite.getEmailConfig(), result);
        } catch (Exception e) {
            Report.log("run error", e);
        }
    }

    public JSONObject processor(String file) throws ReaderException {
        // 1. 解析文件 获取解析到的 json 对象
        JSONObject data = this.importFileData(file);
        JSONArray sets = new JSONArray();
        for (Object testSet : data.getJSONArray("sets")) {
            if (testSet instanceof String) {
                JSONObject set = this.readFileToObject((String) testSet);
                set.put("cases",  this.processor(set.get("cases")));
                sets.add(set);
            }
        }
        data.put("sets", sets);
        return data;
    }

    private JSONObject importFileData(String file) throws ReaderException {
        JSONObject data = this.readFileToObject(file);
        // 判断是否为导入文件
        if (data.get("config") instanceof String) {
            data.put("config", this.readFileToObject(data.getString("dataset")));
        }
        if (data.get("plugins") instanceof String) {
            data.put("plugins", this.readFileToObject(data.getString("plugins")));
        }
        if (data.get("dataset") instanceof String) {
            data.put("dataset", this.readFileToObject(data.getString("dataset")));
        }
        return data;
    }

    private JSONArray processor(Object caseObj) throws ReaderException {
        JSONArray cases = (caseObj instanceof String) ? this.readFileToArray((String) caseObj) : (JSONArray) caseObj;
        for (int i = 0; i < cases.size(); i++) {
            JSONObject testCase = cases.getJSONObject(i);
            Object steps = testCase.get("steps");
            if (steps instanceof String) {
                cases.getJSONObject(i).put("steps", this.readFileToObject((String) steps));
            }
            cases.remove(i);
            cases.add(i, testCase);
        }
        return cases;
    }

    private JSONObject readFileToObject(String file) throws ReaderException {
        return (JSONObject) ReaderFactory.getReader(new File(file)).read();
    }

    private JSONArray readFileToArray(String file) throws ReaderException {
        return (JSONArray) ReaderFactory.getReader(new File(file)).read();
    }

    private void report(JSONObject reportConfig, JSONObject emailConfig, IResult result) {
        reportConfig = reportConfig == null ? new JSONObject() : reportConfig;
        String output = !StringUtil.isEmpty(reportConfig.getString("output")) ?
                reportConfig.getString("output") : "./out-put/" + DateUtil.TODAY_DATE;
        reportConfig.put("output", output);
        String listener = !StringUtil.isEmpty(reportConfig.getString("listener")) ?
                reportConfig.getString("listener") : "components.migoo.xyz.reports.Report";
        IReport report;
        try {
            report = (IReport) Class.forName(listener).newInstance();
        } catch (Exception e) {
            report = new Report();
        }
        report.generateReport(reportConfig, result);
        report.sendReport(emailConfig, result);
    }
}
