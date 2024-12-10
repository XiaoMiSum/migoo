package coder.xyz.migoo.protocol;

import coder.xyz.migoo.El;

public class JDBC extends El {

    JDBC() {
    }

    JDBC(String name) {
        p("variable_name", name);
    }

    public JDBC url(String url) {
        p("url", url);
        return this;
    }

    public JDBC username(String username) {
        p("username", username);
        return this;
    }

    public JDBC password(String password) {
        p("password", password);
        return this;
    }

    public JDBC maxWait(int maxWait) {
        p("maxWait", maxWait);
        return this;
    }

    public JDBC maxActive(int maxActive) {
        p("maxActive", maxActive);
        return this;
    }

    public JDBC datasource(String datasource) {
        p("datasource", datasource);
        return this;
    }

    public JDBC statement(String statement) {
        p("statement", statement);
        return this;
    }
}
