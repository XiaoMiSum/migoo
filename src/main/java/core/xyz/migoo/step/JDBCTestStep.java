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

package core.xyz.migoo.step;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import components.xyz.migoo.plugins.jdbc.DataSource;
import components.xyz.migoo.reports.Report;
import core.xyz.migoo.vars.Vars;
import core.xyz.migoo.vars.VarsHelper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author xiaomi
 * @date 2020/11/6 20:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JSONType(orders = {"datasource", "statement", "extractor"})
public class JDBCTestStep extends TestStep {

    private String datasource;

    private String statement;

    private Integer type;

    @Override
    public void execute(Vars vars, JSONObject config) throws Exception {
        DataSource dataSource = (DataSource) vars.getPlugin(datasource);
        VarsHelper.convertVariables(statement, vars);
        if (type == null || type == 1) {
            List<JSONObject> list = dataSource.executeQuery(statement);
            if (list.size() == 1) {
                this.extractToVars(vars, list.get(0).toString());
            } else {
                this.extractToVars(vars, list.toString());
            }
        } else {
            dataSource.execute(statement);
        }
        this.printRequestLog();
    }

    protected void printRequestLog() {
        Report.log("execute step: {}", getDesc() == null ? "JDBCTestStep" : getDesc());
        Report.log("sql statement " + statement);
        Report.log("extract vars: {}", JSON.toJSONString(getExtractor()));
    }

    @Override
    @JSONField(serialize = false)
    public String toString() {
        return JSON.toJSONString(this);
    }

}
