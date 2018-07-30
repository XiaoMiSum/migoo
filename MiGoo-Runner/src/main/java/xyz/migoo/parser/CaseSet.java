package xyz.migoo.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author xiaomi
 * @date 2018/7/28 14:54
 */
public class CaseSet {

    private Config config;

    private String name;

    private List<Case> cases;

    public List<Case> getCases() {
        return cases;
    }

    public void setCases(List<Case> cases) {
        this.cases = cases;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }


    public static class Case {

        private String title;
        private JSONObject body;
        private JSON validate;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public JSONObject getBody() {
            return body;
        }

        public void setBody(JSONObject body) {
            this.body = body;
        }

        public JSON getValidate() {
            return validate;
        }

        public void setValidate(JSON validate) {
            this.validate = validate;
        }
    }

    public static class Config{
        private JSONObject request;

        public JSONObject getRequest() {
            return request;
        }

        public void setRequest(JSONObject request) {
            this.request = request;
        }
    }

}
