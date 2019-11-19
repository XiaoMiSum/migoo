package xyz.migoo.framework.entity;

import java.util.List;

/**
 * @author yacheng.xiao
 * @date 2019/11/19 15:33
 */
public class MiGooCase {

    private String name;

    private Config config;

    private List<Cases> cases;

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

    public List<Cases> getCases() {
        return cases;
    }

    public void setCases(List<Cases> cases) {
        this.cases = cases;
    }
}
