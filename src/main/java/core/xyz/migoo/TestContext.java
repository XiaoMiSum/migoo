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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.utils.StringUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author xiaomi
 * @date 2020/12/18 20:19
 */
@Data
public class TestContext {

    private Object id;

    private String name;

    private String title;

    private JSONObject config;

    private JSONObject request;

    private JSONObject plugins;

    private JSONObject dataset;

    private List<TestStep> steps;

    private JSONObject query;

    private JSONObject body;

    private JSONObject data;

    private List<JSONObject> validators;

    private List<TestContext> sets;

    private List<TestContext> cases;

    public Object getId() {
        id = id == null ? UUID.randomUUID().toString() : id;
        return id;
    }

    public String getName() {
        return StringUtil.isEmpty(name) ? title : name;
    }

    public JSONObject getConfig() {
        config = config != null ? config : new JSONObject();
        return config;
    }

    public JSONObject getReportConfig() {
        return getConfig().get("report") != null ? getConfig().getJSONObject("report") : getConfig().getJSONObject("reports");
    }

    public JSONObject getMailConfig() {
        return getConfig().get("email") != null ? getConfig().getJSONObject("email") : getConfig().getJSONObject("mail");
    }

    public JSONObject getRequest() {
        request = request != null ? request : getConfig().get("request") != null ? getConfig().getJSONObject("request") : new JSONObject();
        return request;
    }

    public String getRequestProtocol() {
        return getRequest().getString("protocol");
    }

    public String getRequestHost() {
        return getRequest().getString("host");
    }

    public Integer getRequestPort() {
        return getRequest().getInteger("port");
    }

    public String getRequestMethod() {
        return getRequest().getString("method");
    }

    public String getRequestApi() {
        String api = getRequest().getString("api");
        return StringUtil.isEmpty(api) ? "" : api.startsWith("/") ? api : "/" + api;
    }
    public Object removeRequestApi() {
        return getRequest().remove("api");
    }

    public void setRequestApi(Object api) {
        if ("".equals(getRequestApi())) {
            getRequest().put("api", api);
        }
    }

    public JSONObject getRequestHeaders() {
        return getRequest().get("headers") != null ? getRequest().getJSONObject("headers") : new JSONObject();
    }

    public void setRequestHeaders(JSONObject headers) {
        getRequest().put("headers", headers);
    }

    public Object getRequestCookies() {
        return getRequest().get("cookies") == null ? getRequest().get("cookie") : getRequest().get("cookies");
    }

    public void setRequestCookies(Object cookies) {
        getRequest().put("cookies", cookies);
    }

    public JSONObject getDataset() {
        dataset = dataset != null ? dataset : new JSONObject();
        return dataset;
    }

    public JSONObject getVariables() {
        return getDataset().get("vars") != null ? getDataset().getJSONObject("vars")
                : getDataset().get("variables") != null ? getDataset().getJSONObject("variables") : new JSONObject();
    }

    public JSONArray getSetupHook() {
        return getDataset().get("setup") != null ? getDataset().getJSONArray("setup") : new JSONArray();
    }

    public JSONArray getTeardownHook() {
        return getDataset().get("teardown") != null ? getDataset().getJSONArray("teardown") : new JSONArray();
    }

    public List<JSONObject> getValidators() {
        return validators != null ? validators : new ArrayList<>();
    }

    public boolean isSkipped() {
        return getConfig().getBooleanValue("skip");
    }
}
