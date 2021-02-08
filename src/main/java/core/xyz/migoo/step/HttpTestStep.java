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
import components.xyz.migoo.reports.Report;
import core.xyz.migoo.functions.FunctionException;
import core.xyz.migoo.http.MiGooRequest;
import core.xyz.migoo.vars.Vars;
import core.xyz.migoo.vars.VarsHelper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xyz.migoo.simplehttp.Response;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiaomi
 * @date 2020/11/6 20:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JSONType(orders = {"url", "method", "headers", "query", "data", "body", "extractor"})
public class HttpTestStep extends TestStep {

    private static final Pattern URL_REGEX = Pattern.compile("^(http|https)://([\\w\\-_][.\\w\\-_]*)*[:]?([0-9]+)?(([/\\w\\-_]*)*)?");

    private String url;

    private String method;

    private JSONObject headers;

    private JSONObject query;

    private JSONObject data;

    private JSONObject body;

    @JSONField(serialize = false)
    private MiGooRequest request;

    @JSONField(serialize = false)
    private Response response;


    @Override
    public void execute(Vars vars, JSONObject config) throws Exception {
        this.convertRequestVariable(vars);
        this.buildRequest(config);
        response = request.execute();
        this.extractToVars(vars, response.text());
        this.printRequestLog();
        if (getTimer() > 0) {
            this.wait(getTimer() * 1000);
        }
    }


    private void convertRequestVariable(Vars vars) throws FunctionException {
        vars.put("migoo.request.query", query);
        vars.put("migoo.request.data", data);
        vars.put("migoo.request.body", body);
        url = (String) VarsHelper.convertVariables(url, vars);
        VarsHelper.convertVariables(headers, vars);
        VarsHelper.convertVariables(query, vars);
        VarsHelper.convertVariables(data, vars);
        VarsHelper.convertVariables(body, vars);
    }

    private void buildRequest(JSONObject config) {
        JSONObject headers = new JSONObject();
        headers.putAll(config.get("headers") != null ? config.getJSONObject("headers") : new JSONObject());
        headers.putAll(this.headers != null ? this.headers : new JSONObject());
        MiGooRequest.Builder builder = new MiGooRequest.Builder()
                .method(method).query(query).data(data).body(body).headers(headers);
        Matcher matcher = URL_REGEX.matcher(url);
        request = matcher.find() ? builder.port(matcher.group(3) == null ? null : Integer.parseInt(matcher.group(3)))
                .protocol(matcher.group(1)).host(matcher.group(2)).api(matcher.group(4)).build()
                : builder.host(config.getString("host")).protocol(config.getString("protocol"))
                .port(config.getInteger("port")).api(url).build();
    }
    protected void printRequestLog() {
        Report.log("execute step: {}", getDesc() == null ? "HttpTestStep" : getDesc());
        request.printRequestLog();
        Report.log("extract vars: {}", JSON.toJSONString(getExtractor()));
    }

    @Override
    @JSONField(serialize = false)
    public String toString() {
        return JSON.toJSONString(this);
    }

}
