package coder.xyz.migoo.protocol;

import coder.xyz.migoo.El;

import java.util.Map;

public class Mongo extends El {

    Mongo() {
    }

    Mongo(String name) {
        p("variable_name", name);
    }

    public Mongo url(String url) {
        p("url", url);
        return this;
    }

    public Mongo database(String database) {
        p("database", database);
        return this;
    }

    public Mongo collection(String collection) {
        p("collection", collection);
        return this;
    }

    public Mongo datasource(String datasource) {
        p("datasource", datasource);
        return this;
    }

    public Mongo action(String action) {
        p("action", action);
        return this;
    }

    public Mongo data(Map<String, Object> data) {
        p("data", data);
        return this;
    }

    public Mongo condition(Map<String, Object> condition) {
        p("condition", condition);
        return this;
    }
}
