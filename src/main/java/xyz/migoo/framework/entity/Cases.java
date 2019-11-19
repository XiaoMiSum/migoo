package xyz.migoo.framework.entity;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author yacheng.xiao
 * @date 2019/11/19 15:34
 */
public class Cases {

    private String title;

    private Config config;

    private JSONObject request;

    private JSONObject query;

    private JSONObject data;

    private JSONObject body;

    private List<Validate> validates;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public JSONObject getRequest() {
        return request;
    }

    public void setRequest(JSONObject request) {
        this.request = request;
    }

    public JSONObject getQuery() {
        return query;
    }

    public void setQuery(JSONObject query) {
        this.query = query;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public JSONObject getBody() {
        return body;
    }

    public void setBody(JSONObject body) {
        this.body = body;
    }

    public List<Validate> getValidates() {
        return validates;
    }

    public void setValidates(List<Validate> validates) {
        this.validates = validates;
    }
}
