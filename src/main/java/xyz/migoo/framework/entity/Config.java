package xyz.migoo.framework.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author xiaomi
 * @date 2019/11/19 15:37
 */
public class Config {

    private JSONObject variables;
    private JSONArray beforeClass;
    private JSONArray before;
    private JSONArray afterClass;
    private JSONArray after;
    private JSONObject request;
    private Integer ignore;


    public JSONObject getVariables() {
        return variables;
    }

    public void setVariables(JSONObject variables) {
        this.variables = variables;
    }

    public JSONArray getBeforeClass() {
        return beforeClass;
    }

    public void setBeforeClass(JSONArray beforeClass) {
        this.beforeClass = beforeClass;
    }

    public JSONArray getBefore() {
        return before;
    }

    public void setBefore(JSONArray before) {
        this.before = before;
    }

    public JSONArray getAfterClass() {
        return afterClass;
    }

    public void setAfterClass(JSONArray afterClass) {
        this.afterClass = afterClass;
    }

    public JSONArray getAfter() {
        return after;
    }

    public void setAfter(JSONArray after) {
        this.after = after;
    }

    public JSONObject getRequest() {
        return request;
    }

    public void setRequest(JSONObject request) {
        this.request = request;
    }

    public Integer getIgnore() {
        return ignore;
    }

    public void setIgnore(Integer ignore) {
        this.ignore = ignore;
    }
}
