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

package core.xyz.migoo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.annotation.JSONField;
import components.xyz.migoo.reports.Report;
import core.xyz.migoo.functions.FunctionException;
import core.xyz.migoo.http.MiGooRequest;
import core.xyz.migoo.vars.Vars;
import core.xyz.migoo.vars.VarsException;
import core.xyz.migoo.vars.VarsHelper;
import lombok.Data;
import xyz.migoo.simplehttp.Response;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiaomi
 * @date 2020/11/6 20:49
 */
@Data
public class TestStep {

    private static Pattern pattern = Pattern.compile("^(http[s]?)+://([0-9a-zA-Z][.\\w]*[0-9a-zA-Z])*[:]?([0-9]+)?((/\\w*)*)?");

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

    private JSONObject extract;

    public void execute(Vars vars, JSONObject config) throws Exception {
        this.bindRequestVariable(vars);
        this.buildRequest(config);
        response = request.execute();
        this.extractToVars(vars);
        this.printRequestLog();
    }

    private void bindRequestVariable(Vars vars) throws FunctionException {
        if (query != null) {
            VarsHelper.convertVariables(query, vars);
        }
        if (data != null) {
            VarsHelper.convertVariables(data, vars);
        }
        if (body != null) {
            VarsHelper.convertVariables(body, vars);
        }
    }

    private void buildRequest(JSONObject config) {
        MiGooRequest.Builder builder = new MiGooRequest.Builder()
                .method(method).query(query).data(data).body(body)
                .headers(headers == null ? config.getJSONObject("headers") : headers);
        Matcher matcher = pattern.matcher(url);
        request = matcher.find() ? builder.port(matcher.group(3) == null ? null : Integer.parseInt(matcher.group(3)))
                .protocol(matcher.group(1)).host(matcher.group(2)).api(matcher.group(4)).build()
                : builder.host(config.getString("host")).protocol(config.getString("protocol"))
                .port(config.getInteger("port")).api(config.getString("api")).build();
    }

    private void extractToVars(Vars vars){
        if (extract != null && !extract.isEmpty()) {
            for (String key : extract.keySet()) {
                String path = extract.getString(key);
                Object object = JSONPath.read(response.text(), path);
                if (object == null || "".equals(object)) {
                    throw new VarsException("value con not be null, path: " + path);
                }
                extract.put(key, object);
                vars.put(key, object);
            }
        }
    }

    private void printRequestLog() {
        request.printRequestLog();
        Report.log("extract vars: {}", extract.toJSONString());
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
